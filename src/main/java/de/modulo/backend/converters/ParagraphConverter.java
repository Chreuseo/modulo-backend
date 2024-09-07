package de.modulo.backend.converters;

import de.modulo.backend.dtos.ParagraphDTO;
import de.modulo.backend.entities.ParagraphEntity;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParagraphConverter {

    @Autowired
    private SpoRepository spoRepository;

    public ParagraphDTO toDto(ParagraphEntity entity) {
        if (entity == null) {
            return null;
        }

        ParagraphDTO dto = new ParagraphDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setText(entity.getText());
        dto.setSpoId(entity.getSpo().getId());

        return dto;
    }

    public ParagraphEntity toEntity(ParagraphDTO dto) {
        if (dto == null) {
            return null;
        }

        ParagraphEntity entity = new ParagraphEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setText(dto.getText());
        entity.setSpo(spoRepository.findById(dto.getSpoId()).orElse(null));

        return entity;
    }
}
