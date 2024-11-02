package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notification_entity")
@Data
public class NotificationEntity {

    @Id
    private long id;

    private String title;
    private String message;

    @Column(name = "is_read")
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
