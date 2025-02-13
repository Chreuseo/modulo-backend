package de.modulo.backend.converters;

import de.modulo.backend.dtos.CycleDTO;
import de.modulo.backend.entities.CycleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CycleConverterTest {

    @InjectMocks
    private CycleConverter cycleConverter;

    @Test
    void testToDto() {
        // Arrange
        CycleEntity cycleEntity = new CycleEntity();
        cycleEntity.setId(1L);
        cycleEntity.setName("Cycle Name");

        // Act
        CycleDTO cycleDto = cycleConverter.toDto(cycleEntity);

        // Assert
        assertNotNull(cycleDto);
        assertEquals(cycleEntity.getId(), cycleDto.getId());
        assertEquals(cycleEntity.getName(), cycleDto.getName());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        CycleDTO cycleDto = cycleConverter.toDto(null);

        // Assert
        assertNull(cycleDto);
    }

    @Test
    void testToEntity() {
        // Arrange
        CycleDTO cycleDto = new CycleDTO();
        cycleDto.setId(1L);
        cycleDto.setName("Cycle DTO Name");

        // Act
        CycleEntity cycleEntity = cycleConverter.toEntity(cycleDto);

        // Assert
        assertNotNull(cycleEntity);
        assertEquals(cycleDto.getId(), cycleEntity.getId());
        assertEquals(cycleDto.getName(), cycleEntity.getName());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        CycleEntity cycleEntity = cycleConverter.toEntity(null);

        // Assert
        assertNull(cycleEntity);
    }
}
