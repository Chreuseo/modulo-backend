package de.modulo.backend.excpetions;

import de.modulo.backend.authentication.NotifyService;
import de.modulo.backend.entities.UserEntity;

public class NotifyException extends Exception{
    UserEntity userEntity;

    public NotifyException(String message, UserEntity userEntity) {
        super(message);
        this.userEntity = userEntity;
    }
}
