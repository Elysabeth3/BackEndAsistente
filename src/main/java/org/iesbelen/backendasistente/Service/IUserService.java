package org.iesbelen.backendasistente.Service;

import org.iesbelen.backendasistente.Exceptions.CustomException;
import org.iesbelen.backendasistente.Model.Users;

import java.util.Optional;

public interface IUserService{


    Optional<Users> findByUserName(String username) throws CustomException;

    Optional<Users> findByEmail(String email) throws CustomException;

    Users saveUser(Users user);

    boolean checkPassword(String contrasenaIngresada, String contrasenaAlmacenada);

    Users changePassword(String username, String password);
}
