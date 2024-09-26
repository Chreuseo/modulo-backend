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

    private String requiredCompetences;

    private String qualificationTargets;

    private String content;

    private String additionalExams;

    private String mediaTypes;

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
