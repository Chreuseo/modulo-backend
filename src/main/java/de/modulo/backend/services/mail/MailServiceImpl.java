package de.modulo.backend.services.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Data
public class MailServiceImpl {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${custom.sender.name}")
    private String senderName;
    @Value("${spring.mail.username}")
    private String senderAddress;

    public void sendMail(String recipient, String subject, String text, AttachmentType ...attachments) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            String from = senderName + " <" + senderAddress + ">";
            helper.setFrom(from);

            helper.setTo(recipient);

            helper.setSubject(subject);

            helper.setText(text);

            // Anhängen des PDFs, wenn vorhanden
            for (AttachmentType attachment : attachments) {
                helper.addAttachment(attachment.getAttachmentFilename(), new ByteArrayResource(attachment.getAttachment()), "application/pdf");
            }

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            // Hier können Sie zusätzliche Fehlerbehandlung einfügen, falls erforderlich
        }
    }

}
