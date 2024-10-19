package de.modulo.backend.dtos;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class SpoDTO {
    private long id;
    private String header;
    private String footer;
    private String name;
    private Date publication;
    private Date validFrom;
    private Date validUntil;
    private List<SectionDTO> sectionDTOs;     // List of SectionDTOs
    private List<ModuleTypeDTO> moduleTypeDTOs; // List of ModuleTypeDTOs
    private List<ExamTypeDTO> examTypeDTOs;     // List of ExamTypeDTOs
    private List<ModuleRequirementDTO> moduleRequirementDTOs; // List of ModuleRequirementDTOs
    private List<UserDTOFlat> responsibleUsers; // List of UserDTOFlats
    private DegreeDTO degree;                // Full DegreeDTO instead of an ID
    private String moduleManualIntroduction;
    private String studyPlanAppendix;
}
