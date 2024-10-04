package de.modulo.backend.converters;

import de.modulo.backend.dtos.SemesterDTO;
import de.modulo.backend.entities.SemesterEntity;
import org.springframework.stereotype.Component;

@Component
public class SemesterConverter {

    public SemesterDTO toDTO(SemesterEntity entity) {
        SemesterDTO dto = new SemesterDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAbbreviation(entity.getAbbreviation());
        dto.setYear(entity.getYear());
        return dto;
    }

    public SemesterEntity toEntity(SemesterDTO dto) {
        SemesterEntity entity = new SemesterEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setYear(dto.getYear());
        return entity;
    }
}
