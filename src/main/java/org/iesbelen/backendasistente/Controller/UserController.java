package org.iesbelen.backendasistente.Controller;

import jakarta.mail.MessagingException;
import org.iesbelen.backendasistente.Exceptions.CustomException;
import org.iesbelen.backendasistente.Model.*;
import org.iesbelen.backendasistente.Model.DTO.ChangePasswordRequest;
import org.iesbelen.backendasistente.Model.DTO.ForgottenRequest;
import org.iesbelen.backendasistente.Model.DTO.LoginRequest;
import org.iesbelen.backendasistente.Service.IAttemptsService;
import org.iesbelen.backendasistente.Service.IAuthService;
import org.iesbelen.backendasistente.Service.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;

@RestController
@RequestMapping("/asistente/user")
public class UserController {

    private final IUserService userService;

    private final IAuthService authService;

    private final IAttemptsService attemptsService;

    private final PasswordEncoder passwordEncoder;

    @Value("${email}")
    private String email;

    @Value("${password}")
    private String password;

    public UserController(IUserService userService, IAuthService authService, IAttemptsService attemptsService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authService = authService;
        this.attemptsService = attemptsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users request) throws CustomException {
        Users user = userService.findByUserName(request.getUsername()).orElse(null);

        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya está en uso");
        }

        try {
            if(authService.checkPassword(request.getUserpassword())){
                userService.saveUser(Users.builder()
                        .username(request.getUsername())
                        .userpassword(request.getUserpassword())
                        .nombre(request.getNombre())
                        .email(request.getEmail())
                        .cuentaBloqueada(false)
                        .build());
            }
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest request) throws CustomException {
        String username = request.getUsername();
        String password = request.getUserpassword();
        Attempts attempts = new Attempts();

        attempts.setIpAddress(request.getIpAddress());

        attempts.setAttemptTime(new Timestamp(System.currentTimeMillis()));

        System.out.println(attempts);
        boolean authenticated;
        try {
            authenticated = authService.authenticate(username,password,attempts);
        } catch (CustomException e) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        if(authenticated){
            return ResponseEntity.ok("Autenticación exitosa");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
    }

    @PostMapping("/forgotten")
    public ResponseEntity<String> forgottenPassword(@RequestBody ForgottenRequest request) throws CustomException {
        boolean authenticated;
        String newPassword = passwordEncoder.encode("temporal");
        try {
            authenticated = authService.authenticateForgotten(request.getUsername(),request.getEmail(), newPassword);
        } catch (CustomException e) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        if(authenticated){
            GestorEmail gestorEmail = new GestorEmail();
            String asunto = "Cambio de contraseña: ";
            String mensaje = "Le enviamos una contraseña temporal. \n Recuerde cambiar su contraseña nuevamente. \n" + newPassword;
            try {
                gestorEmail.enviarMensajeTexto(email,request.getEmail(),asunto,mensaje,email,password);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.ok("Mensaje enviado puede tardar un poco");
    }

    @GetMapping("/{username}")
    public Users datosUsuario(@PathVariable String username){
        Users user;
        try {
             user = userService.findByUserName(username).orElse(null);
        } catch (CustomException e) {
            throw new RuntimeException(e);
        }
        if (user != null){
            return user;
        }
        return null;
    }

    @PostMapping("/{username}")
    public ResponseEntity<String> changePassword(@PathVariable String username, @RequestBody ChangePasswordRequest request){
        try {
            if (authService.checkPassword(request.getPassword())){
                if (authService.authenticateChangePassword(username,request.getPassword(),request.getConfirmPassword())){
                    userService.changePassword(username,request.getPassword());
                    return ResponseEntity.ok("Contraseña cambiada correctamente");
                }
            }
        } catch (CustomException e) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Misma Contraseña");
    }
}
