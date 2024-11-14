package de.modulo.backend.converters;

import de.modulo.backend.dtos.DegreeDTO;
import de.modulo.backend.entities.DegreeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DegreeConverterTest {

    private DegreeConverter degreeConverter;

    @BeforeEach
    void setUp() {
        degreeConverter = new DegreeConverter();
    }

    @Test
    void testToDto() {
        // Arrange
        DegreeEntity degreeEntity = new DegreeEntity();
        degreeEntity.setId(1L);
        degreeEntity.setName("Bachelor of Science");

        // Act
        DegreeDTO degreeDto = degreeConverter.toDto(degreeEntity);

        // Assert
        assertNotNull(degreeDto);
        assertEquals(degreeEntity.getId(), degreeDto.getId());
        assertEquals(degreeEntity.getName(), degreeDto.getName());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        DegreeDTO degreeDto = degreeConverter.toDto(null);

        // Assert
        assertNull(degreeDto);
    }

    @Test
    void testToEntity() {
        // Arrange
        DegreeDTO degreeDto = new DegreeDTO();
        degreeDto.setId(1L);
        degreeDto.setName("Master of Arts");

        // Act
        DegreeEntity degreeEntity = degreeConverter.toEntity(degreeDto);

        // Assert
        assertNotNull(degreeEntity);
        assertEquals(degreeDto.getId(), degreeEntity.getId());
        assertEquals(degreeDto.getName(), degreeEntity.getName());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        DegreeEntity degreeEntity = degreeConverter.toEntity(null);

        // Assert
        assertNull(degreeEntity);
    }
}
