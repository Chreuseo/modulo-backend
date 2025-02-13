package de.modulo.backend.converters;

import de.modulo.backend.dtos.DurationDTO;
import de.modulo.backend.entities.DurationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DurationConverterTest {

    @InjectMocks
    private DurationConverter durationConverter;

    @Test
    void testToDto() {
        // Arrange
        DurationEntity durationEntity = new DurationEntity();
        durationEntity.setId(1L);
        durationEntity.setName("1 Semester");

        // Act
        DurationDTO durationDto = durationConverter.toDto(durationEntity);

        // Assert
        assertNotNull(durationDto);
        assertEquals(durationEntity.getId(), durationDto.getId());
        assertEquals(durationEntity.getName(), durationDto.getName());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        DurationDTO durationDto = durationConverter.toDto(null);

        // Assert
        assertNull(durationDto);
    }


    @Test
    void testToEntity() {
        // Arrange
        DurationDTO durationDto = new DurationDTO();
        durationDto.setId(1L);
        durationDto.setName("2 Semester");

        // Act
        DurationEntity durationEntity = durationConverter.toEntity(durationDto);

        // Assert
        assertNotNull(durationEntity);
        assertEquals(durationDto.getId(), durationEntity.getId());
        assertEquals(durationDto.getName(), durationEntity.getName());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        DurationEntity durationEntity = durationConverter.toEntity(null);

        // Assert
        assertNull(durationEntity);
    }
}
