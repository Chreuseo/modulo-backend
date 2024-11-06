package de.modulo.backend.services;

import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.NOTIFICATION;
import de.modulo.backend.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotifyService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationEntity sendNotification(UserEntity editor, UserEntity user, NOTIFICATION notification, Object ...editedObject){
        NotificationEntity notificationEntity = generateNotificationEntity(editor, user, notification, editedObject);
        return notificationRepository.save(notificationEntity);
    }

    private String generateNotificationText(UserEntity editor, NOTIFICATION notification, Object ...editedObject){
        switch(notification){
            case LECTURER_EDITED_MODULE -> {
                return notification.getMessage().
                        replace("[editor]", editor.getFirstName() + " " + editor.getLastName()).
                        replace("[module]", ((ModuleImplementationEntity) editedObject[0]).getName());
            }
            case MODULE_ADDED_TO_SPO -> {
                return notification.getMessage().
                        replace("[editor]", editor.getFirstName() + " " + editor.getLastName()).
                        replace("[module]", ((ModuleImplementationEntity) editedObject[0]).getName()).
                        replace("[spo]", ((SpoEntity) editedObject[1]).getName() + " " + ((SpoEntity) editedObject[1]).getDegree().getName());
            }default -> {
                return "";
            }
        }
    }

    private NotificationEntity generateNotificationEntity(UserEntity editor, UserEntity user, NOTIFICATION notification, Object ...editedObject){
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setUser(user);
        notificationEntity.setMessage(generateNotificationText(editor, notification, editedObject));
        notificationEntity.setUnread(true);
        return notificationEntity;
    }

}

