package de.modulo.backend.entities;

import de.modulo.backend.enums.DOCUMENT_TYPE;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_entity")
@Data
@NoArgsConstructor
public class DocumentEntity {

    public DocumentEntity(SpoEntity spo, SemesterEntity semester, DOCUMENT_TYPE type, LocalDateTime generatedAt) {
        this.spo = spo;
        this.semester = semester;
        this.type = type;
        this.generatedAt = generatedAt;
    }

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

    private LocalDateTime generatedAt;
}
