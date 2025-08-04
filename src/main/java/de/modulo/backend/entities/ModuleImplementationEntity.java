package de.modulo.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "module_implementation")
@Getter
@Setter
@NoArgsConstructor
public class ModuleImplementationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String abbreviation;

    @Column(name = "allowed_resources", columnDefinition = "TEXT")
    private String allowedResources;

    @ManyToOne
    @JoinColumn(name = "first_examinant_id")
    private UserEntity firstExaminant;

    @ManyToOne
    @JoinColumn(name = "second_examinant_id")
    private UserEntity secondExaminant;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    private UserEntity responsible;

    @ManyToOne
    @JoinColumn(name = "cycle_id")
    private CycleEntity cycle;

    @ManyToOne
    @JoinColumn(name = "duration_id")
    private DurationEntity duration;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private LanguageEntity language;

    private String workload;

    @Column(name = "required_competences", columnDefinition = "TEXT")
    private String requiredCompetences;

    @Column(name = "qualification_targets", columnDefinition = "TEXT")
    private String qualificationTargets;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "additional_exams", columnDefinition = "TEXT")
    private String additionalExams;

    @Column(name = "media_types", columnDefinition = "TEXT")
    private String mediaTypes;

    @Column(name = "literature", columnDefinition = "TEXT")
    private String literature;

    @ManyToOne
    @JoinColumn(name = "maternity_protection_id")
    private MaternityProtectionEntity maternityProtection;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleImplementationEntity that)) return false;
        return id.equals(that.id);
    }
}
