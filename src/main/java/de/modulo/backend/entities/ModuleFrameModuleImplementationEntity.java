package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "module_frame_module_implementation")
@Getter
@Setter
@NoArgsConstructor
public class ModuleFrameModuleImplementationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module_frame_id", nullable = false)
    private ModuleFrameEntity moduleFrame;

    @ManyToOne
    @JoinColumn(name = "module_implementation_id", nullable = false)
    private ModuleImplementationEntity moduleImplementation;

    @ManyToOne
    @JoinColumn(name = "module_requirement_id")
    private ModuleRequirementEntity moduleRequirement;

    private String semester;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleFrameModuleImplementationEntity that)) return false;
        return id.equals(that.id);
    }
}
