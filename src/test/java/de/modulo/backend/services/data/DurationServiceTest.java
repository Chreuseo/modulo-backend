package de.modulo.backend.services.data;

import de.modulo.backend.converters.DurationConverter;
import de.modulo.backend.dtos.DurationDTO;
import de.modulo.backend.entities.DurationEntity;
import de.modulo.backend.repositories.DurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DurationServiceTest {

    @InjectMocks
    private DurationService durationService;

    @Mock
    private DurationRepository durationRepository;

    @Mock
    private DurationConverter durationConverter;

    @BeforeEach
    public void setUp() {
        // setup any necessary mocks or test data here if needed
    }

    @Test
    public void testGetAll() {
        DurationEntity entity1 = new DurationEntity();
        entity1.setId(1L);
        entity1.setName("Duration 1");

        DurationEntity entity2 = new DurationEntity();
        entity2.setId(2L);
        entity2.setName("Duration 2");

        List<DurationEntity> entities = List.of(entity1, entity2);

        // Mock behavior
        when(durationRepository.findAll()).thenReturn(entities);
        when(durationConverter.toDto(any(DurationEntity.class))).thenAnswer(invocation -> {
            DurationEntity arg = invocation.getArgument(0);
            DurationDTO dto = new DurationDTO();
            dto.setId(arg.getId());
            dto.setName(arg.getName());
            return dto;
        });

        // Call method
        List<DurationDTO> result = durationService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Duration 1", result.get(0).getName());
        assertEquals("Duration 2", result.get(1).getName());
        verify(durationRepository).findAll();
        verify(durationConverter, times(2)).toDto(any(DurationEntity.class));
    }

    @Test
    public void testAddNewDuration() {
        DurationDTO dto = new DurationDTO();
        dto.setName("New Duration");

        DurationEntity entity = new DurationEntity();
        entity.setId(1L);
        entity.setName(dto.getName());

        // Mock behavior
        when(durationRepository.findByName(dto.getName())).thenReturn(Optional.empty());
        when(durationConverter.toEntity(dto)).thenReturn(entity);
        when(durationRepository.save(entity)).thenReturn(entity);
        when(durationConverter.toDto(entity)).thenReturn(dto);

        // Call method
        DurationDTO result = durationService.add(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        verify(durationRepository).findByName(dto.getName());
        verify(durationRepository).save(entity);
        verify(durationConverter).toEntity(dto);
        verify(durationConverter).toDto(entity);
    }

    @Test
    public void testAddExistingDurationThrowsException() {
        DurationDTO dto = new DurationDTO();
        dto.setName("Existing Duration");

        DurationEntity existingEntity = new DurationEntity();
        existingEntity.setName(dto.getName());

        // Mock behavior
        when(durationRepository.findByName(dto.getName())).thenReturn(Optional.of(existingEntity));

        // Assert
        assertThrows(IllegalArgumentException.class, () -> durationService.add(dto));
        verify(durationRepository).findByName(dto.getName());
        verify(durationRepository, never()).save(any(DurationEntity.class));
    }

    @Test
    public void testDeleteExistingDuration() {
        Long id = 1L;

        // Mock behavior
        when(durationRepository.existsById(id)).thenReturn(true);

        // Call method
        durationService.delete(id);

        // Assert
        verify(durationRepository).existsById(id);
        verify(durationRepository).deleteById(id);
    }

    @Test
    public void testDeleteNonExistingDurationThrowsException() {
        Long id = 99L;

        // Mock behavior
        when(durationRepository.existsById(id)).thenReturn(false);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> durationService.delete(id));
        verify(durationRepository).existsById(id);
        verify(durationRepository, never()).deleteById(id);
    }
}
