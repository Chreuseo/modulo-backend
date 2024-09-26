package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "module_implementation_lecturer")
@Getter
@Setter
@NoArgsConstructor
public class ModuleImplementationLecturerEntity {

    @EmbeddedId
    private ModuleImplementationLecturerEntityId id;

    @ManyToOne
    @JoinColumn(name = "module_implementation_id", nullable = false)
    private ModuleImplementationEntity moduleImplementation;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private UserEntity lecturer;

    @Embeddable
    @Data
    public static class ModuleImplementationLecturerEntityId implements Serializable {
        private Long moduleImplementation;
        private Long lecturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleImplementationLecturerEntity that)) return false;
        return id.equals(that.id);
    }
}
