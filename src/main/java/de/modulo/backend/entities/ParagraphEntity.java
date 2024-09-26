package de.modulo.backend.entities;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "paragraph_entity")
@Data
public class ParagraphEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Define many-to-one relationship with SpoEntity
    @JoinColumn(name = "spo_id")
    private SpoEntity spo;


    private String title;
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParagraphEntity that)) return false;
        return id.equals(that.id);
    }
}
