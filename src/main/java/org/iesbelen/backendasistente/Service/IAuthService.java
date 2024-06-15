package org.iesbelen.backendasistente.Service;

import org.iesbelen.backendasistente.Exceptions.CustomException;
import org.iesbelen.backendasistente.Model.Attempts;

public interface IAuthService {
    boolean authenticate(String username, String password, Attempts attempt) throws CustomException;

    boolean authenticateForgotten(String username, String email, String newPassword) throws CustomException;

    boolean authenticateChangePassword(String username, String password, String confirmPassword) throws CustomException;

    boolean checkPassword(String password) throws CustomException;
}
