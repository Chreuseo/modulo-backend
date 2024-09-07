package de.modulo.backend.converters;

import de.modulo.backend.dtos.CourseTypeDTO;
import de.modulo.backend.entities.CourseTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class CourseTypeConverter {


    // Convert Entity to DTO
    public CourseTypeDTO toDto(CourseTypeEntity entity) {
        if (entity == null) {
            return null;
        }
        CourseTypeDTO dto = new CourseTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAbbreviation(entity.getAbbreviation());
        return dto;
    }

    // Convert DTO to Entity
    public CourseTypeEntity toEntity(CourseTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        CourseTypeEntity entity = new CourseTypeEntity();
        entity.setId(dto.getId()); // Be cautious when setting ID for new entities
        entity.setName(dto.getName());
        entity.setAbbreviation(dto.getAbbreviation());
        return entity;
    }
}
