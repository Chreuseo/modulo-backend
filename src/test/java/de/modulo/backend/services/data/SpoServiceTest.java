package de.modulo.backend.services.data;

import de.modulo.backend.converters.*;
import de.modulo.backend.dtos.*;
import de.modulo.backend.entities.*;
import de.modulo.backend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpoServiceTest {

    @InjectMocks
    private SpoService service;

    @Mock
    private SpoRepository spoRepository;

    @Mock
    private SpoConverter spoConverter;

    @Mock
    private SectionConverter sectionConverter;

    @Mock
    private ModuleRequirementConverter moduleRequirementConverter;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private ModuleTypeRepository moduleTypeRepository;

    @Mock
    private ModuleTypeConverter moduleTypeConverter;

    @Mock
    private ExamTypeService examTypeService;

    @Mock
    private ModuleRequirementRepository moduleRequirementRepository;

    @Mock
    private SpoResponsibleUserRepository spoResponsibleUserRepository;

    @Mock
    private UserConverter userConverter;

    @Mock
    private UserRepository userRepository;

    private SpoEntity mockSpoEntity;
    private SpoDTO mockSpoDTO;
    private SpoDTOFlat mockSpoDTOFlat;

    @BeforeEach
    public void setUp() {
        mockSpoEntity = new SpoEntity(); // Create a mock SpoEntity
        mockSpoEntity.setId(1L);
        mockSpoEntity.setName("Mock Spo");

        mockSpoDTO = new SpoDTO(); // Create a mock SpoDTO
        mockSpoDTO.setId(1L);
        mockSpoDTO.setName("Mock Spo");

        mockSpoDTOFlat = new SpoDTOFlat(); // Create a mock SpoDTOFlat
        mockSpoDTOFlat.setId(1L);
        mockSpoDTOFlat.setName("Mock Spo");
    }

    @Test
    public void testGetAllSpos() {
        when(spoRepository.findAll()).thenReturn(Collections.singletonList(mockSpoEntity));
        when(spoConverter.toDtoFlat(mockSpoEntity)).thenReturn(mockSpoDTOFlat);

        assertEquals(1, service.getAllSpos().size());
        assertEquals("Mock Spo", service.getAllSpos().get(0).getName());
    }

    @Test
    public void testAdd() {
        when(spoConverter.toEntity(mockSpoDTOFlat)).thenReturn(mockSpoEntity);
        when(spoRepository.save(mockSpoEntity)).thenReturn(mockSpoEntity);
        when(spoConverter.toDtoFlat(mockSpoEntity)).thenReturn(mockSpoDTOFlat);

        SpoDTOFlat result = service.add(mockSpoDTOFlat);
        assertEquals(mockSpoDTOFlat.getId(), result.getId());
    }

    @Test
    public void testDelete_Success() {
        when(spoRepository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(spoRepository).deleteById(1L);
    }

    @Test
    public void testDelete_SpoNotFound() {
        when(spoRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.delete(1L);
        });

        assertEquals("Spo not found with id: 1", exception.getMessage());
    }

    @Test
    public void testUpdate_Success1() {
        when(spoRepository.existsById(1L)).thenReturn(true);
        when(spoConverter.toEntity(mockSpoDTO)).thenReturn(mockSpoEntity);
        when(spoRepository.save(mockSpoEntity)).thenReturn(mockSpoEntity);
        when(spoConverter.toDto(mockSpoEntity)).thenReturn(mockSpoDTO);

        SpoDTO result = service.update(mockSpoDTO);
        assertEquals(mockSpoDTO.getId(), result.getId());
    }

    @Test
    public void testUpdate_Success2() {
        when(spoRepository.existsById(1L)).thenReturn(true);
        when(spoConverter.toEntity(mockSpoDTO)).thenReturn(mockSpoEntity);
        when(spoRepository.save(mockSpoEntity)).thenReturn(mockSpoEntity);
        when(spoConverter.toDto(mockSpoEntity)).thenReturn(mockSpoDTO);

        List<SectionDTO> sectionDTOs = new ArrayList<>();
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(1L);
        sectionDTO.setName("Section");
        sectionDTO.setIndex(1);
        sectionDTOs.add(sectionDTO);

        List<ModuleTypeDTO> moduleTypeDTOs = new ArrayList<>();
        ModuleTypeDTO moduleTypeDTO = new ModuleTypeDTO();
        moduleTypeDTO.setId(1L);
        moduleTypeDTO.setName("ModuleType");
        moduleTypeDTO.setIndex(1);
        moduleTypeDTOs.add(moduleTypeDTO);

        List<ModuleRequirementDTO> moduleRequirementDTOs = new ArrayList<>();
        ModuleRequirementDTO moduleRequirementDTO = new ModuleRequirementDTO();
        moduleRequirementDTO.setId(1L);
        moduleRequirementDTO.setName("ModuleRequirement");
        moduleRequirementDTOs.add(moduleRequirementDTO);

        mockSpoDTO.setSectionDTOs(sectionDTOs);
        mockSpoDTO.setModuleTypeDTOs(moduleTypeDTOs);
        mockSpoDTO.setModuleRequirementDTOs(moduleRequirementDTOs);

        when(sectionConverter.toEntity(sectionDTO)).thenReturn(new SectionEntity());
        when(moduleTypeConverter.toEntity(moduleTypeDTO)).thenReturn(new ModuleTypeEntity());
        when(moduleRequirementConverter.toEntity(moduleRequirementDTO)).thenReturn(new ModuleRequirementEntity());

        SpoDTO result = service.update(mockSpoDTO);
        assertEquals(mockSpoDTO.getId(), result.getId());
    }

    @Test
    public void testUpdate_SpoNotFound() {
        when(spoRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.update(mockSpoDTO);
        });

        assertEquals("Spo not found with id: 1", exception.getMessage());
    }

    @Test
    public void testGetSpo_Success() {
        when(spoRepository.findById(1L)).thenReturn(Optional.of(mockSpoEntity));
        when(spoConverter.toDto(mockSpoEntity)).thenReturn(mockSpoDTO);

        SpoDTO result = service.getSpo(1L);
        assertEquals(mockSpoDTO.getId(), result.getId());
    }

    @Test
    public void testGetSpo_SpoNotFound() {
        when(spoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getSpo(1L);
        });

        assertEquals("Spo not found with id: 1", exception.getMessage());
    }

    @Test
    public void testAddResponsible_Success() {
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setId(1L);
        mockUserEntity.setFirstName("Mock User");

        when(spoRepository.findById(1L)).thenReturn(Optional.of(mockSpoEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUserEntity));

        service.addResponsible(1L, 1L);

        ArgumentCaptor<SpoResponsibleUserEntity> captor = ArgumentCaptor.forClass(SpoResponsibleUserEntity.class);
        verify(spoResponsibleUserRepository).save(captor.capture());
        assertEquals(1L, captor.getValue().getUser().getId());
        assertEquals(1L, captor.getValue().getSpo().getId());
    }

    @Test
    public void testRemoveResponsible_Success() {
        SpoResponsibleUserEntity.SpoResponsibleUserId id = new SpoResponsibleUserEntity.SpoResponsibleUserId();
        id.setSpoId(1L);
        id.setUserId(1L);

        SpoResponsibleUserEntity mockResponsibleUser = new SpoResponsibleUserEntity();
        mockResponsibleUser.setId(id);

        when(spoResponsibleUserRepository.existsById(id)).thenReturn(true);

        service.removeResponsible(1L, 1L);

        verify(spoResponsibleUserRepository).deleteById(id);
    }

    @Test
    public void testRemoveResponsible_NotFound() {
        when(spoResponsibleUserRepository.existsById(any())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.removeResponsible(1L, 1L);
        });

        assertEquals("Responsible user not found with spoId: 1 and userId: 1", exception.getMessage());
    }
}
