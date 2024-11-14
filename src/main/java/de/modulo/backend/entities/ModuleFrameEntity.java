package de.modulo.backend.entities;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "module_frame_entity")
@Data
public class ModuleFrameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne // Relationship with SpoEntity
    @JoinColumn(name = "spo_id")
    private SpoEntity spo;

    @ManyToOne // Relationship with SectionEntity
    @JoinColumn(name = "section_id")
    private SectionEntity section;

    @ManyToOne // Relationship with ModuleTypeEntity
    @JoinColumn(name = "module_type_id")
    private ModuleTypeEntity moduleType;

    private int quantity;
    private String name;
    private int sws; // Semester Weekly Hours
    private int weight;
    private int credits;
    private boolean allExamsMandatory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleFrameEntity that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id) + spo.hashCode() + section.hashCode() + moduleType.hashCode() + quantity + name.hashCode() + sws + weight + credits;
    }
}
