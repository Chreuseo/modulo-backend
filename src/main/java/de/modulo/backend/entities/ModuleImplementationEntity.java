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

    private String courseType;

    private String allowedResources; // Note: Fixing typo "allowedRessources" to "allowedResources"

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

    @ManyToOne
    @JoinColumn(name = "module_requirement_id")
    private ModuleRequirementEntity moduleRequirement;

    private String requiredCompetences;

    private String qualificationTargets;

    private String content;

    @ManyToOne
    @JoinColumn(name = "maternity_protection_id")
    private MaternityProtectionEntity maternityProtection;

    // Constructors, getters, and setters can be generated using Lombok annotations
}
