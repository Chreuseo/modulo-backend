package de.modulo.backend.excpetions;

import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.NOTIFICATION;
import de.modulo.backend.services.NotifyService;
import lombok.Data;

import java.util.List;

@Data
public class NotifyException extends Exception{
    private UserEntity editor;
    private List<UserEntity> userEntities;
    private final NotifyService notifyService;
    private NOTIFICATION notification;
    private Object[] editedObject;

    public NotifyException(NotifyService notifyService,
                           String message,
                           UserEntity editor,
                           List<UserEntity> userEntities,
                           NOTIFICATION notification,
                           Object ...editedObject) {
        super(message);
        this.notifyService = notifyService;
        this.editor = editor;
        this.userEntities = userEntities;
        this.notification = notification;
        this.editedObject = editedObject;
    }

    public NotifyException(NotifyService notifyService,
                           UserEntity editor,
                           List<UserEntity> userEntities,
                           NOTIFICATION notification,
                           Object ...editedObject) {
        this.notifyService = notifyService;
        this.editor = editor;
        this.userEntities = userEntities;
        this.notification = notification;
        this.editedObject = editedObject;
    }

    public void sendMailNotification(){
        for(UserEntity user : userEntities){
            notifyService.sendMailNotification(editor, user, notification, editedObject);
        }
    }

    private void sendInAppNotification(){
        for(UserEntity user : userEntities){
            notifyService.sendNotification(editor, user, notification, editedObject);
        }
    }

    public void sendNotification(){
        sendInAppNotification();
        if(editor.isSendMailNotifications()){
            sendMailNotification();
        }
    }
}
