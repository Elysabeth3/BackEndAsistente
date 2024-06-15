package org.iesbelen.backendasistente.Service.imp;

import org.iesbelen.backendasistente.Exceptions.CustomException;
import org.iesbelen.backendasistente.Model.Users;
import org.iesbelen.backendasistente.Mapper.IUserMapper;
import org.iesbelen.backendasistente.Service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final IUserMapper userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(IUserMapper userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Users> findByUserName(String username) throws CustomException {
        Optional<Users> userOptional = userRepository.findUserByUsername(username);
        return userOptional;
    }

    @Override
    public Optional<Users> findByEmail(String email) throws CustomException {
        Optional<Users> usuario = userRepository.findByEmail(email);
        if (usuario.get() == null) {
            throw new CustomException("Email no encontrado: " + email);
        }
        return usuario;
    }

    @Override
    public Users saveUser(Users user) {
        user.setUserpassword(passwordEncoder.encode(user.getUserpassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean checkPassword(String contrasenaIngresada, String contrasenaAlmacenada) {
        System.out.println(contrasenaIngresada);
        System.out.println(contrasenaAlmacenada);
        return passwordEncoder.matches(contrasenaIngresada, contrasenaAlmacenada);
    }

    @Override
    public Users changePassword(String username, String password){
        try {
            Users user = findByUserName(username).orElse(null);
            user.setUserpassword(password);
            saveUser(user);
            return user;
        } catch (CustomException e) {
            throw new RuntimeException(e);
        }
    }

}
