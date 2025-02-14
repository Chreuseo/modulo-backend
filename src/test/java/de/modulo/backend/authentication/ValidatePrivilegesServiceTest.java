package de.modulo.backend.authentication;

import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.NOTIFICATION;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.repositories.ModuleImplementationRepository;
import de.modulo.backend.repositories.ModuleImplementationLecturerRepository;
import de.modulo.backend.repositories.SpoResponsibleUserRepository;
import de.modulo.backend.repositories.SpoRepository;
import de.modulo.backend.repositories.UserRepository;
import de.modulo.backend.services.NotifyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidatePrivilegesServiceTest {

    @InjectMocks
    private ValidatePrivilegesService validatePrivilegesService;

    @Mock
    private SessionService sessionService;

    @Mock
    private ModuleImplementationRepository moduleImplementationRepository;

    @Mock
    private ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;

    @Mock
    private SpoResponsibleUserRepository spoResponsibleUserRepository;

    @Mock
    private SpoRepository spoRepository;

    @Mock
    private NotifyService notifyService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testValidateGeneralPrivileges_AdminRole_ShouldPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        when(sessionService.getRoleBySessionId(any(UUID.class))).thenReturn(ROLE.ADMIN);

        // Act & Assert
        validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ_DETAILS, UUID.randomUUID().toString());
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdminForGeneralSettings_AddPrivilege_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_UserRole_SpoSettings_DeletePrivilege_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.DELETE, sessionToken));
    }

    @Test
    void testValidateSpoSpecificPrivileges_AdminRole_ShouldPass() throws InsufficientPermissionsException {
        // Arrange
        when(sessionService.getRoleBySessionId(any(UUID.class))).thenReturn(ROLE.ADMIN);
        when(spoRepository.findById(any(Long.class))).thenReturn(Optional.of(new SpoEntity()));

        // Act & Assert
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.UPDATE, UUID.randomUUID().toString(), 1L);
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdminRole_UnauthorizedUpdate_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        when(spoRepository.findById(any(Long.class))).thenReturn(Optional.of(new SpoEntity()));

        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(Long.class), any(Long.class))).thenReturn(false);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.UPDATE, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_AdminRole_ShouldPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        when(sessionService.getRoleBySessionId(any(UUID.class))).thenReturn(ROLE.ADMIN);

        // Act & Assert
        validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.READ, UUID.randomUUID().toString(), 1L);
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdminRole_ModuleNotResponsible_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(any(Long.class))).thenReturn(Optional.of(new ModuleImplementationEntity()));
        when(moduleImplementationLecturerRepository.existsById(any())).thenReturn(false); // Not a lecturer

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_AdminRole_ShouldPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        when(sessionService.getRoleBySessionId(any(UUID.class))).thenReturn(ROLE.ADMIN);
        when(moduleImplementationRepository.findById(any(Long.class))).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act & Assert
        validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.READ, UUID.randomUUID().toString(), 1L, 1L);
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_SpoAdmin_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(Long.class), any(Long.class))).thenReturn(false);
        when(moduleImplementationRepository.findById(any(Long.class))).thenReturn(Optional.of(new ModuleImplementationEntity()));


        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_UserRole_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(Long.class), any(Long.class))).thenReturn(false);
        when(moduleImplementationRepository.findById(any(Long.class))).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.ADD, sessionToken, 1L, 1L));
    }

    // Additional tests for NotifyException can be similarly written where appropriate...
}
