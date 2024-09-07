package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "course_type")
@Data
public class CourseTypeEntity {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String abbreviation;
}
