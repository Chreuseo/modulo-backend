package de.modulo.backend.converters;

import de.modulo.backend.dtos.DurationDTO;
import de.modulo.backend.entities.DurationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DurationConverterTest {

    private DurationConverter durationConverter;

    @BeforeEach
    void setUp() {
        durationConverter = new DurationConverter();
    }

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
        assertNotNull(durationDto);
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
}
