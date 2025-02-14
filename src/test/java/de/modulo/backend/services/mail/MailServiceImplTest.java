package de.modulo.backend.services.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    public void setUp() {
        mailService.setSenderName("Test Sender");
        mailService.setSenderAddress("test@example.com");
    }

    @Test
    void testSendMail_Successful() throws Exception {
        // Arrange
        String recipient = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test email body";
        boolean isHtml = false;

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        mailService.sendMail(recipient, subject, text, isHtml);

        // Assert
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendMail_WithAttachments() throws Exception {
        // Arrange
        String recipient = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test email body";
        boolean isHtml = false;

        byte[] attachmentData = "Test Attachment".getBytes();
        de.modulo.backend.services.mail.AttachmentType attachment = new de.modulo.backend.services.mail.AttachmentType(attachmentData, "test.pdf");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        mailService.sendMail(recipient, subject, text, isHtml, attachment);

        // Assert
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendMail_ExceptionThrown() throws Exception {
        // Arrange
        String recipient = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test email body";
        boolean isHtml = false;

        when(javaMailSender.createMimeMessage()).thenThrow(new RuntimeException("Mail exception"));

        // Act & Assert
        try {
            mailService.sendMail(recipient, subject, text, isHtml);
        } catch (Exception e) {
            assertInstanceOf(RuntimeException.class, e);
            assertEquals("Mail exception", e.getMessage());
        }

        // Verify that the send method on javaMailSender was never called
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    private class AttachmentType {
        private String attachmentFilename;
        private byte[] attachment;

        public AttachmentType(String attachmentFilename, byte[] attachment) {
            this.attachmentFilename = attachmentFilename;
            this.attachment = attachment;
        }

        public String getAttachmentFilename() {
            return attachmentFilename;
        }

        public byte[] getAttachment() {
            return attachment;
        }
    }
}
