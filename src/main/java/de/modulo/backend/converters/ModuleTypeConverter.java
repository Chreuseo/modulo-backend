package de.modulo.backend.converters;

import de.modulo.backend.dtos.ModuleTypeDTO;
import de.modulo.backend.entities.ModuleTypeEntity;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModuleTypeConverter {

    @Autowired
    private SpoRepository spoRepository;

    public ModuleTypeDTO toDto(ModuleTypeEntity entity) {
        if (entity == null) {
            return null;
        }

        ModuleTypeDTO dto = new ModuleTypeDTO();
        dto.setId(entity.getId());
        if (entity.getSpo() != null) {
            dto.setSpoId(entity.getSpo().getId());
        }
        dto.setName(entity.getName());
        dto.setIndex(entity.getOrderNumber());

        return dto;
    }

    public ModuleTypeEntity toEntity(ModuleTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        ModuleTypeEntity entity = new ModuleTypeEntity();
        entity.setId(dto.getId());
        entity.setSpo(spoRepository.findById(dto.getSpoId()).orElse(null));
        entity.setName(dto.getName());
        entity.setOrderNumber(dto.getIndex());

        return entity;
    }
}
