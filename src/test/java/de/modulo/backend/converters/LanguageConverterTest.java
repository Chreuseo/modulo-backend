package de.modulo.backend.converters;

import de.modulo.backend.dtos.LanguageDTO;
import de.modulo.backend.entities.LanguageEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LanguageConverterTest {

    @InjectMocks
    private LanguageConverter languageConverter;

    @Test
    void testToDto() {
        // Arrange
        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setId(1L);
        languageEntity.setName("German");

        // Act
        LanguageDTO languageDto = languageConverter.toDto(languageEntity);

        // Assert
        assertNotNull(languageDto);
        assertEquals(languageEntity.getId(), languageDto.getId());
        assertEquals(languageEntity.getName(), languageDto.getName());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        LanguageDTO languageDto = languageConverter.toDto(null);

        // Assert
        assertNull(languageDto);
    }

    @Test
    void testToEntity() {
        // Arrange
        LanguageDTO languageDto = new LanguageDTO();
        languageDto.setId(1L);
        languageDto.setName("English");

        // Act
        LanguageEntity languageEntity = languageConverter.toEntity(languageDto);

        // Assert
        assertNotNull(languageEntity);
        assertEquals(languageDto.getId(), languageEntity.getId());
        assertEquals(languageDto.getName(), languageEntity.getName());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        LanguageEntity languageEntity = languageConverter.toEntity(null);

        // Assert
        assertNull(languageEntity);
    }
}
