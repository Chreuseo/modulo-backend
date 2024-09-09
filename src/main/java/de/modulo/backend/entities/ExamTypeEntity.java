package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "exam_type")
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class ExamTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "spo_id", nullable = false)
    private SpoEntity spo;

    @Column(name = "name", nullable = false)
    private String name;

    private String abbreviation;

    private String length;
}
