package de.modulo.backend.dtos;

import lombok.Data;


@Data
public class ModuleMappingDTO {
    private long id;
    private ModuleImplementationDTOFlat moduleImplementationDTOFlat;
    private ModuleFrameDTO moduleFrameDTO;

    private ModuleRequirementDTO moduleRequirementDTO;
}
