package de.modulo.backend.services.data;

import de.modulo.backend.converters.ModuleTypeConverter;
import de.modulo.backend.dtos.ModuleTypeDTO;
import de.modulo.backend.entities.ModuleTypeEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.ModuleTypeRepository;
import de.modulo.backend.repositories.SpoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleTypeServiceTest {

    @InjectMocks
    private ModuleTypeService moduleTypeService;

    @Mock
    private ModuleTypeRepository moduleTypeRepository;

    @Mock
    private ModuleTypeConverter moduleTypeConverter;

    @Mock
    private SpoRepository spoRepository;

    private ModuleTypeDTO moduleTypeDTO;
    private ModuleTypeEntity moduleTypeEntity;
    private Long exampleId = 1L;
    private SpoEntity spoEntity;

    @BeforeEach
    public void setUp() {
        spoEntity = new SpoEntity();
        spoEntity.setId(exampleId);
        moduleTypeDTO = new ModuleTypeDTO();
        moduleTypeDTO.setId(exampleId);
        moduleTypeDTO.setSpoId(exampleId);
        moduleTypeDTO.setName("Test Module");
        moduleTypeDTO.setIndex(0);

        moduleTypeEntity = new ModuleTypeEntity();
        moduleTypeEntity.setId(exampleId);
        moduleTypeEntity.setSpo(spoEntity);
        moduleTypeEntity.setName("Test Module");
        moduleTypeEntity.setOrderNumber(0);
    }

    @Test
    public void testAdd() {
        // Arrange
        when(moduleTypeConverter.toEntity(moduleTypeDTO)).thenReturn(moduleTypeEntity);
        when(moduleTypeRepository.save(moduleTypeEntity)).thenReturn(moduleTypeEntity);
        when(moduleTypeConverter.toDto(moduleTypeEntity)).thenReturn(moduleTypeDTO);

        // Act
        ModuleTypeDTO result = moduleTypeService.add(moduleTypeDTO);

        // Assert
        assertEquals(moduleTypeDTO.getId(), result.getId());
        verify(moduleTypeConverter).toEntity(moduleTypeDTO);
        verify(moduleTypeRepository).save(moduleTypeEntity);
        verify(moduleTypeConverter).toDto(moduleTypeEntity);
    }

    @Test
    public void testDelete_whenExists() {
        // Arrange
        when(moduleTypeRepository.existsById(exampleId)).thenReturn(true);

        // Act
        moduleTypeService.delete(exampleId);

        // Assert
        verify(moduleTypeRepository).deleteById(exampleId);
    }

    @Test
    public void testDelete_whenNotExists() {
        // Arrange
        when(moduleTypeRepository.existsById(exampleId)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            moduleTypeService.delete(exampleId);
        });

        assertEquals("ModuleType not found with id: " + exampleId, exception.getMessage());
        verify(moduleTypeRepository, never()).deleteById(exampleId);
    }

    @Test
    public void testGetBySpo() {
        // Arrange
        when(spoRepository.findById(exampleId)).thenReturn(Optional.of(spoEntity));
        when(moduleTypeRepository.findBySpoId(exampleId)).thenReturn(Collections.singletonList(moduleTypeEntity));
        when(moduleTypeConverter.toDto(moduleTypeEntity)).thenReturn(moduleTypeDTO);

        // Act
        List<ModuleTypeDTO> result = moduleTypeService.getBySpo(exampleId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(moduleTypeDTO.getId(), result.get(0).getId());
        verify(spoRepository).findById(exampleId);
        verify(moduleTypeRepository).findBySpoId(exampleId);
        verify(moduleTypeConverter).toDto(moduleTypeEntity);
    }

    @Test
    public void testGetBySpo_whenSpoNotFound() {
        // Arrange
        when(spoRepository.findById(exampleId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            moduleTypeService.getBySpo(exampleId);
        });

        assertEquals("Spo not found with id: " + exampleId, exception.getMessage());
        verify(moduleTypeRepository, never()).findBySpoId(exampleId);
    }
}
