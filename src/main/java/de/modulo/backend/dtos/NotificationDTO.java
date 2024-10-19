package de.modulo.backend.dtos;

import lombok.Data;

@Data
public class NotificationDTO {

        private long id;
        private String title;
        private String message;
        private boolean read;
        private long userId;
}
