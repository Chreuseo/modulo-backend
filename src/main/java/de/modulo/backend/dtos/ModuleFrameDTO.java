package de.modulo.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ModuleFrameDTO {

    private long id;
    private long spoId;               // Full SPO DTO
    private SectionDTO section;       // Full Section DTO
    private ModuleTypeDTO moduleType; // Full Module Type DTO
    private int quantity;
    private String name;
    private int sws;                  // Semester Weekly Hours
    private int weight;
    private int credits;
    private boolean allExamsMandatory;

    private List<CourseTypeDTO> courseTypes; // List of CourseType DTOs
    private List<ExamTypeDTO> examTypes;     // List of ExamType DTOs
}
