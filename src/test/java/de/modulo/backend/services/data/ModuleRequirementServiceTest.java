package de.modulo.backend.services.data;

import de.modulo.backend.converters.ModuleRequirementConverter;
import de.modulo.backend.dtos.ModuleRequirementDTO;
import de.modulo.backend.entities.ModuleRequirementEntity;
import de.modulo.backend.repositories.ModuleRequirementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ModuleRequirementServiceTest {

    @InjectMocks
    private ModuleRequirementService moduleRequirementService;

    @Mock
    private ModuleRequirementRepository moduleRequirementRepository;

    @Mock
    private ModuleRequirementConverter converter;

    private ModuleRequirementDTO moduleRequirementDTO;
    private ModuleRequirementEntity moduleRequirementEntity;

    @BeforeEach
    public void setUp() {
        moduleRequirementDTO = new ModuleRequirementDTO();
        moduleRequirementDTO.setId(1L);
        moduleRequirementDTO.setName("Test Requirement");
        moduleRequirementDTO.setSpoId(1L);

        moduleRequirementEntity = new ModuleRequirementEntity();
        moduleRequirementEntity.setId(1L);
        moduleRequirementEntity.setName("Test Requirement");
        // Assume SpoEntity is set here as needed
    }

    @Test
    public void testAddModuleRequirement() {
        when(converter.toEntity(any())).thenReturn(moduleRequirementEntity);
        when(moduleRequirementRepository.save(any())).thenReturn(moduleRequirementEntity);
        when(converter.toDto(any())).thenReturn(moduleRequirementDTO);

        ModuleRequirementDTO result = moduleRequirementService.addModuleRequirement(moduleRequirementDTO);

        assertNotNull(result);
        assertEquals(moduleRequirementDTO.getId(), result.getId());
        assertEquals(moduleRequirementDTO.getName(), result.getName());
        verify(moduleRequirementRepository, times(1)).save(any());
    }

    @Test
    public void testDelete() {
        doNothing().when(moduleRequirementRepository).deleteById(1L);

        moduleRequirementService.delete(1L);

        verify(moduleRequirementRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdate() {
        when(moduleRequirementRepository.findById(moduleRequirementDTO.getId())).thenReturn(Optional.of(moduleRequirementEntity));
        when(converter.toEntity(moduleRequirementDTO)).thenReturn(moduleRequirementEntity);
        when(moduleRequirementRepository.save(any())).thenReturn(moduleRequirementEntity);
        when(converter.toDto(any())).thenReturn(moduleRequirementDTO);

        ModuleRequirementDTO result = moduleRequirementService.update(moduleRequirementDTO);

        assertNotNull(result);
        assertEquals(moduleRequirementDTO.getId(), result.getId());
        assertEquals(moduleRequirementDTO.getName(), result.getName());
        verify(moduleRequirementRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateNotFound() {
        when(moduleRequirementRepository.findById(moduleRequirementDTO.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            moduleRequirementService.update(moduleRequirementDTO);
        });

        assertEquals("ModuleRequirement not found", exception.getMessage());
    }

    @Test
    public void testGetBySpoId() {
        List<ModuleRequirementEntity> entities = List.of(moduleRequirementEntity);
        when(moduleRequirementRepository.findBySpoId(1L)).thenReturn(entities);
        when(converter.toDto(any())).thenReturn(moduleRequirementDTO);

        List<ModuleRequirementDTO> result = moduleRequirementService.getBySpoId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(moduleRequirementDTO.getId(), result.get(0).getId());
        verify(moduleRequirementRepository, times(1)).findBySpoId(1L);
    }
}
