package de.modulo.backend.services;

import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.NOTIFICATION;
import de.modulo.backend.repositories.NotificationRepository;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    private final NotificationRepository notificationRepository;
    private final SpoRepository spoRepository;

    @Autowired
    public NotifyService(NotificationRepository notificationRepository, SpoRepository spoRepository) {
        this.notificationRepository = notificationRepository;
        this.spoRepository = spoRepository;
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
            }
            case SPO_CREATED -> {
                SpoEntity spo = (SpoEntity) editedObject[0];
                SpoEntity spo1 = spoRepository.findById(spo.getId()).orElse(spo);
                return notification.getMessage().
                        replace("[editor]", editor.getFirstName() + " " + editor.getLastName()).
                        replace("[spo]", (spo1.getName() + " " + (spo1.getDegree().getName())));
            }
            case MODULE_CREATED -> {
                return notification.getMessage().
                        replace("[editor]", editor.getFirstName() + " " + editor.getLastName()).
                        replace("[module]", ((ModuleImplementationEntity) editedObject[0]).getName());
            }
            default -> {
                return "";
            }
        }
    }

    private NotificationEntity generateNotificationEntity(UserEntity editor, UserEntity user, NOTIFICATION notification, Object ...editedObject){
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setUser(user);
        notificationEntity.setTitle(notification.getTitle());
        notificationEntity.setMessage(generateNotificationText(editor, notification, editedObject));
        notificationEntity.setUnread(true);
        return notificationEntity;
    }

}

