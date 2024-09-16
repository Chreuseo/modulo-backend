package de.modulo.backend.dtos;

import lombok.Data;

import java.util.List;


@Data
public class ModuleFrameModuleImplementationDTO {
    private Long id;
    private ModuleImplementationDTOFlat moduleImplementationDTOFlat;
    private ModuleFrameDTO moduleFrameDTO;

    private List<ExamTypeDTO> examTypes;
    private ModuleRequirementDTO moduleRequirementDTO;
}
