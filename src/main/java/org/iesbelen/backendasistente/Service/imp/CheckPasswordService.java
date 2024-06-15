package org.iesbelen.backendasistente.Service.imp;

import org.iesbelen.backendasistente.Exceptions.CustomException;
import org.iesbelen.backendasistente.Service.ICheckPasswordService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CheckPasswordService implements ICheckPasswordService {

    private String password;

    @Override
    public boolean checkPassword(String password) throws CustomException {
        this.password = password;

        if (!checkLongPassword()){
            throw new CustomException("Contraseña muy corta");
        }

        if (!checkMinMayusPassword()){
            throw new CustomException("La contraseña debe contener Mayusculas y Minusculas");
        }

        if (!checkNumsPassword()){
            throw new CustomException("La contraseña debe contener Numeros");
        }

        if (!checkEspecialCharacters()){
            throw new CustomException("La contraseña debe inculir caracteres especiales");
        }

        return true;

    }
    private boolean checkLongPassword(){
        return password.length() >= 8;
    }

    private boolean checkMinMayusPassword(){
        String contieneMayusculasYMinusculas = "(?=.*[a-z])(?=.*[A-Z])";
        if (Pattern.compile(contieneMayusculasYMinusculas).matcher(password).find()){
            return true;
        }
        return false;
    }

    private boolean checkNumsPassword(){
        String contieneNumeros = ".*\\d.*";
        if (Pattern.compile(contieneNumeros).matcher(password).find()){
            return true;
        }
        return false;
    }

    private boolean checkEspecialCharacters(){
        String contieneCaracteresEspeciales = ".*[^a-zA-Z0-9\\s].*";
        if (Pattern.compile(contieneCaracteresEspeciales).matcher(password).find()){
            return true;
        }
        return false;
    }
}
