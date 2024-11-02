package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notification_entity")
@Data
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String message;

    private boolean unread;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
