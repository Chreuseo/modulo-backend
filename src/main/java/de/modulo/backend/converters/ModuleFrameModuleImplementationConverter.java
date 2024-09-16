package de.modulo.backend.converters;

import de.modulo.backend.dtos.ModuleFrameModuleImplementationDTO;
import de.modulo.backend.entities.ModuleFrameModuleImplementationEntity;
import de.modulo.backend.repositories.ModuleFrameRepository;
import de.modulo.backend.repositories.ModuleImplementationRepository;
import de.modulo.backend.repositories.ModuleRequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ModuleFrameModuleImplementationConverter {

    private final ModuleImplementationConverter moduleImplementationConverter;
    private final ModuleFrameConverter moduleFrameConverter;
    private final ModuleRequirementConverter moduleRequirementConverter;

    private final ModuleFrameRepository moduleFrameRepository;
    private final ModuleImplementationRepository moduleImplementationRepository;
    private final ModuleRequirementRepository moduleRequirementRepository;

    @Autowired
    public ModuleFrameModuleImplementationConverter(ModuleImplementationConverter moduleImplementationConverter,
                                                    ModuleFrameConverter moduleFrameConverter,
                                                    ModuleRequirementConverter moduleRequirementConverter,
                                                    ModuleFrameRepository moduleFrameRepository,
                                                    ModuleImplementationRepository moduleImplementationRepository,
                                                    ModuleRequirementRepository moduleRequirementRepository) {
        this.moduleImplementationConverter = moduleImplementationConverter;
        this.moduleFrameConverter = moduleFrameConverter;
        this.moduleRequirementConverter = moduleRequirementConverter;

        this.moduleFrameRepository = moduleFrameRepository;
        this.moduleImplementationRepository = moduleImplementationRepository;
        this.moduleRequirementRepository = moduleRequirementRepository;
    }

    public ModuleFrameModuleImplementationDTO toDto(ModuleFrameModuleImplementationEntity entity) {
        if (entity == null) {
            return null;
        }

        ModuleFrameModuleImplementationDTO dto = new ModuleFrameModuleImplementationDTO();
        dto.setId(entity.getId());
        dto.setModuleImplementationDTOFlat(moduleImplementationConverter.toDtoFlat(entity.getModuleImplementation()));
        dto.setModuleFrameDTO(moduleFrameConverter.toDto(entity.getModuleFrame()));
        dto.setExamTypeDTOs(new ArrayList<>()); // You need to implement logic to fetch exam types if required
        dto.setModuleRequirementDTO(moduleRequirementConverter.toDto(entity.getModuleRequirement()));

        return dto;
    }

    public ModuleFrameModuleImplementationEntity toEntity(ModuleFrameModuleImplementationDTO dto) {
        if (dto == null) {
            return null;
        }

        ModuleFrameModuleImplementationEntity entity = new ModuleFrameModuleImplementationEntity();
        entity.setId(dto.getId());
        entity.setModuleImplementation(moduleImplementationRepository.findById(dto.getModuleImplementationDTOFlat().getId()).orElse(null));
        entity.setModuleFrame(moduleFrameRepository.findById(dto.getModuleFrameDTO().getId()).orElse(null));
        if(dto.getModuleRequirementDTO() != null) {
            entity.setModuleRequirement(moduleRequirementRepository.findById(dto.getModuleRequirementDTO().getId()).orElse(null));
        }

        return entity;
    }
}
