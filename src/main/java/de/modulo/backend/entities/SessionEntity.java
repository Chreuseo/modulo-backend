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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionEntity that)) return false;
        return token.equals(that.token);
    }
}
