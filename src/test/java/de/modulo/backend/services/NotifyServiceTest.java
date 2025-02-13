package de.modulo.backend.services;

import de.modulo.backend.entities.*;
import de.modulo.backend.enums.NOTIFICATION;
import de.modulo.backend.repositories.NotificationRepository;
import de.modulo.backend.services.mail.MailSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotifyServiceTest {

    @InjectMocks
    private NotifyService notifyService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private MailSenderService mailSenderService;

    private UserEntity editor;
    private UserEntity user;
    private List<UserEntity> users;
    private NOTIFICATION notification;
    private ModuleImplementationEntity module;
    private SpoEntity spo;

    @BeforeEach
    public void setup() {
        editor = new UserEntity();
        editor.setFirstName("John");
        editor.setLastName("Doe");

        user = new UserEntity();
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setSendMailNotifications(true);

        users = new ArrayList<>();
        users.add(user);

        module = new ModuleImplementationEntity();
        module.setName("Calculus");

        spo = new SpoEntity();
        spo.setName("SPO");
        spo.setDegree(new DegreeEntity());
        spo.getDegree().setName("Bachelor");
    }

    @Test
    public void testSendNotification_SendsEmailsAndSavesNotifications1() {
        notification = NOTIFICATION.LECTURER_EDITED_MODULE;

        List<NotificationEntity> savedNotifications = new ArrayList<>();
        when(notificationRepository.saveAllAndFlush(any())).thenReturn(savedNotifications);

        List<NotificationEntity> result = notifyService.sendNotification(editor, users, notification, module);

        // Verify email sending
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(mailSenderService, times(1)).sendMail(notificationCaptor.capture());

        NotificationEntity sentNotification = notificationCaptor.getValue();
        assertEquals("Der Dozent John Doe hat das Modul Calculus bearbeitet.", sentNotification.getMessage());
        assertEquals(user, sentNotification.getUser());

        // Verify notifications are saved
        verify(notificationRepository, times(1)).saveAllAndFlush(any());
    }

    @Test
    public void testSendNotification_SendsEmailsAndSavesNotifications2() {
        notification = NOTIFICATION.MODULE_ADDED_TO_SPO;

        List<NotificationEntity> savedNotifications = new ArrayList<>();
        when(notificationRepository.saveAllAndFlush(any())).thenReturn(savedNotifications);

        List<NotificationEntity> result = notifyService.sendNotification(editor, users, notification, module, spo);

        // Verify email sending
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(mailSenderService, times(1)).sendMail(notificationCaptor.capture());

        NotificationEntity sentNotification = notificationCaptor.getValue();
        assertEquals("Der Dozent John Doe hat das Modul Calculus zum Studiengang SPO Bachelor hinzugefügt.", sentNotification.getMessage());
        assertEquals(user, sentNotification.getUser());

        // Verify notifications are saved
        verify(notificationRepository, times(1)).saveAllAndFlush(any());
    }

    @Test
    public void testSendNotification_SendsEmailsAndSavesNotifications3() {
        notification = NOTIFICATION.SPO_CREATED;

        List<NotificationEntity> savedNotifications = new ArrayList<>();
        when(notificationRepository.saveAllAndFlush(any())).thenReturn(savedNotifications);

        List<NotificationEntity> result = notifyService.sendNotification(editor, users, notification, spo);

        // Verify email sending
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(mailSenderService, times(1)).sendMail(notificationCaptor.capture());

        NotificationEntity sentNotification = notificationCaptor.getValue();
        assertEquals("Der Dozent John Doe hat den Studiengang SPO Bachelor erstellt.", sentNotification.getMessage());
        assertEquals(user, sentNotification.getUser());

        // Verify notifications are saved
        verify(notificationRepository, times(1)).saveAllAndFlush(any());
    }

    @Test
    public void testSendNotification_SendsEmailsAndSavesNotifications4() {
        notification = NOTIFICATION.MODULE_CREATED;

        List<NotificationEntity> savedNotifications = new ArrayList<>();
        when(notificationRepository.saveAllAndFlush(any())).thenReturn(savedNotifications);

        List<NotificationEntity> result = notifyService.sendNotification(editor, users, notification, module);

        // Verify email sending
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(mailSenderService, times(1)).sendMail(notificationCaptor.capture());

        NotificationEntity sentNotification = notificationCaptor.getValue();
        assertEquals("Der Dozent John Doe hat das Modul Calculus erstellt.", sentNotification.getMessage());
        assertEquals(user, sentNotification.getUser());

        // Verify notifications are saved
        verify(notificationRepository, times(1)).saveAllAndFlush(any());
    }

    @Test
    public void testSendNotification_SendsEmailsAndSavesNotifications5() {
        notification = NOTIFICATION.DOCUMENTS_GENERATED;

        List<NotificationEntity> savedNotifications = new ArrayList<>();
        when(notificationRepository.saveAllAndFlush(any())).thenReturn(savedNotifications);

        List<NotificationEntity> result = notifyService.sendNotification(editor, users, notification);

        // Verify email sending
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(mailSenderService, times(1)).sendMail(notificationCaptor.capture());

        NotificationEntity sentNotification = notificationCaptor.getValue();
        assertEquals("Die angeforderten Dokumente wurden generiert.", sentNotification.getMessage());
        assertEquals(user, sentNotification.getUser());

        // Verify notifications are saved
        verify(notificationRepository, times(1)).saveAllAndFlush(any());
    }


    @Test
    public void testSendNotification_UserDoesNotReceiveEmail() {
        notification = NOTIFICATION.LECTURER_EDITED_MODULE;

        UserEntity userWithoutEmail = new UserEntity();
        userWithoutEmail.setSendMailNotifications(false);
        users.add(userWithoutEmail);

        when(notificationRepository.saveAllAndFlush(any())).thenReturn(new ArrayList<>());

        notifyService.sendNotification(editor, users, notification, module);

        // Verify that email is not sent for the user who doesn't want it
        verify(mailSenderService, times(1)).sendMail(any());
        verify(mailSenderService, times(1)).sendMail(any());
    }

    @Test
    public void testSendNotification_NoUsers() {
        notification = NOTIFICATION.LECTURER_EDITED_MODULE;

        List<NotificationEntity> result = notifyService.sendNotification(editor, new ArrayList<>(), notification, module);

        // Verify that no notifications were sent or saved
        verify(mailSenderService, never()).sendMail(any());
        assertTrue(result.isEmpty());
    }
}
