package de.modulo.backend.converters;

import de.modulo.backend.dtos.ModuleRequirementDTO;
import de.modulo.backend.entities.ModuleRequirementEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.SpoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ModuleRequirementConverterTest {

    @Mock
    SpoRepository spoRepository;

    @InjectMocks
    private ModuleRequirementConverter moduleRequirementConverter;

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
    void testToDto_NullEntity() {
        // Act
        ModuleRequirementDTO moduleRequirementDto = moduleRequirementConverter.toDto(null);

        // Assert
        assertNull(moduleRequirementDto);
    }

    @Test
    void testToEntity() {
        // Arrange
        ModuleRequirementDTO moduleRequirementDto = new ModuleRequirementDTO();
        moduleRequirementDto.setId(1L);
        moduleRequirementDto.setName("Vorrückungsberechtigt.");
        moduleRequirementDto.setSpoId(2L);

        // Create a mock SpoEntity that we expect to receive from the repository
        SpoEntity mockSpoEntity = new SpoEntity();
        mockSpoEntity.setId(2L);
        mockSpoEntity.setName("SPO Example"); // Set any additional fields you deem necessary

        // Mock the behavior of spoRepository.findById for the specific SPO ID
        when(spoRepository.findById(2L)).thenReturn(java.util.Optional.of(mockSpoEntity));


        // Act
        ModuleRequirementEntity moduleRequirementEntity = moduleRequirementConverter.toEntity(moduleRequirementDto);

        // Assert
        assertNotNull(moduleRequirementEntity);
        assertEquals(moduleRequirementDto.getId(), moduleRequirementEntity.getId());
        assertEquals(moduleRequirementDto.getName(), moduleRequirementEntity.getName());
        assertEquals(moduleRequirementDto.getSpoId(), moduleRequirementEntity.getSpo().getId());
        assertEquals(mockSpoEntity.getName(), moduleRequirementEntity.getSpo().getName());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        ModuleRequirementEntity moduleRequirementEntity = moduleRequirementConverter.toEntity(null);

        // Assert
        assertNull(moduleRequirementEntity);
    }
}
