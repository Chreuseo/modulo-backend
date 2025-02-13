package de.modulo.backend.controller;

import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.SectionDTO;
import de.modulo.backend.entities.SectionEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.repositories.SectionRepository;
import de.modulo.backend.services.NotifyService;
import de.modulo.backend.services.data.SectionService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SectionControllerTest {

    @InjectMocks
    SectionController sectionController;

    @Mock
    ValidatePrivilegesService validatePrivilegesService;

    @Mock
    HttpServletRequest mockRequest;

    @Mock
    SectionService sectionService;

    @Mock
    SectionRepository sectionRepository;

    @Mock
    NotifyService notifyService;

    @Test
    void testCreateSection() throws InsufficientPermissionsException {
        // Arrange
        doNothing().when(validatePrivilegesService).validateSpoSpecificPrivileges(any(), any(), any(), any());
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(1L);
        sectionDTO.setIndex(1);
        sectionDTO.setName("Test");
        sectionDTO.setSpoId(1L);

        // Act
        ResponseEntity<SectionDTO> response = sectionController.createSection(sectionDTO, mockRequest);

        // Assert
        verify(sectionService).add(any());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testDeleteSection() throws InsufficientPermissionsException {
        // Arrange
        doNothing().when(validatePrivilegesService).validateSpoSpecificPrivileges(any(), any(), any(), any());
        SectionEntity sectionEntity = new SectionEntity();
        sectionEntity.setId(1L);
        sectionEntity.setSpo(new SpoEntity());
        sectionEntity.getSpo().setId(1L);

        when(sectionRepository.findById(any())).thenReturn(Optional.of(sectionEntity));
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(1L);
        sectionDTO.setIndex(1);
        sectionDTO.setName("Test");
        sectionDTO.setSpoId(1L);

        // Act
        ResponseEntity<Void> response = sectionController.deleteSection(sectionDTO.getId(), mockRequest);

        // Assert
        verify(sectionService).delete(any());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testCreateSection_insufficientPermission() throws InsufficientPermissionsException {
        // Arrange
        doThrow(new InsufficientPermissionsException("")).when(validatePrivilegesService).validateSpoSpecificPrivileges(any(), any(), any(), any());
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(1L);
        sectionDTO.setIndex(1);
        sectionDTO.setName("Test");
        sectionDTO.setSpoId(1L);

        // Act
        ResponseEntity<SectionDTO> response = sectionController.createSection(sectionDTO, mockRequest);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testDeleteSection_insufficientPermission() throws InsufficientPermissionsException {
        // Arrange
        doThrow(new InsufficientPermissionsException("")).when(validatePrivilegesService).validateSpoSpecificPrivileges(any(), any(), any(), any());
        SectionEntity sectionEntity = new SectionEntity();
        sectionEntity.setId(1L);
        sectionEntity.setSpo(new SpoEntity());
        sectionEntity.getSpo().setId(1L);

        when(sectionRepository.findById(any())).thenReturn(Optional.of(sectionEntity));
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(1L);
        sectionDTO.setIndex(1);
        sectionDTO.setName("Test");
        sectionDTO.setSpoId(1L);

        // Act
        ResponseEntity<Void> response = sectionController.deleteSection(sectionDTO.getId(), mockRequest);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

}
