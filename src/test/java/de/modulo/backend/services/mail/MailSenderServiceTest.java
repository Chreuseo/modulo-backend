package de.modulo.backend.services.mail;

import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.entities.UserEntity;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailSenderServiceTest {

    @InjectMocks
    private MailSenderService mailSenderService;

    @Mock
    private MailServiceImpl mailService;

    @Mock
    private NotificationEntity notificationEntity;

    @Test
    void testSendMail() {
        // Arrange
        String recipient = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test email body";

        // Act
        mailSenderService.sendMail(recipient, subject, text);

        // Assert
        verify(mailService, times(1)).sendMail(recipient, subject, text, false);
    }

    @Test
    void testSendHtmlMail() {
        // Arrange
        String recipient = "recipient@example.com";
        String subject = "Test HTML Subject";
        String htmlText = "<html><body>Test HTML email body</body></html>";

        // Act
        mailSenderService.sendHtmlMail(recipient, subject, htmlText);

        // Assert
        verify(mailService, times(1)).sendMail(recipient, subject, htmlText, true);
    }

    @Test
    void testSendMail_WithNotificationEntity() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setMail("test@test.com");
        userEntity.setId(1L);
        userEntity.setFirstName("Test");
        userEntity.setLastName("User");
        String subject = "Test Notification Subject";
        String message = "Test Notification Message";
        when(notificationEntity.getUser()).thenReturn(userEntity); // User being mocked
        when(notificationEntity.getTitle()).thenReturn(subject);
        when(notificationEntity.getMessage()).thenReturn(message);

        // Act
        mailSenderService.sendMail(notificationEntity);

        // Assert
        verify(mailService, times(1)).sendMail(userEntity.getMail(), subject, message, false);
    }

    @Test
    void testSendMail_WithAttachments() {
        // Arrange
        String recipient = "recipient@example.com";
        String subject = "Test Attachment Email";
        String text = "Test email with attachment";
        AttachmentType attachment = new AttachmentType("content".getBytes(), "attachment.pdf");

        // Act
        mailSenderService.sendMail(recipient, subject, text, attachment);

        // Assert
        verify(mailService, times(1)).sendMail(recipient, subject, text, false, attachment);
    }
}
