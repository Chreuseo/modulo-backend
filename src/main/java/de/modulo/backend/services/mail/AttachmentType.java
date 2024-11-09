package de.modulo.backend.services.mail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttachmentType {

    private byte[] attachment;
    private String attachmentFilename;
}
