package de.modulo.backend.controller;

import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ModuleTypeDTO;
import de.modulo.backend.entities.ModuleTypeEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.repositories.ModuleTypeRepository;
import de.modulo.backend.services.data.ModuleTypeService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleTypeControllerTest {

    @InjectMocks
    ModuleTypeController moduleTypeController;

    @Mock
    ValidatePrivilegesService validatePrivilegesService;

    @Mock
    ModuleTypeRepository moduleTypeRepository;

    @Mock
    HttpServletRequest mockRequest;

    @Mock
    ModuleTypeService moduleTypeService;

    @BeforeEach
    void setUp() throws InsufficientPermissionsException, NotifyException {
    }

    @Test
    void testCreateModuleType() throws InsufficientPermissionsException {
        // Arrange

        doNothing().when(validatePrivilegesService).validateSpoSpecificPrivileges(any(), any(), any(), any());

        ModuleTypeDTO moduleTypeDTO = new ModuleTypeDTO();
        moduleTypeDTO.setId(1L);
        moduleTypeDTO.setIndex(1);
        moduleTypeDTO.setName("Test");
        moduleTypeDTO.setSpoId(1L);

        // Act
        ResponseEntity<ModuleTypeDTO> response = moduleTypeController.createModuleType(moduleTypeDTO, mockRequest);
        // Assert
        verify(moduleTypeService).add(any());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testDeleteModuleType() throws InsufficientPermissionsException {
        // Arrange

        doNothing().when(validatePrivilegesService).validateSpoSpecificPrivileges(any(), any(), any(), any());

        Long id = 1L;
        ModuleTypeEntity mockModuleTypeEntity = new ModuleTypeEntity();
        mockModuleTypeEntity.setId(id);
        mockModuleTypeEntity.setSpo(new SpoEntity());
        mockModuleTypeEntity.getSpo().setId(1L);
        when(moduleTypeRepository.findById(id)).thenReturn(Optional.of(mockModuleTypeEntity));

        // Act
        ResponseEntity<Void> response = moduleTypeController.deleteModuleType(id, mockRequest);
        // Assert
        verify(moduleTypeService).delete(any());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testCreateModuleType_insufficientPermission() throws InsufficientPermissionsException {
        // Arrange
        doThrow(new InsufficientPermissionsException("")).when(validatePrivilegesService).validateSpoSpecificPrivileges(any(), any(), any(), any());

        ModuleTypeDTO moduleTypeDTO = new ModuleTypeDTO();
        moduleTypeDTO.setId(1L);
        moduleTypeDTO.setIndex(1);
        moduleTypeDTO.setName("Test");
        moduleTypeDTO.setSpoId(1L);

        // Act
        ResponseEntity<ModuleTypeDTO> response = moduleTypeController.createModuleType(moduleTypeDTO, mockRequest);

        // Assert
        verify(moduleTypeService, never()).add(any());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testDeleteModuleType_insufficientPermission() throws InsufficientPermissionsException {
        // Arrange
        doThrow(new InsufficientPermissionsException("")).when(validatePrivilegesService).validateSpoSpecificPrivileges(any(), any(), any(), any());

        Long id = 1L;
        ModuleTypeEntity mockModuleTypeEntity = new ModuleTypeEntity();
        mockModuleTypeEntity.setId(id);
        mockModuleTypeEntity.setSpo(new SpoEntity());
        mockModuleTypeEntity.getSpo().setId(1L);
        when(moduleTypeRepository.findById(id)).thenReturn(Optional.of(mockModuleTypeEntity));

        // Act
        ResponseEntity<Void> response = moduleTypeController.deleteModuleType(id, mockRequest);

        // Assert
        verify(moduleTypeService, never()).delete(any());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
