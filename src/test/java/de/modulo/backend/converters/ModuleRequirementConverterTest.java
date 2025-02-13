package de.modulo.backend.converters;

import de.modulo.backend.dtos.ModuleRequirementDTO;
import de.modulo.backend.entities.ModuleRequirementEntity;
import de.modulo.backend.entities.SpoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ModuleRequirementConverterTest {

    private ModuleRequirementConverter moduleRequirementConverter;

    @BeforeEach
    void setUp() {
        moduleRequirementConverter = new ModuleRequirementConverter();
    }

    @Test
    void testToDto() {
        // Arrange
        ModuleRequirementEntity moduleRequirementEntity = new ModuleRequirementEntity();
        moduleRequirementEntity.setId(1L);
        moduleRequirementEntity.setName("Vorrückungsberechtigt.");

        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(2L);
        spoEntity.setName("SPO 2020");
        moduleRequirementEntity.setSpo(spoEntity);

        // Act
        ModuleRequirementDTO moduleRequirementDto = moduleRequirementConverter.toDto(moduleRequirementEntity);

        // Assert
        assertNotNull(moduleRequirementDto);
        assertEquals(moduleRequirementEntity.getId(), moduleRequirementDto.getId());
        assertEquals(moduleRequirementEntity.getName(), moduleRequirementDto.getName());
        assertEquals(moduleRequirementEntity.getSpo().getId(), moduleRequirementDto.getSpoId());
    }

    @Test
    void testToEntity() {
        // Arrange
        ModuleRequirementDTO moduleRequirementDto = new ModuleRequirementDTO();
        moduleRequirementDto.setId(1L);
        moduleRequirementDto.setName("Vorrückungsberechtigt.");

        // Act
        ModuleRequirementEntity moduleRequirementEntity = moduleRequirementConverter.toEntity(moduleRequirementDto);

        // Assert
        assertNotNull(moduleRequirementEntity);
        assertEquals(moduleRequirementDto.getId(), moduleRequirementEntity.getId());
        assertEquals(moduleRequirementDto.getName(), moduleRequirementEntity.getName());
    }
}
