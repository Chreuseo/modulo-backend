package de.modulo.backend.services.data;

import de.modulo.backend.converters.ModuleImplementationConverter;
import de.modulo.backend.dtos.ModuleImplementationDTO;
import de.modulo.backend.dtos.ModuleImplementationDTOFlat;
import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.ModuleImplementationLecturerEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ModuleImplementationServiceTest {

    @InjectMocks
    private ModuleImplementationService service;

    @Mock
    private ModuleImplementationRepository moduleImplementationRepository;

    @Mock
    private ModuleImplementationConverter moduleImplementationConverter;

    @Mock
    private ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;

    @Mock
    private ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository;

    @Test
    public void testGetAllModuleImplementations() {
        // Given
        ModuleImplementationEntity entity = new ModuleImplementationEntity();
        ModuleImplementationDTOFlat dtoFlat = new ModuleImplementationDTOFlat();

        when(moduleImplementationRepository.findAll()).thenReturn(Collections.singletonList(entity));
        when(moduleImplementationConverter.toDtoFlat(entity)).thenReturn(dtoFlat);

        // When
        List<ModuleImplementationDTOFlat> result = service.getAllModuleImplementations();

        // Then
        assertEquals(1, result.size());
        assertEquals(dtoFlat, result.get(0));

        verify(moduleImplementationRepository, times(1)).findAll();
        verify(moduleImplementationConverter, times(1)).toDtoFlat(entity);
    }

    @Test
    public void testAddModuleImplementation() {
        // Given
        ModuleImplementationDTOFlat dtoFlat = new ModuleImplementationDTOFlat();
        ModuleImplementationEntity entity = new ModuleImplementationEntity();

        when(moduleImplementationConverter.toEntity(dtoFlat)).thenReturn(entity);
        when(moduleImplementationRepository.save(entity)).thenReturn(entity);
        when(moduleImplementationConverter.toDtoFlat(entity)).thenReturn(dtoFlat);

        // When
        ModuleImplementationDTOFlat result = service.addModuleImplementation(dtoFlat);

        // Then
        assertEquals(dtoFlat, result);

        verify(moduleImplementationConverter, times(1)).toEntity(dtoFlat);
        verify(moduleImplementationRepository, times(1)).save(entity);
        verify(moduleImplementationConverter, times(1)).toDtoFlat(entity);
    }

    @Test
    public void testUpdateModuleImplementation() throws InsufficientPermissionsException {
        // Given
        Long moduleId = 1L; // Specify the ID for the module implementation
        ModuleImplementationDTO dto = new ModuleImplementationDTO();
        dto.setId(moduleId); // Set it in the DTO

        UserEntity user = new UserEntity();
        user.setRole(ROLE.ADMIN);

        ModuleImplementationEntity entity = new ModuleImplementationEntity();
        ModuleImplementationEntity oldEntity = new ModuleImplementationEntity();
        oldEntity.setResponsible(user); // old responsible is admin

        when(moduleImplementationConverter.toEntity(dto)).thenReturn(entity);
        when(moduleImplementationRepository.findById(moduleId)).thenReturn(Optional.of(oldEntity));
        when(moduleImplementationRepository.save(entity)).thenReturn(entity);
        when(moduleImplementationConverter.toDto(entity)).thenReturn(dto);

        // When
        ModuleImplementationDTO result = service.updateModuleImplementation(dto, user);

        // Then
        assertEquals(dto, result);

        // Verify that the right methods were called
        verify(moduleImplementationRepository, times(1)).findById(moduleId);
        verify(moduleImplementationRepository, times(1)).save(entity);
    }

    @Test
    public void testGetModuleImplementationById() {
        // Given
        Long moduleId = 1L;
        ModuleImplementationEntity entity = new ModuleImplementationEntity();
        ModuleImplementationDTO dto = new ModuleImplementationDTO();

        when(moduleImplementationRepository.findById(moduleId)).thenReturn(Optional.of(entity));
        when(moduleImplementationConverter.toDto(entity)).thenReturn(dto);

        // When
        ModuleImplementationDTO result = service.getModuleImplementationById(moduleId);

        // Then
        assertEquals(dto, result);

        verify(moduleImplementationRepository, times(1)).findById(moduleId);
        verify(moduleImplementationConverter, times(1)).toDto(entity);
    }

    @Test
    public void testDeleteModuleImplementation() {
        // Given
        Long moduleId = 1L;

        // When
        service.deleteModuleImplementation(moduleId);

        // Then
        verify(moduleFrameModuleImplementationRepository, times(1)).deleteModuleFrameModuleImplementationEntitiesByModuleImplementationId(moduleId);
        verify(moduleImplementationLecturerRepository, times(1)).deleteModuleImplementationLecturerEntitiesByModuleImplementationId(moduleId);
        verify(examTypeModuleImplementationRepository, times(1)).deleteExamTypeModuleImplementationEntitiesByModuleImplementationId(moduleId);
        verify(moduleImplementationRepository, times(1)).deleteById(moduleId);
    }

    @Test
    public void testGetAllAssignedModuleImplementations() {
        // Given
        Long userId = 1L;
        ModuleImplementationEntity entity = new ModuleImplementationEntity();
        ModuleImplementationDTOFlat dtoFlat = new ModuleImplementationDTOFlat();

        when(moduleImplementationRepository.getModuleImplementationEntitiesByResponsibleId(userId)).thenReturn(Collections.singletonList(entity));
        when(moduleImplementationLecturerRepository.getModuleImplementationLecturerEntitiesByLecturerId(userId)).thenReturn(Collections.emptyList());
        when(moduleImplementationConverter.toDtoFlat(entity)).thenReturn(dtoFlat);

        // When
        List<ModuleImplementationDTOFlat> result = service.getAllAssignedModuleImplementations(userId);

        // Then
        assertEquals(1, result.size());
        assertEquals(dtoFlat, result.get(0));

        verify(moduleImplementationRepository, times(1)).getModuleImplementationEntitiesByResponsibleId(userId);
        verify(moduleImplementationLecturerRepository, times(1)).getModuleImplementationLecturerEntitiesByLecturerId(userId);
        verify(moduleImplementationConverter, times(1)).toDtoFlat(entity);
    }

    @Test
    public void testAddModuleImplementationAndSetResponsible() {
        // Given
        ModuleImplementationDTOFlat dtoFlat = new ModuleImplementationDTOFlat();
        UserEntity user = new UserEntity();
        ModuleImplementationEntity entity = new ModuleImplementationEntity();

        when(moduleImplementationConverter.toEntity(dtoFlat)).thenReturn(entity);
        when(moduleImplementationRepository.save(entity)).thenReturn(entity);
        when(moduleImplementationConverter.toDtoFlat(entity)).thenReturn(dtoFlat);

        // When
        ModuleImplementationDTOFlat result = service.addModuleImplementationAndSetResponsible(dtoFlat, user);

        // Then
        assertEquals(dtoFlat, result);
        assertEquals(user, entity.getResponsible());

        verify(moduleImplementationConverter, times(1)).toEntity(dtoFlat);
        verify(moduleImplementationRepository, times(1)).save(entity);
        verify(moduleImplementationConverter, times(1)).toDtoFlat(entity);
    }

    @Test
    public void testGetModuleImplementationFlatById() {
        // Given
        Long moduleId = 1L;
        ModuleImplementationEntity entity = new ModuleImplementationEntity();
        ModuleImplementationDTOFlat dtoFlat = new ModuleImplementationDTOFlat();

        when(moduleImplementationRepository.findById(moduleId)).thenReturn(Optional.of(entity));
        when(moduleImplementationConverter.toDtoFlat(entity)).thenReturn(dtoFlat);

        // When
        ModuleImplementationDTOFlat result = service.getModuleImplementationFlatById(moduleId);

        // Then
        assertEquals(dtoFlat, result);

        verify(moduleImplementationRepository, times(1)).findById(moduleId);
        verify(moduleImplementationConverter, times(1)).toDtoFlat(entity);
    }

    @Test
    public void testGetModuleImplementationFlatByIdNotFound() {
        // Given
        Long moduleId = 1L;

        when(moduleImplementationRepository.findById(moduleId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getModuleImplementationFlatById(moduleId);
        });
        assertEquals("Module Implementation not found with id: " + moduleId, exception.getMessage());

        verify(moduleImplementationRepository, times(1)).findById(moduleId);
    }

    @Test
    public void testAddLecturerToModuleImplementation() {
        // Given
        Long moduleImplementationId = 1L;
        Long lecturerId = 2L;
        ModuleImplementationEntity moduleImplementationEntity = new ModuleImplementationEntity();
        UserEntity lecturer = new UserEntity();
        ModuleImplementationDTO dto = new ModuleImplementationDTO();

        when(moduleImplementationRepository.findById(moduleImplementationId)).thenReturn(Optional.of(moduleImplementationEntity));
        when(userRepository.findById(lecturerId)).thenReturn(Optional.of(lecturer));
        when(moduleImplementationConverter.toDto(moduleImplementationEntity)).thenReturn(dto);

        // When
        ModuleImplementationDTO result = service.addLecturerToModuleImplementation(moduleImplementationId, lecturerId);

        // Then
        assertEquals(dto, result);

        verify(moduleImplementationRepository, times(1)).findById(moduleImplementationId);
        verify(userRepository, times(1)).findById(lecturerId);
        verify(moduleImplementationLecturerRepository, times(1)).save(any(ModuleImplementationLecturerEntity.class));
    }

    @Test
    public void testRemoveLecturerFromModuleImplementation() {
        // Given
        Long moduleImplementationId = 1L;
        Long lecturerId = 2L;

        // When
        service.removeLecturerFromModuleImplementation(moduleImplementationId, lecturerId);

        // Then
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId id = new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId();
        id.setModuleImplementation(moduleImplementationId);
        id.setLecturer(lecturerId);

        verify(moduleImplementationLecturerRepository, times(1)).deleteById(id);
    }
}
