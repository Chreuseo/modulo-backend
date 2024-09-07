package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "module_implementation_lecturer")
@Getter
@Setter
@NoArgsConstructor
public class ModuleImplementationLecturerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module_implementation_id", nullable = false)
    private ModuleImplementationEntity moduleImplementation;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private UserEntity lecturer;

    // Constructors, getters, and setters can be generated using Lombok annotations
}
