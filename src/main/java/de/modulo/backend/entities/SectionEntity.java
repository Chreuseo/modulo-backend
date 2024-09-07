package de.modulo.backend.entities;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "section_entity")
@Data
public class SectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Define many-to-one relationship with SpoEntity
    @JoinColumn(name = "spo_id")
    private SpoEntity spo;

    private String name;

    private int orderNumber;

    // Additional methods or annotations can be added if needed
}
