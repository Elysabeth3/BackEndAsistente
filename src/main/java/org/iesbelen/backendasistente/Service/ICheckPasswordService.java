package org.iesbelen.backendasistente.Service;

import org.iesbelen.backendasistente.Exceptions.CustomException;

public interface ICheckPasswordService {

    boolean checkPassword(String password) throws CustomException;
}
