package de.modulo.backend.services.data;

import de.modulo.backend.converters.ExamTypeConverter;
import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.entities.ExamTypeEntity;
import de.modulo.backend.entities.ExamTypeModuleFrameEntity;
import de.modulo.backend.repositories.ExamTypeModuleFrameRepository;
import de.modulo.backend.repositories.ExamTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamTypeServiceTest {

    @InjectMocks
    private ExamTypeService examTypeService;

    @Mock
    private ExamTypeRepository examTypeRepository;

    @Mock
    private ExamTypeModuleFrameRepository examTypeModuleFrameRepository;

    @Mock
    private ExamTypeConverter examTypeConverter;

    private ExamTypeEntity mockEntity;
    private ExamTypeDTO mockDto;

    @BeforeEach
    public void setUp() {
        mockEntity = new ExamTypeEntity();
        mockEntity.setId(1L);
        mockEntity.setName("Test Exam");
        mockEntity.setAbbreviation("TE");
        mockEntity.setLength("60 minutes");

        mockDto = new ExamTypeDTO();
        mockDto.setId(1L);
        mockDto.setName("Test Exam");
        mockDto.setAbbreviation("TE");
        mockDto.setLength("60 minutes");
        // Set the SPO ID as needed, e.g. mockDto.setSpoId(5L);
    }

    @Test
    public void testGetBySpo() {
        long spoId = 1L;
        when(examTypeRepository.findBySpoId(spoId)).thenReturn(Collections.singletonList(mockEntity));
        when(examTypeConverter.toDto(mockEntity)).thenReturn(mockDto);

        List<ExamTypeDTO> result = examTypeService.getBySpo(spoId);

        assertEquals(1, result.size());
        assertEquals(mockDto, result.get(0));
        verify(examTypeRepository, times(1)).findBySpoId(spoId);
    }

    @Test
    public void testGetByModuleFrame() {
        long moduleFrameId = 1L;
        ExamTypeModuleFrameEntity moduleFrameEntity = new ExamTypeModuleFrameEntity();
        moduleFrameEntity.setExamType(mockEntity);
        moduleFrameEntity.setMandatory(true);

        when(examTypeModuleFrameRepository.getExamTypeModuleFrameEntitiesByModuleFrameId(moduleFrameId))
                .thenReturn(Collections.singletonList(moduleFrameEntity));
        when(examTypeConverter.toDto(mockEntity)).thenReturn(mockDto);

        List<ExamTypeDTO> result = examTypeService.getByModuleFrame(moduleFrameId);

        assertEquals(1, result.size());
        assertEquals(mockDto, result.get(0));
        assertTrue(result.get(0).isMandatory());
        verify(examTypeModuleFrameRepository, times(1)).getExamTypeModuleFrameEntitiesByModuleFrameId(moduleFrameId);
    }

    @Test
    public void testAdd() {
        when(examTypeConverter.toEntity(mockDto)).thenReturn(mockEntity);
        when(examTypeRepository.save(mockEntity)).thenReturn(mockEntity);
        when(examTypeConverter.toDto(mockEntity)).thenReturn(mockDto);

        ExamTypeDTO result = examTypeService.add(mockDto);

        assertNotNull(result);
        assertEquals(mockDto, result);
        verify(examTypeConverter, times(1)).toEntity(mockDto);
        verify(examTypeRepository, times(1)).save(mockEntity);
        verify(examTypeConverter, times(1)).toDto(mockEntity);
    }

    @Test
    public void testRemove() {
        long id = 1L;

        examTypeService.remove(id);

        verify(examTypeRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdate() {
        when(examTypeConverter.toEntity(mockDto)).thenReturn(mockEntity);
        when(examTypeRepository.save(mockEntity)).thenReturn(mockEntity);
        when(examTypeConverter.toDto(mockEntity)).thenReturn(mockDto);

        ExamTypeDTO result = examTypeService.update(mockDto);

        assertNotNull(result);
        assertEquals(mockDto, result);
        verify(examTypeConverter, times(1)).toEntity(mockDto);
        verify(examTypeRepository, times(1)).save(mockEntity);
        verify(examTypeConverter, times(1)).toDto(mockEntity);
    }
}
