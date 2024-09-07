package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exam_type_module_implementation")
@Getter
@Setter
@NoArgsConstructor
public class ExamTypeModuleImplementationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_type_id", nullable = false)
    private ExamTypeEntity examType; // Assuming ExamTypeEntity is defined elsewhere

    @ManyToOne
    @JoinColumn(name = "module_implementation_id", nullable = false)
    private ModuleImplementationEntity moduleImplementation;

    private String description;

    // Constructors, getters, and setters can be generated using Lombok annotations
}
