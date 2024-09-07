package de.modulo.backend.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.modulo.backend.converters.ModuleRequirementConverter;
import de.modulo.backend.converters.SpoConverter;
import de.modulo.backend.dtos.ModuleRequirementDTO;
import de.modulo.backend.dtos.SpoDTOFlat;
import de.modulo.backend.entities.ModuleRequirementEntity;
import de.modulo.backend.entities.SpoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ModuleRequirementConverterTest {

    @InjectMocks
    private ModuleRequirementConverter moduleRequirementConverter;

    @Mock
    private SpoConverter spoConverter;

    private ModuleRequirementEntity moduleRequirementEntity;
    private ModuleRequirementDTO moduleRequirementDTO;

    @BeforeEach
    public void setUp() {
        moduleRequirementEntity = new ModuleRequirementEntity();
        moduleRequirementEntity.setId(1L);
        moduleRequirementEntity.setName("Math Module");

        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(1L); // Assuming SpoEntity has an ID property
        moduleRequirementEntity.setSpo(spoEntity);

        moduleRequirementDTO = new ModuleRequirementDTO();
        moduleRequirementDTO.setId(1L);
        moduleRequirementDTO.setName("Math Module");

        SpoDTOFlat spoDTOFlat = new SpoDTOFlat();
        spoDTOFlat.setId(1L); // Assuming SpoDTOFlat has an ID property
        moduleRequirementDTO.setSpo(spoDTOFlat);
    }

    @Test
    public void testToDto_NullEntity_ReturnsNull() {
        assertNull(moduleRequirementConverter.toDto(null));
    }

    @Test
    public void testToDto_ValidEntity_ReturnsDto() {
        when(spoConverter.toDtoFlat(any(SpoEntity.class))).thenReturn(moduleRequirementDTO.getSpo());

        ModuleRequirementDTO result = moduleRequirementConverter.toDto(moduleRequirementEntity);

        assertNotNull(result);
        assertEquals(moduleRequirementEntity.getId(), result.getId());
        assertEquals(moduleRequirementEntity.getName(), result.getName());
        assertNotNull(result.getSpo());
        assertEquals(moduleRequirementEntity.getSpo().getId(), result.getSpo().getId());
    }

    @Test
    public void testToEntity_NullDto_ReturnsNull() {
        assertNull(moduleRequirementConverter.toEntity(null));
    }

    @Test
    public void testToEntity_ValidDto_ReturnsEntity() {
        when(spoConverter.toEntity(any(SpoDTOFlat.class))).thenReturn(moduleRequirementEntity.getSpo());

        ModuleRequirementEntity result = moduleRequirementConverter.toEntity(moduleRequirementDTO);

        assertNotNull(result);
        assertEquals(moduleRequirementDTO.getId(), result.getId());
        assertEquals(moduleRequirementDTO.getName(), result.getName());
        assertNotNull(result.getSpo());
        assertEquals(moduleRequirementDTO.getSpo().getId(), result.getSpo().getId());
    }
}
