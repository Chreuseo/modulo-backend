package de.modulo.backend.converters;

import de.modulo.backend.dtos.SectionDTO;
import de.modulo.backend.entities.SectionEntity;
import de.modulo.backend.repositories.SectionRepository;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SectionConverter {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SpoRepository spoRepository;

    public SectionDTO toDto(SectionEntity entity) {
        if (entity == null) {
            return null;
        }

        SectionDTO dto = new SectionDTO();
        dto.setId(entity.getId());
        if (entity.getSpo() != null) {
            dto.setSpoId(entity.getSpo().getId());
        }
        dto.setName(entity.getName());
        dto.setIndex(entity.getOrderNumber());

        return dto;
    }

    public SectionEntity toEntity(SectionDTO dto) {
        if (dto == null) {
            return null;
        }

        SectionEntity entity = new SectionEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        if(dto.getSpoId() != null){
            entity.setSpo(spoRepository.findById(dto.getSpoId()).orElse(null));
        }

        entity.setName(dto.getName());
        entity.setOrderNumber(dto.getIndex());

        return entity;
    }
}
