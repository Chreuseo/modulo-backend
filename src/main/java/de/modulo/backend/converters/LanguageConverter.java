package de.modulo.backend.converters;

import de.modulo.backend.dtos.LanguageDTO;
import de.modulo.backend.entities.LanguageEntity;
import org.springframework.stereotype.Component;

@Component
public class LanguageConverter {

    public LanguageDTO toDto(LanguageEntity languageEntity) {
        if (languageEntity == null) {
            return null;
        }

        LanguageDTO languageDto = new LanguageDTO();
        languageDto.setId(languageEntity.getId());
        languageDto.setName(languageEntity.getName());

        return languageDto;
    }

    public LanguageEntity toEntity(LanguageDTO languageDto) {
        if (languageDto == null) {
            return null;
        }

        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setId(languageDto.getId());
        languageEntity.setName(languageDto.getName());

        return languageEntity;
    }
}
