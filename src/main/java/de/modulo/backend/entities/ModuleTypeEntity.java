package de.modulo.backend.entities;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "module_type_entity")
@Data
public class ModuleTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne // Define many-to-one relationship with SpoEntity
    @JoinColumn(name = "spo_id")
    private SpoEntity spo;

    private String name;

    private int orderNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleTypeEntity that)) return false;
        return id == that.id;
    }
}
