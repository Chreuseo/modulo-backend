package de.modulo.backend.converters;

import de.modulo.backend.dtos.CourseTypeDTO;
import de.modulo.backend.entities.CourseTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTypeConverterTest {

    private CourseTypeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CourseTypeConverter();
    }

    @Test
    void testToDto() {
        // Arrange
        CourseTypeEntity entity = new CourseTypeEntity();
        entity.setId(1L);
        entity.setName("Mathematics");
        entity.setAbbreviation("MATH");

        // Act
        CourseTypeDTO dto = converter.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getAbbreviation(), dto.getAbbreviation());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        CourseTypeDTO dto = converter.toDto(null);

        // Assert
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        // Arrange
        CourseTypeDTO dto = new CourseTypeDTO();
        dto.setId(1L);
        dto.setName("Science");
        dto.setAbbreviation("SCI");
        dto.setEnabled(true);

        // Act
        CourseTypeEntity entity = converter.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getAbbreviation(), entity.getAbbreviation());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        CourseTypeEntity entity = converter.toEntity(null);

        // Assert
        assertNull(entity);
    }
}
