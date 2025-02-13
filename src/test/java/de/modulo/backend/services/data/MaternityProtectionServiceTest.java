package de.modulo.backend.services.data;

import de.modulo.backend.converters.MaternityProtectionConverter;
import de.modulo.backend.dtos.MaternityProtectionDTO;
import de.modulo.backend.entities.MaternityProtectionEntity;
import de.modulo.backend.repositories.MaternityProtectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MaternityProtectionServiceTest {

    @InjectMocks
    private MaternityProtectionService maternityProtectionService;

    @Mock
    private MaternityProtectionRepository maternityProtectionRepository;

    @Mock
    private MaternityProtectionConverter maternityProtectionConverter;

    private MaternityProtectionEntity maternityProtectionEntity;
    private MaternityProtectionDTO maternityProtectionDTO;

    @BeforeEach
    public void setUp() {
        maternityProtectionEntity = new MaternityProtectionEntity();
        maternityProtectionEntity.setId(1L);
        maternityProtectionEntity.setName("Standard Maternity Leave");

        maternityProtectionDTO = new MaternityProtectionDTO();
        maternityProtectionDTO.setId(1L);
        maternityProtectionDTO.setName("Standard Maternity Leave");
    }

    @Test
    public void testGetAll() {
        when(maternityProtectionRepository.findAll()).thenReturn(Collections.singletonList(maternityProtectionEntity));
        when(maternityProtectionConverter.toDto(any(MaternityProtectionEntity.class))).thenReturn(maternityProtectionDTO);

        var result = maternityProtectionService.getAll();

        assertEquals(1, result.size());
        assertEquals(maternityProtectionDTO.getName(), result.get(0).getName());
        verify(maternityProtectionRepository, times(1)).findAll();
        verify(maternityProtectionConverter, times(1)).toDto(any(MaternityProtectionEntity.class));
    }

    @Test
    public void testAddSuccess() {
        when(maternityProtectionRepository.findByName(maternityProtectionDTO.getName())).thenReturn(Optional.empty());
        when(maternityProtectionConverter.toEntity(any(MaternityProtectionDTO.class))).thenReturn(maternityProtectionEntity);
        when(maternityProtectionRepository.save(any(MaternityProtectionEntity.class))).thenReturn(maternityProtectionEntity);
        when(maternityProtectionConverter.toDto(any(MaternityProtectionEntity.class))).thenReturn(maternityProtectionDTO);

        MaternityProtectionDTO result = maternityProtectionService.add(maternityProtectionDTO);

        assertEquals(maternityProtectionDTO.getName(), result.getName());
        verify(maternityProtectionRepository, times(1)).findByName(maternityProtectionDTO.getName());
        verify(maternityProtectionRepository, times(1)).save(any(MaternityProtectionEntity.class));
    }

    @Test
    public void testAddAlreadyExists() {
        when(maternityProtectionRepository.findByName(maternityProtectionDTO.getName()))
                .thenReturn(Optional.of(maternityProtectionEntity));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> maternityProtectionService.add(maternityProtectionDTO));
        assertEquals("Maternity Protection already exists with name: " + maternityProtectionDTO.getName(), exception.getMessage());
        verify(maternityProtectionRepository, times(1)).findByName(maternityProtectionDTO.getName());
        verify(maternityProtectionRepository, never()).save(any(MaternityProtectionEntity.class));
    }

    @Test
    public void testDeleteSuccess() {
        when(maternityProtectionRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> maternityProtectionService.delete(1L));
        verify(maternityProtectionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteNotFound() {
        when(maternityProtectionRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> maternityProtectionService.delete(1L));
        assertEquals("Maternity Protection entry not found with id: 1", exception.getMessage());
        verify(maternityProtectionRepository, never()).deleteById(anyLong());
    }
}
