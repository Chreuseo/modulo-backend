package de.modulo.backend.entities;

import de.modulo.backend.enums.DOCUMENT_TYPE;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "document_entity")
@Data
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "spo_id")
    private SpoEntity spo;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;

    private DOCUMENT_TYPE type;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] data;

    private Date generatedAt;
}
