package de.modulo.backend.converters;

import de.modulo.backend.dtos.DegreeDTO;
import de.modulo.backend.entities.DegreeEntity;
import org.springframework.stereotype.Component;

@Component
public class DegreeConverter {

    public DegreeDTO toDto(DegreeEntity entity) {
        if (entity == null) {
            return null;
        }

        DegreeDTO dto = new DegreeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public DegreeEntity toEntity(DegreeDTO dto) {
        if (dto == null) {
            return null;
        }

        DegreeEntity entity = new DegreeEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }
}
