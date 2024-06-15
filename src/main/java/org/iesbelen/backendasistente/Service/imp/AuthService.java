package org.iesbelen.backendasistente.Service.imp;

import org.iesbelen.backendasistente.Exceptions.CustomException;
import org.iesbelen.backendasistente.Model.Attempts;
import org.iesbelen.backendasistente.Model.Users;
import org.iesbelen.backendasistente.Service.IAttemptsService;
import org.iesbelen.backendasistente.Service.IAuthService;
import org.iesbelen.backendasistente.Service.ICheckPasswordService;
import org.iesbelen.backendasistente.Service.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class AuthService implements IAuthService {

    private final IAttemptsService attemptsService;

    private final IUserService userService;

    private final ICheckPasswordService checkPassword;

    private final int MAX_ATTEMPTS_FAILED;

    public AuthService(IAttemptsService attemptsService, IUserService userService, ICheckPasswordService checkPassword, @Value("${max.attepmts.failed}") int maxAttemptsFailed) {
        this.attemptsService = attemptsService;
        this.userService = userService;
        this.checkPassword = checkPassword;
        MAX_ATTEMPTS_FAILED = maxAttemptsFailed;
    }
    @Override
    public boolean authenticate(String username, String password, Attempts attempt) throws CustomException {
        Users user = userService.findByUserName(username).orElse(null);
        if (user == null) {
            throw new CustomException("Usuario no encontrado: " + username);
        }
        if (user.isCuentaBloqueada()) {
            throw new CustomException("Usuario Bloqueado");
        }
        if (userService.checkPassword(password, user.getUserpassword())) {
            return true;
        } else {
            long intentosFallidos = attemptsService.countFailedAttemptsPerUserInTimePeriod(user, Timestamp.valueOf(LocalDateTime.now().minusHours(24)));
            if (intentosFallidos >= MAX_ATTEMPTS_FAILED) {
                user.setCuentaBloqueada(true);
                userService.saveUser(user);
            }
            return false; // Autenticación fallida
        }
    }

    @Override
    public boolean authenticateForgotten(String username, String email, String newPassword) throws CustomException {
        Users user = userService.findByUserName(username).orElse(null);

        if (user == null) {
            return false;
        }

        if (user.getUsername().equals(username) && user.getEmail().equals(email)){
            user.setUserpassword(newPassword);
            userService.saveUser(user);
            return true;
        }

        return false;
    }

    @Override
    public boolean authenticateChangePassword(String username, String password, String confirmPassword) throws CustomException {
        Users user = userService.findByUserName(username).orElse(null);
        if (user == null) {
            return false;
        }
        boolean validPassword = checkPassword(password);

        if (validPassword){
            if(password.equals(confirmPassword)){
                if(!userService.checkPassword(password,user.getUserpassword())){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean checkPassword(String password) throws CustomException {
        return checkPassword.checkPassword(password);
    }


}
