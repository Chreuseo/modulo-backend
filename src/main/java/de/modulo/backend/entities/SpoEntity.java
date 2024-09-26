package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "spo_entity")
@Data
public class SpoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String header;
    private String footer;
    private String name;

    @Temporal(TemporalType.DATE)
    private Date publication;

    @Temporal(TemporalType.DATE)
    @Column(name = "valid_from")
    private Date validFrom; // New field

    @Temporal(TemporalType.DATE)
    @Column(name = "valid_until")
    private Date validUntil; // New field

    @ManyToOne // assuming degree is another entity
    @JoinColumn(name = "degree_id")
    private DegreeEntity degree;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpoEntity that)) return false;
        return id == that.id;
    }
}
