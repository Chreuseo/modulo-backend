package de.modulo.backend.converters;

import de.modulo.backend.dtos.ModuleTypeDTO;
import de.modulo.backend.entities.ModuleTypeEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.SpoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ModuleTypeConverterTest {

    @Mock
    SpoRepository spoRepository;

    @InjectMocks
    private ModuleTypeConverter moduleTypeConverter;

    @Test
    void testToDto() {
        // Arrange
        ModuleTypeEntity moduleTypeEntity = new ModuleTypeEntity();
        moduleTypeEntity.setId(1L);
        moduleTypeEntity.setName("Pflichtmodul");

        SpoEntity mockSpoEntity = new SpoEntity();
        mockSpoEntity.setId(2L);
        mockSpoEntity.setName("SPO 2020");
        moduleTypeEntity.setSpo(mockSpoEntity);

        // Act
        ModuleTypeDTO moduleTypeDto = moduleTypeConverter.toDto(moduleTypeEntity);

        // Assert
        assertNotNull(moduleTypeDto);
        assertEquals(moduleTypeEntity.getId(), moduleTypeDto.getId());
        assertEquals(moduleTypeEntity.getName(), moduleTypeDto.getName());
        assertEquals(moduleTypeEntity.getSpo().getId(), moduleTypeDto.getSpoId());
    }

    @Test
    void testToDto_NullSpo() {
        // Arrange
        ModuleTypeEntity moduleTypeEntity = new ModuleTypeEntity();
        moduleTypeEntity.setId(1L);
        moduleTypeEntity.setName("Pflichtmodul");

        // Act
        ModuleTypeDTO moduleTypeDto = moduleTypeConverter.toDto(moduleTypeEntity);

        // Assert
        assertNotNull(moduleTypeDto);
        assertEquals(moduleTypeEntity.getId(), moduleTypeDto.getId());
        assertEquals(moduleTypeEntity.getName(), moduleTypeDto.getName());
        assertNull(moduleTypeDto.getSpoId());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        ModuleTypeDTO moduleTypeDto = moduleTypeConverter.toDto(null);

        // Assert
        assertNull(moduleTypeDto);
    }

    @Test
    void testToEntity() {
        // Arrange
        ModuleTypeDTO moduleTypeDto = new ModuleTypeDTO();
        moduleTypeDto.setId(1L);
        moduleTypeDto.setName("Pflichtmodul");
        moduleTypeDto.setSpoId(2L);

        SpoEntity mockSpoEntity = new SpoEntity();
        mockSpoEntity.setId(2L);
        mockSpoEntity.setName("SPO 2020");
        moduleTypeDto.setSpoId(mockSpoEntity.getId());

        when(spoRepository.findById(mockSpoEntity.getId())).thenReturn(java.util.Optional.of(mockSpoEntity));

        // Act
        ModuleTypeEntity moduleTypeEntity = moduleTypeConverter.toEntity(moduleTypeDto);

        // Assert
        assertNotNull(moduleTypeEntity);
        assertEquals(moduleTypeDto.getId(), moduleTypeEntity.getId());
        assertEquals(moduleTypeDto.getName(), moduleTypeEntity.getName());
        assertEquals(moduleTypeDto.getSpoId(), moduleTypeEntity.getSpo().getId());
    }

    @Test
    void testToEntity_NullSpo() {
        // Arrange
        ModuleTypeDTO moduleTypeDto = new ModuleTypeDTO();
        moduleTypeDto.setId(1L);
        moduleTypeDto.setName("Pflichtmodul");

        // Act
        ModuleTypeEntity moduleTypeEntity = moduleTypeConverter.toEntity(moduleTypeDto);

        // Assert
        assertNotNull(moduleTypeEntity);
        assertEquals(moduleTypeDto.getId(), moduleTypeEntity.getId());
        assertEquals(moduleTypeDto.getName(), moduleTypeEntity.getName());
        assertNull(moduleTypeEntity.getSpo());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        ModuleTypeEntity moduleTypeEntity = moduleTypeConverter.toEntity(null);

        // Assert
        assertNull(moduleTypeEntity);
    }
}
