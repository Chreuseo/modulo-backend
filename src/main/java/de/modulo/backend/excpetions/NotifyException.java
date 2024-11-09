package de.modulo.backend.excpetions;

import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.NOTIFICATION;
import de.modulo.backend.services.NotifyService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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

    public void sendMailNotification(NotificationEntity notificationEntity, UserEntity user){
        // TODO
    }

    public void sendNotification(){
        notifyService.sendNotification(editor, userEntities, notification, editedObject);
    }
}
