package de.modulo.backend.services.data;

import de.modulo.backend.converters.ModuleFrameConverter;
import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.dtos.ModuleFrameSetDTO;
import de.modulo.backend.entities.ModuleFrameEntity;
import de.modulo.backend.entities.SectionEntity;
import de.modulo.backend.entities.ModuleTypeEntity;
import de.modulo.backend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModuleFrameServiceTest {

    @InjectMocks
    private ModuleFrameService service;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private ModuleTypeRepository moduleTypeRepository;

    @Mock
    private ModuleFrameRepository moduleFrameRepository;

    @Mock
    private ModuleFrameConverter moduleFrameConverter;

    @Mock
    private ExamTypeModuleFrameRepository examTypeModuleFrameRepository;

    @Mock
    private CourseTypeModuleFrameRepository courseTypeModuleFrameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetModuleFrameSetDTOBySpoId() {
        Long spoId = 1L;

        // Mock SectionEntity
        SectionEntity sectionEntity = new SectionEntity();
        sectionEntity.setId(1L);
        sectionEntity.setName("Section 1");
        sectionEntity.setOrderNumber(1);

        // Mock ModuleTypeEntity
        ModuleTypeEntity moduleTypeEntity = new ModuleTypeEntity();
        moduleTypeEntity.setId(1L);
        moduleTypeEntity.setName("Module Type 1");
        moduleTypeEntity.setOrderNumber(1);

        when(sectionRepository.findBySpoId(spoId)).thenReturn(Collections.singletonList(sectionEntity));
        when(moduleTypeRepository.findBySpoId(spoId)).thenReturn(Collections.singletonList(moduleTypeEntity));

        ModuleFrameSetDTO.Section mockedSectionDto = new ModuleFrameSetDTO.Section();
        mockedSectionDto.setId(sectionEntity.getId());
        mockedSectionDto.setName(sectionEntity.getName());
        mockedSectionDto.setIndex(sectionEntity.getOrderNumber());

        when(moduleFrameConverter.toDto(any())).thenReturn(null); // Mock the conversion

        ModuleFrameSetDTO result = service.getModuleFrameSetDTOBySpoId(spoId);

        assertNotNull(result);
        assertEquals(1, result.getSections().size());
        assertEquals(mockedSectionDto.getId(), result.getSections().get(0).getId());
    }

    @Test
    void testAddModuleFrame() {
        ModuleFrameDTO moduleFrameDTO = new ModuleFrameDTO();
        moduleFrameDTO.setId(1L);
        moduleFrameDTO.setName("Module Frame 1");

        ModuleFrameEntity savedEntity = new ModuleFrameEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Module Frame 1");

        when(moduleFrameConverter.toEntity(moduleFrameDTO)).thenReturn(savedEntity);
        when(moduleFrameRepository.save(savedEntity)).thenReturn(savedEntity);
        when(moduleFrameConverter.toDto(savedEntity)).thenReturn(moduleFrameDTO);

        ModuleFrameDTO result = service.addModuleFrame(moduleFrameDTO);

        assertNotNull(result);
        assertEquals(moduleFrameDTO.getId(), result.getId());
        verify(moduleFrameRepository, times(1)).save(savedEntity);
    }

    @Test
    void testDeleteModuleFrame() {
        Long moduleId = 1L;
        when(moduleFrameRepository.existsById(moduleId)).thenReturn(true);

        service.deleteModuleFrame(moduleId);

        verify(moduleFrameRepository, times(1)).deleteById(moduleId);
    }

    @Test
    void testDeleteModuleFrameNotFound() {
        Long moduleId = 1L;
        when(moduleFrameRepository.existsById(moduleId)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.deleteModuleFrame(moduleId);
        });

        assertEquals("ModuleFrame not found with id: " + moduleId, exception.getMessage());
    }

    @Test
    void testUpdateModuleFrame() {
        ModuleFrameDTO moduleFrameDTO = new ModuleFrameDTO();
        moduleFrameDTO.setId(1L);
        moduleFrameDTO.setName("Module Frame Updated");

        ModuleFrameEntity existingEntity = new ModuleFrameEntity();
        existingEntity.setId(1L);

        when(moduleFrameRepository.existsById(moduleFrameDTO.getId())).thenReturn(true);
        when(moduleFrameConverter.toEntity(moduleFrameDTO)).thenReturn(existingEntity);
        when(moduleFrameRepository.save(existingEntity)).thenReturn(existingEntity);
        when(moduleFrameConverter.toDto(existingEntity)).thenReturn(moduleFrameDTO);

        ModuleFrameDTO result = service.updateModuleFrame(moduleFrameDTO);

        assertNotNull(result);
        assertEquals("Module Frame Updated", result.getName());
        verify(moduleFrameRepository, times(1)).save(existingEntity);
    }

    @Test
    void testUpdateModuleFrameNotFound() {
        ModuleFrameDTO moduleFrameDTO = new ModuleFrameDTO();
        moduleFrameDTO.setId(1L);

        when(moduleFrameRepository.existsById(moduleFrameDTO.getId())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.updateModuleFrame(moduleFrameDTO);
        });

        assertEquals("ModuleFrame not found with id: " + moduleFrameDTO.getId(), exception.getMessage());
    }
}
