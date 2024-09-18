package de.modulo.backend.dtos;

import lombok.Data;

import java.util.List;


@Data
public class ModuleImplementationDTO {

    private Long id;
    private String name;
    private String abbreviation;
    private String allowedResources; // Fixed typo to match the entity
    private UserDTOFlat firstExaminant; // Assuming to create UserDTO class
    private UserDTOFlat secondExaminant;
    private UserDTOFlat responsible;
    private List<UserDTOFlat> lecturers;
    private CycleDTO cycle; // Assuming to create CycleDTO class
    private DurationDTO duration; // Assuming to create DurationDTO class
    private LanguageDTO language; // Assuming to create LanguageDTO class
    private String workload;
    private String requiredCompetences;
    private String qualificationTargets;
    private String content;

    private String additionalExams;
    private String mediaTypes;
    private String literature;

    private MaternityProtectionDTO maternityProtection; // Assuming to create MaternityProtectionDTO class
}
