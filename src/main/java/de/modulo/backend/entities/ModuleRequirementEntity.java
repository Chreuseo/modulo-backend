package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "module_requirements")
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class ModuleRequirementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "spo_id", nullable = false)
    private SpoEntity spo; // Assuming SpoEntity is defined elsewhere

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleRequirementEntity that)) return false;
        return id == that.id;
    }
}
