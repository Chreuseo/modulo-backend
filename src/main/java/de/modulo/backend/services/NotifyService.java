package de.modulo.backend.services;

import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.NOTIFICATION;
import de.modulo.backend.services.mail.MailSenderService;
import de.modulo.backend.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotifyService {

    private final NotificationRepository notificationRepository;
    private final MailSenderService mailSenderService;

    @Autowired
    public NotifyService(NotificationRepository notificationRepository,
                         MailSenderService mailSenderService) {
        this.notificationRepository = notificationRepository;
        this.mailSenderService = mailSenderService;
    }

    public List<NotificationEntity> sendNotification(UserEntity editor,
                                               List<UserEntity> users,
                                               NOTIFICATION notification,
                                               Object ...editedObject){
        List<NotificationEntity> notificationEntities = new ArrayList<>();
        for(UserEntity user : users){
            NotificationEntity notificationEntity = generateNotificationEntity(editor, user, notification, editedObject);
            notificationEntities.add(notificationEntity);
            if(user.isSendMailNotifications()){
                mailSenderService.sendMail(notificationEntity);
            }
        }
        return notificationRepository.saveAllAndFlush(notificationEntities);
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
                return notification.getMessage().
                        replace("[editor]", editor.getFirstName() + " " + editor.getLastName()).
                        replace("[spo]", ((SpoEntity) editedObject[0]).getName() + " " + ((SpoEntity) editedObject[0]).getDegree().getName());
            }
            case MODULE_CREATED -> {
                return notification.getMessage().
                        replace("[editor]", editor.getFirstName() + " " + editor.getLastName()).
                        replace("[module]", ((ModuleImplementationEntity) editedObject[0]).getName());
            }
            case DOCUMENTS_GENERATED -> {
                return notification.getMessage();
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

