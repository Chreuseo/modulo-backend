package de.modulo.backend.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.modulo.backend.converters.SectionConverter;
import de.modulo.backend.dtos.SectionDTO;
import de.modulo.backend.entities.SectionEntity;
import de.modulo.backend.entities.SpoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class SectionConverterTest {

    private SectionConverter sectionConverter;

    @BeforeEach
    public void setUp() {
        sectionConverter = new SectionConverter();
    }

    @Test
    public void testToDtoWithNullEntity() {
        SectionDTO result = sectionConverter.toDto(null);
        assertNull(result, "toDto should return null when input entity is null");
    }

    @Test
    public void testToDtoWithEntity() {
        // Given
        Long id = 1L;
        String name = "Section Title";

        // Setting up SPoEntity
        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(10L); // Example SPO Id

        // Setting up Next Section
        SectionEntity nextSection = new SectionEntity();
        nextSection.setId(2L); // Example Next Section Id

        // Creating the SectionEntity
        SectionEntity entity = new SectionEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setSpo(spoEntity);
        entity.setNextSection(nextSection);

        // When
        SectionDTO result = sectionConverter.toDto(entity);

        // Then
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(spoEntity.getId(), result.getSpoId());
    }

    @Test
    public void testToEntityWithNullDto() {
        SectionEntity result = sectionConverter.toEntity(null);
        assertNull(result, "toEntity should return null when input DTO is null");
    }

    @Test
    public void testToEntity() {
        // Given
        Long id = 1L;
        String name = "Section Title";

        SectionDTO dto = new SectionDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setSpoId(10L); // Assuming we could have a method to convert SpoEntity

        // When
        SectionEntity result = sectionConverter.toEntity(dto);

        // Then
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        // We cannot directly assert `spo` and `nextSection` here,
        // as they are not initialized in the converter.
        assertNull(result.getSpo(), "spo should be null in the entity");
        assertNull(result.getNextSection(), "nextSection should be null in the entity");
    }
}
