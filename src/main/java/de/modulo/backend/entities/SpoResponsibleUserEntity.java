package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "spo_responsible_user")
@Data
public class SpoResponsibleUserEntity {

    @EmbeddedId
    private SpoResponsibleUserId id;

    @ManyToOne
    @JoinColumn(name = "spo_id", nullable = false, updatable = false)
    private SpoEntity spo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserEntity user;

    @Embeddable
    @Data
    public static class SpoResponsibleUserId implements Serializable {
        private Long spoId;
        private Long userId;
    }
}
