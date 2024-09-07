package de.modulo.backend.entities;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_type_module_frame_entity")
@Data
public class ExamTypeModuleFrameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Primary key

    @ManyToOne // Relationship with ExamTypeEntity
    @JoinColumn(name = "exam_type_id", nullable = false)
    private ExamTypeEntity examType;

    @ManyToOne // Relationship with ModuleFrameEntity
    @JoinColumn(name = "module_frame_id", nullable = false)
    private ModuleFrameEntity moduleFrame;

    // Additional methods or annotations can be added if needed
}
