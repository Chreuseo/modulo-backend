package de.modulo.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ModuleImplementationDTO {

    private Long id;
    private String name;
    private String abbreviation;
    private String courseType;
    private String allowedResources; // Fixed typo to match the entity
    private UserDTO firstExaminant; // Assuming to create UserDTO class
    private UserDTO secondExaminant;
    private UserDTO responsible;
    private CycleDTO cycle; // Assuming to create CycleDTO class
    private DurationDTO duration; // Assuming to create DurationDTO class
    private LanguageDTO language; // Assuming to create LanguageDTO class
    private String requiredCompetences;
    private String qualificationTargets;
    private String content;
    private MaternityProtectionDTO maternityProtection; // Assuming to create MaternityProtectionDTO class

    private List<ModuleMappingDTO> moduleMappingDTOs;
}
