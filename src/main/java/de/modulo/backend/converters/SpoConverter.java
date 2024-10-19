package de.modulo.backend.converters;

import de.modulo.backend.dtos.*;
import de.modulo.backend.entities.SpoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpoConverter {

    private final DegreeConverter degreeConverter;


    @Autowired
    public SpoConverter(DegreeConverter degreeConverter) {
        this.degreeConverter = degreeConverter;
    }

    public SpoDTOFlat toDtoFlat(SpoEntity entity) {
        if (entity == null) {
            return null;
        }

        SpoDTOFlat dto = new SpoDTOFlat();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDegree(degreeConverter.toDto(entity.getDegree()));
        dto.setPublication(entity.getPublication());
        dto.setValidFrom(entity.getValidFrom());
        dto.setValidUntil(entity.getValidUntil());
        return dto;
    }

    public SpoDTO toDto(SpoEntity entity) {
        if (entity == null) {
            return null;
        }

        SpoDTO dto = new SpoDTO();
        dto.setId(entity.getId());
        dto.setHeader(entity.getHeader());
        dto.setFooter(entity.getFooter());
        dto.setName(entity.getName());
        dto.setPublication(entity.getPublication());
        dto.setValidFrom(entity.getValidFrom());
        dto.setValidUntil(entity.getValidUntil());
        dto.setModuleManualIntroduction(entity.getModuleManualIntroduction());
        dto.setStudyPlanAppendix(entity.getStudyPlanAppendix());

        dto.setDegree(degreeConverter.toDto(entity.getDegree()));

        return dto;
    }

    public SpoEntity toEntity(SpoDTO dto) {
        if (dto == null) {
            return null;
        }

        SpoEntity entity = new SpoEntity();
        entity.setId(dto.getId());
        entity.setHeader(dto.getHeader());
        entity.setFooter(dto.getFooter());
        entity.setName(dto.getName());
        entity.setPublication(dto.getPublication());
        entity.setValidFrom(dto.getValidFrom());
        entity.setValidUntil(dto.getValidUntil());
        entity.setModuleManualIntroduction(dto.getModuleManualIntroduction());
        entity.setStudyPlanAppendix(dto.getStudyPlanAppendix());

        entity.setDegree(degreeConverter.toEntity(dto.getDegree()));

        return entity;
    }

    public SpoEntity toEntity(SpoDTOFlat dto) {
        if (dto == null) {
            return null;
        }

        SpoEntity entity = new SpoEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDegree(degreeConverter.toEntity(dto.getDegree()));
        entity.setPublication(dto.getPublication());
        entity.setValidFrom(dto.getValidFrom());
        entity.setValidUntil(dto.getValidUntil());
        return entity;
    }
}
