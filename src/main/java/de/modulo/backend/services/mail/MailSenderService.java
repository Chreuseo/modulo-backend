package de.modulo.backend.services.mail;

import de.modulo.backend.entities.NotificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    private final MailServiceImpl mailService;

    @Autowired
    public MailSenderService(MailServiceImpl mailService) {
        this.mailService = mailService;
    }

    public void sendMail(String recipient, String subject, String text, AttachmentType ...attachments) {
        mailService.sendMail(recipient, subject, text, false, attachments);
    }

    public void sendHtmlMail(String recipient, String subject, String text, AttachmentType ...attachments) {
        mailService.sendMail(recipient, subject, text, true, attachments);
    }

    public void sendMail(NotificationEntity notificationEntity, AttachmentType ...attachments) {
        mailService.sendMail(notificationEntity.getUser().getMail(), notificationEntity.getTitle(), notificationEntity.getMessage(), false, attachments);
    }
}
