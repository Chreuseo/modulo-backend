package de.modulo.backend.converters;

import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.entities.ExamTypeEntity;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExamTypeConverter {

    @Autowired
    private SpoRepository spoRepository;

    public ExamTypeDTO toDto(ExamTypeEntity entity) {
        if (entity == null) {
            return null;
        }

        ExamTypeDTO dto = new ExamTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAbbreviation(entity.getAbbreviation());
        dto.setSpoId(entity.getSpo().getId());
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
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setSpo(spoRepository.findById(dto.getSpoId()).orElse(null));
        entity.setLength(dto.getLength());
        entity.setMandatory(dto.isMandatory());
        return entity;
    }
}
