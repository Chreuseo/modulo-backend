package de.modulo.backend.converters;

import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.entities.ExamTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExamTypeConverter {

    @Autowired
    private SpoConverter spoConverter;

    public ExamTypeDTO toDto(ExamTypeEntity entity) {
        if (entity == null) {
            return null;
        }

        ExamTypeDTO dto = new ExamTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLength(entity.getLength());
        dto.setMandatory(entity.isMandatory());
        return dto;
    }

    public ExamTypeEntity toEntity(ExamTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        ExamTypeEntity entity = new ExamTypeEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setLength(dto.getLength());
        entity.setMandatory(dto.isMandatory());
        return entity;
    }
}
