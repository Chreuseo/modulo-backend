package de.modulo.backend.dtos;

import lombok.Data;

@Data
public class NotificationDTO {

        private long id;
        private String title;
        private String message;
        private boolean unread;
        private long userId;
}
