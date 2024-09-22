package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "session_entity")
@Data
public class SessionEntity {

    @Id
    private UUID token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private long expirationDate;

    private long creationDate;

    private long lastAccessDate;

    private String ip;
}
