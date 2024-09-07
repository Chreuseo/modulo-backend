package de.modulo.backend.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.modulo.backend.converters.ModuleTypeConverter;
import de.modulo.backend.dtos.ModuleTypeDTO;
import de.modulo.backend.entities.ModuleTypeEntity;
import de.modulo.backend.entities.SpoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ModuleTypeConverterTest {

    private ModuleTypeConverter moduleTypeConverter;

    @BeforeEach
    public void setUp() {
        moduleTypeConverter = new ModuleTypeConverter();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToDtoWithNullEntity() {
        ModuleTypeDTO result = moduleTypeConverter.toDto(null);
        assertNull(result, "toDto should return null when input entity is null");
    }

    @Test
    public void testToDtoWithEntity() {
        // Given
        long id = 1L;
        String name = "Module Type 1";

        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(10L); // Example SPO Id

        ModuleTypeEntity nextModuleTypeEntity = new ModuleTypeEntity();
        nextModuleTypeEntity.setId(2L); // Example Next Module Type Id

        ModuleTypeEntity entity = new ModuleTypeEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setSpo(spoEntity);
        entity.setNextModuleType(nextModuleTypeEntity);

        // When
        ModuleTypeDTO result = moduleTypeConverter.toDto(entity);

        // Then
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(spoEntity.getId(), result.getSpoId());
    }

    @Test
    public void testToEntityWithNullDto() {
        ModuleTypeEntity result = moduleTypeConverter.toEntity(null);
        assertNull(result, "toEntity should return null when input DTO is null");
    }

    @Test
    public void testToEntity() {
        // Given
        long id = 1L;
        String name = "Module Type 1";

        ModuleTypeDTO dto = new ModuleTypeDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setSpoId(10L); // Assuming we mock it.

        // When
        ModuleTypeEntity result = moduleTypeConverter.toEntity(dto);

        // Then
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        // Note: spo and nextModuleType are not directly set in the toEntity method,
        // so we can't assert them here unless we modify the entity further.
        assertNull(result.getSpo(), "spo should be null unless the entity is fully constructed");
        assertNull(result.getNextModuleType(), "nextModuleType should be null unless the entity is fully constructed");
    }
}
