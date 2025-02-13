package de.modulo.backend.services.data;

import de.modulo.backend.converters.SemesterConverter;
import de.modulo.backend.dtos.SemesterDTO;
import de.modulo.backend.entities.SemesterEntity;
import de.modulo.backend.repositories.SemesterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SemesterServiceTest {

    @InjectMocks
    private SemesterService semesterService;

    @Mock
    private SemesterRepository semesterRepository;

    @Mock
    private SemesterConverter semesterConverter;

    @Test
    public void testGetAllSemesters() {
        SemesterEntity entity1 = new SemesterEntity();
        entity1.setId(1L);
        entity1.setName("Fall 2021");
        entity1.setAbbreviation("F21");
        entity1.setYear("2021");

        SemesterEntity entity2 = new SemesterEntity();
        entity2.setId(2L);
        entity2.setName("Spring 2020");
        entity2.setAbbreviation("S20");
        entity2.setYear("2020");

        when(semesterRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        SemesterDTO dto1 = new SemesterDTO();
        dto1.setId(1L);
        dto1.setName("Fall 2021");
        dto1.setAbbreviation("F21");
        dto1.setYear("2021");

        SemesterDTO dto2 = new SemesterDTO();
        dto2.setId(2L);
        dto2.setName("Spring 2020");
        dto2.setAbbreviation("S20");
        dto2.setYear("2020");

        when(semesterConverter.toDTO(entity1)).thenReturn(dto1);
        when(semesterConverter.toDTO(entity2)).thenReturn(dto2);

        List<SemesterDTO> result = semesterService.getAllSemesters();

        assertEquals(2, result.size());
        assertEquals("Spring 2020", result.get(0).getName()); // Verify sorting
        assertEquals("Fall 2021", result.get(1).getName()); // Verify sorting
        verify(semesterRepository, times(1)).findAll();
        verify(semesterConverter, times(2)).toDTO(any(SemesterEntity.class));
    }

    @Test
    public void testAddSemester() {
        SemesterDTO semesterDTO = new SemesterDTO();
        semesterDTO.setName("Fall 2021");
        semesterDTO.setAbbreviation("F21");
        semesterDTO.setYear("2021");

        SemesterEntity entity = new SemesterEntity();
        entity.setId(1L);
        entity.setName("Fall 2021");
        entity.setAbbreviation("F21");
        entity.setYear("2021");

        when(semesterConverter.toEntity(semesterDTO)).thenReturn(entity);
        when(semesterRepository.save(entity)).thenReturn(entity);
        when(semesterConverter.toDTO(entity)).thenReturn(semesterDTO);

        SemesterDTO result = semesterService.addSemester(semesterDTO);

        assertEquals("Fall 2021", result.getName());
        verify(semesterConverter, times(1)).toEntity(semesterDTO);
        verify(semesterRepository, times(1)).save(entity);
        verify(semesterConverter, times(1)).toDTO(entity);
    }

    @Test
    public void testUpdateSemester() {
        SemesterDTO semesterDTO = new SemesterDTO();
        semesterDTO.setId(1L);
        semesterDTO.setName("Fall 2022");
        semesterDTO.setAbbreviation("F22");
        semesterDTO.setYear("2022");

        SemesterEntity entity = new SemesterEntity();
        entity.setId(1L);
        entity.setName("Fall 2021");
        entity.setAbbreviation("F21");
        entity.setYear("2021");

        when(semesterConverter.toEntity(semesterDTO)).thenReturn(entity);
        when(semesterRepository.save(entity)).thenReturn(entity);
        when(semesterConverter.toDTO(entity)).thenReturn(semesterDTO);

        SemesterDTO result = semesterService.updateSemester(semesterDTO);

        assertEquals("Fall 2022", result.getName());
        verify(semesterConverter, times(1)).toEntity(semesterDTO);
        verify(semesterRepository, times(1)).save(entity);
        verify(semesterConverter, times(1)).toDTO(entity);
    }

    @Test
    public void testRemoveSemester() {
        long id = 1L;

        semesterService.removeSemester(id);

        verify(semesterRepository, times(1)).deleteById(id);
    }
}
