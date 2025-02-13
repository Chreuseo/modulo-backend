package de.modulo.backend.services.data;

import de.modulo.backend.converters.CycleConverter;
import de.modulo.backend.dtos.CycleDTO;
import de.modulo.backend.entities.CycleEntity;
import de.modulo.backend.repositories.CycleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CycleServiceTest {

    @InjectMocks
    private CycleService cycleService;

    @Mock
    private CycleRepository cycleRepository;

    @Mock
    private CycleConverter cycleConverter;

    private CycleDTO cycleDTO;
    private CycleEntity cycleEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cycleDTO = new CycleDTO();
        cycleDTO.setId(1L);
        cycleDTO.setName("Test Cycle");

        cycleEntity = new CycleEntity();
        cycleEntity.setId(1L);
        cycleEntity.setName("Test Cycle");
    }

    @Test
    void testAddCycle() {
        when(cycleConverter.toEntity(any(CycleDTO.class))).thenReturn(cycleEntity);
        when(cycleRepository.save(any(CycleEntity.class))).thenReturn(cycleEntity);
        when(cycleConverter.toDto(any(CycleEntity.class))).thenReturn(cycleDTO);

        CycleDTO result = cycleService.addCycle(cycleDTO);

        assertNotNull(result);
        assertEquals(cycleDTO.getId(), result.getId());
        verify(cycleRepository).save(cycleEntity);
        verify(cycleConverter).toEntity(cycleDTO);
        verify(cycleConverter).toDto(cycleEntity);
    }

    @Test
    void testUpdateCycle_Success() {
        when(cycleRepository.existsById(cycleDTO.getId())).thenReturn(true);
        when(cycleConverter.toEntity(any(CycleDTO.class))).thenReturn(cycleEntity);
        when(cycleRepository.save(any(CycleEntity.class))).thenReturn(cycleEntity);
        when(cycleConverter.toDto(any(CycleEntity.class))).thenReturn(cycleDTO);

        CycleDTO result = cycleService.updateCycle(cycleDTO);

        assertNotNull(result);
        assertEquals(cycleDTO.getId(), result.getId());
        verify(cycleRepository).save(cycleEntity);
    }

    @Test
    void testUpdateCycle_NotFound() {
        when(cycleRepository.existsById(cycleDTO.getId())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cycleService.updateCycle(cycleDTO);
        });

        assertEquals("Cycle not found with id " + cycleDTO.getId(), exception.getMessage());
        verify(cycleRepository, never()).save(any());
    }

    @Test
    void testRemoveCycle_Success() {
        when(cycleRepository.existsById(cycleDTO.getId())).thenReturn(true);

        cycleService.removeCycle(cycleDTO.getId());

        verify(cycleRepository).deleteById(cycleDTO.getId());
    }

    @Test
    void testRemoveCycle_NotFound() {
        when(cycleRepository.existsById(cycleDTO.getId())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cycleService.removeCycle(cycleDTO.getId());
        });

        assertEquals("Cycle not found with id " + cycleDTO.getId(), exception.getMessage());
        verify(cycleRepository, never()).deleteById(any());
    }

    @Test
    void testGetAllCycles() {
        List<CycleEntity> cycleEntities = new ArrayList<>();
        cycleEntities.add(cycleEntity);

        when(cycleRepository.findAll()).thenReturn(cycleEntities);
        when(cycleConverter.toDto(any(CycleEntity.class))).thenReturn(cycleDTO);

        List<CycleDTO> result = cycleService.getAllCycles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cycleDTO.getId(), result.get(0).getId());
        verify(cycleRepository).findAll();
    }
}
