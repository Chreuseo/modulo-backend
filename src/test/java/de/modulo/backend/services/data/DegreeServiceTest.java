package de.modulo.backend.services.data;

import de.modulo.backend.converters.DegreeConverter;
import de.modulo.backend.dtos.DegreeDTO;
import de.modulo.backend.entities.DegreeEntity;
import de.modulo.backend.repositories.DegreeRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DegreeServiceTest {

    @InjectMocks
    private DegreeService degreeService;

    @Mock
    private DegreeRepository degreeRepository;

    @Mock
    private DegreeConverter degreeConverter;

    private DegreeDTO degreeDTO;
    private DegreeEntity degreeEntity;

    @BeforeEach
    public void setUp() {
        degreeDTO = new DegreeDTO();
        degreeDTO.setId(1L);
        degreeDTO.setName("Bachelor of Science");

        degreeEntity = new DegreeEntity();
        degreeEntity.setId(1L);
        degreeEntity.setName("Bachelor of Science");
    }

    @Test
    public void testGetAll() {
        when(degreeRepository.findAll()).thenReturn(Collections.singletonList(degreeEntity));
        when(degreeConverter.toDto(degreeEntity)).thenReturn(degreeDTO);

        List<DegreeDTO> result = degreeService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bachelor of Science", result.get(0).getName());

        verify(degreeRepository, times(1)).findAll();
        verify(degreeConverter, times(1)).toDto(degreeEntity);
    }

    @Test
    public void testAddDegreeSuccessfully() {
        when(degreeRepository.findByName(degreeDTO.getName())).thenReturn(Optional.empty());
        when(degreeConverter.toEntity(degreeDTO)).thenReturn(degreeEntity);
        when(degreeRepository.save(degreeEntity)).thenReturn(degreeEntity);
        when(degreeConverter.toDto(degreeEntity)).thenReturn(degreeDTO);

        DegreeDTO savedDegree = degreeService.add(degreeDTO);

        assertNotNull(savedDegree);
        assertEquals("Bachelor of Science", savedDegree.getName());

        verify(degreeRepository, times(1)).findByName(degreeDTO.getName());
        verify(degreeRepository, times(1)).save(degreeEntity);
        verify(degreeConverter, times(1)).toEntity(degreeDTO);
        verify(degreeConverter, times(1)).toDto(degreeEntity);
    }

    @Test
    public void testAddDegreeAlreadyExists() {
        when(degreeRepository.findByName(degreeDTO.getName())).thenReturn(Optional.of(degreeEntity));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            degreeService.add(degreeDTO);
        });

        assertEquals("Degree already exists with name: Bachelor of Science", exception.getMessage());

        verify(degreeRepository, times(1)).findByName(degreeDTO.getName());
        verify(degreeRepository, never()).save(any(DegreeEntity.class));
        verify(degreeConverter, never()).toEntity(any(DegreeDTO.class));
        verify(degreeConverter, never()).toDto(any(DegreeEntity.class));
    }

    @Test
    public void testDeleteDegreeSuccessfully() {
        when(degreeRepository.existsById(1L)).thenReturn(true);

        degreeService.delete(1L);

        verify(degreeRepository, times(1)).existsById(1L);
        verify(degreeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteDegreeNotFound() {
        when(degreeRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            degreeService.delete(1L);
        });

        assertEquals("Degree not found with id: 1", exception.getMessage());

        verify(degreeRepository, times(1)).existsById(1L);
        verify(degreeRepository, never()).deleteById(1L);
    }
}
