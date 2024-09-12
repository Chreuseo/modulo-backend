package de.modulo.backend.converters;

import de.modulo.backend.dtos.ModuleRequirementDTO;

import de.modulo.backend.entities.ModuleRequirementEntity;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModuleRequirementConverter {

    @Autowired
    private SpoRepository spoRepository;

    public ModuleRequirementDTO toDto(ModuleRequirementEntity entity) {
        if (entity == null) {
            return null;
        }

        ModuleRequirementDTO dto = new ModuleRequirementDTO();
        dto.setId(entity.getId());
        dto.setSpoId(entity.getSpo().getId()); // Convert SpoEntity to SpoDTOFlat if needed
        dto.setName(entity.getName());
        return dto;
    }

    public ModuleRequirementEntity toEntity(ModuleRequirementDTO dto) {
        if (dto == null) {
            return null;
        }

        ModuleRequirementEntity entity = new ModuleRequirementEntity();
        entity.setId(dto.getId());
        entity.setSpo(spoRepository.findById(dto.getSpoId()).orElseThrow()); // Convert SpoDTOFlat to SpoEntity if needed
        entity.setName(dto.getName());
        return entity;
    }
}

