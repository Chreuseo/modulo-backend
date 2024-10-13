package de.modulo.backend.excpetions;

import de.modulo.backend.entities.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class NotifyException extends Exception{
    private UserEntity editor;
    private List<UserEntity> userEntities;

    public NotifyException(String message, UserEntity editor, List<UserEntity> userEntities) {
        super(message);
        this.editor = editor;
        this.userEntities = userEntities;
    }

    public NotifyException(UserEntity editor, List<UserEntity> userEntities) {
        this.editor = editor;
        this.userEntities = userEntities;
    }

    public void sendMailNotification(){
        // TODO
    }

    private void sendInAppNotification(){
        // TODO
    }

    public void sendNotification(){
        // TODO
    }
}
