package de.modulo.backend.authentication;

import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.ModuleImplementationLecturerEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ENTITY_TYPE;
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
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

    @Test
    void testValidateGeneralPrivileges_UserRole_AddSetting_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_UserRole_DeleteSpSettings_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.DELETE, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_AddUser_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdminAuthorizedUpdate_ShouldPass() throws InsufficientPermissionsException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(1L)).thenReturn(Optional.of(new SpoEntity()));
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(), any())).thenReturn(true); // User responsible for SPO

        // Act & Assert
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.UPDATE, sessionToken, 1L);
    }

    @Test
    void testValidateSpoSpecificPrivileges_UserTryingToDeleteSPO_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(spoRepository.findById(1L)).thenReturn(Optional.of(new SpoEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.DELETE, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_UserRole_CannotAddModule_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdminRole_ValidUpdate_ShouldPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));
        when(moduleImplementationLecturerRepository.existsById(any())).thenReturn(false); // Not a lecturer
        when(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken))).thenReturn(2L); // Not responsible

        // Act & Assert
        // Verify InsufficientPermissionsException is thrown
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_UserRole_CannotAddModule_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.ADD, sessionToken, 1L, 1L));
    }


    @Test
    void testValidateSpoOrModuleSpecificPrivileges_SpoAdmin_NotResponsibleForModule_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(Long.class), any(Long.class))).thenReturn(false);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_SpoAdmin_NotifyUsersOnUpdate_ShouldThrowNotifyException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(Long.class), any(Long.class))).thenReturn(true);
        when(moduleImplementationLecturerRepository.existsById(any())).thenReturn(true); // User is a lecturer

        // Act & Assert
        assertThrows(NotifyException.class, () -> {
            validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L, 1L);
        });
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_ForSPOSettings_AddPrivilege_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_ForUser_AddPrivilege_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_UserRole_ForModule_AddPrivilege_ShouldThrowNotifyException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act & Assert
        assertThrows(NotifyException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdminRole_CannotDeleteSpo_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(any(Long.class))).thenReturn(Optional.of(new SpoEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.DELETE, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdminRole_CannotDeleteModule_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(any(Long.class))).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.DELETE, sessionToken, 1L));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_SpoAdmin_CanAddSpo() throws NotifyException, InsufficientPermissionsException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(1L)).thenReturn(Optional.of(new SpoEntity()));

        // Act - no Exceptions should be thrown
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.ADD, sessionToken, 1L);
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.ADD, sessionToken, 1L);
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_CanUpdateModule_ShouldNotifyResponsible() throws NotifyException, InsufficientPermissionsException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(any())).thenReturn(Optional.of(new ModuleImplementationEntity()));
        when(moduleImplementationLecturerRepository.existsById(any(ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId.class))).thenReturn(true);
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(Long.class), any(Long.class))).thenReturn(true);
        when(sessionService.getUserIdBySessionId(any())).thenReturn(1L); // Ensure user is responsible

        // Act and Assert
        assertThrows(NotifyException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_ForDocument_UpdatePrivilege_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.UPDATE, sessionToken));
    }

    @Test
    void testValidateModuleSpecificPrivileges_UserRole_CannotReadModule_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(any())).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_ForSPOSettings_ReadPrivilege_ShouldPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act
        validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.READ, sessionToken);
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_UserRole_CannotUpdateSpo() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(any())).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_ForSPO_CreatePrivilege_ShouldWarnAdmin() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(userRepository.findAllByRole(ROLE.ADMIN)).thenReturn(new ArrayList<>()); // Testing notify

        // Act
        assertThrows(NotifyException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.ADD, sessionToken)); // Should hit the notification path
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_ForDocument_ReadPrivilege_NotPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act
        assertThrows(InsufficientPermissionsException.class, () -> validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.READ, sessionToken));
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdminRole_WithLecturer_ThrowsInsufficientPermissionsException_IfNotResponsible() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        ModuleImplementationEntity module = new ModuleImplementationEntity();
        UserEntity user = new UserEntity();
        user.setId(1L); // Assume this is the lecturer's ID.
        module.setResponsible(user);

        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));
        when(moduleImplementationLecturerRepository.existsById(any())).thenReturn(false); // User is a lecturer.

        // Act
        assertThrows(InsufficientPermissionsException.class, () -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdminRole_NotResponsibleButIsLecturer_ShouldNotify() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        ModuleImplementationEntity module = new ModuleImplementationEntity();
        module.setResponsible(null); // No responsible user set.

        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));
        when(moduleImplementationLecturerRepository.existsById(any())).thenReturn(true); // Simulate that the user is a lecturer.
        when(sessionService.getUserIdBySessionId(any())).thenReturn(2L); // Assume user ID does not match the responsible.

        // Act & Assert
        assertThrows(NotifyException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L));
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdminCannotDeleteSPOs_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.DELETE, sessionToken));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_UserRole_CannotUpdateSpoSettings_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(any())).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.UPDATE, sessionToken, 1L, 2L));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_NonResponsibleUser_CannotUpdateModule_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        ModuleImplementationEntity module = new ModuleImplementationEntity();
        module.setResponsible(new UserEntity()); // Set different responsible user than the session user
        module.getResponsible().setId(3L); // Assume this is the responsible user

        when(moduleImplementationRepository.findById(any(Long.class))).thenReturn(Optional.of(module));
        when(moduleImplementationLecturerRepository.existsById(any())).thenReturn(false); // Not a lecturer
        when(sessionService.getUserIdBySessionId(any())).thenReturn(2L); // User not responsible for the module

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_AdminRole_DocumentRead_ShouldPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.ADMIN);

        // Act
        validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.READ, sessionToken, 1L);
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_SpoAdmin_CannotAddSPO_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(any())).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.ADD, sessionToken, 1L, 2L));
    }


    /* Additional test for NotifyException in case someone who is not responsible tries to update a module */
    @Test
    void testValidateModuleSpecificPrivileges_SpoAdminRole_NotResponsible_ShouldThrowNotifyException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        ModuleImplementationEntity module = new ModuleImplementationEntity();
        // Set up module with a responsible that is not the user we are simulating
        module.setResponsible(new UserEntity()); // Different responsible user
        module.getResponsible().setId(3L); // Assume this is the responsible user
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));
        when(moduleImplementationLecturerRepository.existsById(any())).thenReturn(true); // User is a lecturer
        when(sessionService.getUserIdBySessionId(any())).thenReturn(2L); // Not responsible

        // Act & Assert
        assertThrows(NotifyException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L));
    }

    /* Testing privileges to add a module which is an admin action */
    @Test
    void testValidateGeneralPrivileges_AdminRole_AddModule_ShouldPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.ADMIN);

        // Act
        validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.ADD, sessionToken);
    }

    /* Testing if a USER cannot delete a module */
    @Test
    void testValidateGeneralPrivileges_UserRole_DeleteModule_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.DELETE, sessionToken));
    }

    /* Check if NotifyException is raised when adding a module */
    @Test
    void testValidateGeneralPrivileges_SpoAdmin_AddModule_ShouldThrowNotifyException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(userRepository.findAllByRole(ROLE.ADMIN)).thenReturn(new ArrayList<>()); // Raise NotifyException for no administrators

        // Act & Assert
        assertThrows(NotifyException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.ADD, sessionToken));
    }

    // Document Tests
    @Test
    void testValidateGeneralPrivileges_AdminRole_ForDocument_Delete_ShouldPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.ADMIN);

        // Act
        validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.DELETE, sessionToken);
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_ForDocument_AddPrivilege_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_UserRole_ForDocument_ReadPrivilege_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.READ, sessionToken));
    }

    // Module Frame Module Implementation Tests
    @Test
    void testValidateModuleSpecificPrivileges_UserRole_CannotAddModuleFrame_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_AdminRole_ReadModuleFrame_ShouldPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.ADMIN);

        // Act
        validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.READ, sessionToken, 1L);
    }

    // User Tests
    @Test
    void testValidateGeneralPrivileges_UserRole_ForUser_AddPrivilege_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_UserRole_ForUser_UpdatePrivilege_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.UPDATE, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_UserRole_ForUser_DeletePrivilege_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.DELETE, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_ForUser_ReadDetails_ShouldThrowInsufficientPermissionsException() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act & Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ_DETAILS, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_AdminRole_ForUsers_AllPrivileged_ShouldPass() throws InsufficientPermissionsException, NotifyException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.ADMIN);

        // Act
        // Replace this with checking READ, ADD, DELETE
        validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ, sessionToken);
        validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.ADD, sessionToken);
        validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.UPDATE, sessionToken);
        validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.USER, PRIVILEGES.DELETE, sessionToken);
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_Module_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.DELETE, sessionToken));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_SpoAdmin_ModuleFrameModuleImplementation_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_User_Spo_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.DELETE, sessionToken));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.UPDATE, sessionToken));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateGeneralPrivileges_User_ModuleFrameModuleImplementation_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateGeneralPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken));
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdmin_Module_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdmin_Document_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.ADD, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdmin_ModuleFrameModuleImplementation_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdmin_ModuleFrameModuleImplementation_ShouldPass() throws InsufficientPermissionsException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(), any())).thenReturn(true);

        // Act and Assert
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L);
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L);
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L);
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdmin_User_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdmin_SpoSettings_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdmin_SpoSettings_ShouldPass() throws InsufficientPermissionsException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(), any())).thenReturn(true);

        // Act and Assert
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.DELETE, sessionToken, 1L);
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.UPDATE, sessionToken, 1L);
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.ADD, sessionToken, 1L);
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.READ, sessionToken, 1L);
    }

    @Test
    void testValidateSpoSpecificPrivileges_SpoAdmin_GeneralSettings_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateSpoSpecificPrivileges_User_Module_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateSpoSpecificPrivileges_User_Module_ShouldPass() throws InsufficientPermissionsException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(), any())).thenReturn(true);

        // Act and Assert
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L);
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.READ, sessionToken, 1L);
    }

    @Test
    void testValidateSpoSpecificPrivileges_User_ModuleFrameModuleImplementation_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateSpoSpecificPrivileges_User_ModuleFrameModuleImplementation_Responsible_ShouldPass() throws InsufficientPermissionsException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));
        when(spoResponsibleUserRepository.existsBySpoIdAndUserId(any(), any())).thenReturn(true);

        // Act and Assert
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L);
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.READ, sessionToken, 1L);
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L);
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L);
    }

    @Test
    void testValidateSpoSpecificPrivileges_User_User() throws InsufficientPermissionsException {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.ADD, sessionToken, 1L));
        // Should pass
        validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ, sessionToken, 1L);
    }

    @Test
    void testValidateSpoSpecificPrivileges_User_GeneralSettings(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.ADD, sessionToken, 1L));
        // Should pass
        assertDoesNotThrow(() -> validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateSpoSpecificPrivileges_User_Document(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(spoRepository.findById(any())).thenReturn(Optional.of(new SpoEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.ADD, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdmin_Document_InsufficientPermissions(){
            // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.ADD, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdmin_ModuleFrameModuleImplementation_InsufficientPermissions(){
            // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdmin_ModuleFrameModuleImplementation_Responsible_ShouldPass(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken))).thenReturn(1L);
        ModuleImplementationEntity module = new ModuleImplementationEntity();
        module.setResponsible(new UserEntity());
        module.getResponsible().setId(1L);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));

        // Act and Assert
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdmin_User_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
               validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.ADD, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ_DETAILS, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdmin_SpoSettings(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.ADD, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_SpoAdmin_GeneralSettings(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.ADD, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_User_Spo(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.ADD, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_User_Document_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.ADD, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_User_GeneralSettings(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.ADD, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.GENERAL_SETTINGS, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_User_Module_ShouldPass() {
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken))).thenReturn(1L);
        ModuleImplementationEntity module = new ModuleImplementationEntity();
        module.setResponsible(new UserEntity());
        module.getResponsible().setId(1L);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));

        // Act and Assert
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.DELETE, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.ADD, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_User_Module_NotResponsibleButLecturer(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken))).thenReturn(1L);
        ModuleImplementationEntity module = new ModuleImplementationEntity();
        ModuleImplementationLecturerEntity lecturer = new ModuleImplementationLecturerEntity();
        lecturer.setLecturer(new UserEntity());
        lecturer.getLecturer().setId(1L);
        when(moduleImplementationLecturerRepository.existsById(any())).thenReturn(true);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.ADD, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.READ, sessionToken, 1L));
        assertThrows(NotifyException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE, PRIVILEGES.UPDATE, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_User_SpoSettings(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.ADD, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.SPO_SETTINGS, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_User_User(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.ADD, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ_DETAILS, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_User_ModuleFrameModuleImplementation_InsufficientPermission(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_User_ModuleFrameModuleImplementation_Responsible_ShouldPass(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken))).thenReturn(1L);
        ModuleImplementationEntity module = new ModuleImplementationEntity();
        module.setResponsible(new UserEntity());
        module.getResponsible().setId(1L);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));

        // Act and Assert
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.READ, sessionToken, 1L));
    }

    @Test
    void testValidateModuleSpecificPrivileges_User_ModuleFrameModuleImplementation_NotResponsibleButLecturer(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken))).thenReturn(1L);
        ModuleImplementationEntity module = new ModuleImplementationEntity();
        ModuleImplementationLecturerEntity lecturer = new ModuleImplementationLecturerEntity();
        lecturer.setLecturer(new UserEntity());
        lecturer.getLecturer().setId(1L);
        when(moduleImplementationLecturerRepository.existsById(any())).thenReturn(true);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.READ, sessionToken, 1L));

    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_SpoAdmin_ModuleFrameModuleImplementation_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L, 1L));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_SpoAdmin_ModuleFrameModuleImplementation_ModuleImplementationResponsible(){
        // Arrange
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken))).thenReturn(1L);
        ModuleImplementationEntity module = new ModuleImplementationEntity();
        module.setResponsible(new UserEntity());
        module.getResponsible().setId(1L);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(module));

        // Act and Assert
        assertDoesNotThrow(() -> validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.DELETE, sessionToken, 1L, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION, PRIVILEGES.ADD, sessionToken, 1L, 1L));

    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_SpoAdmin_Documents_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.DELETE, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.ADD, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.READ, sessionToken, 1L, 1L));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_SpoAdmin_User_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.SPO_ADMIN);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.DELETE, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.ADD, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ_DETAILS, sessionToken, 1L, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ, sessionToken, 1L, 1L));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_User_Documents_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.DELETE, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.ADD, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.DOCUMENT, PRIVILEGES.READ, sessionToken, 1L, 1L));
    }

    @Test
    void testValidateSpoOrModuleSpecificPrivileges_User_User_InsufficientPermissions(){
        // Arrange
        String sessionToken = UUID.randomUUID().toString();
        when(sessionService.getRoleBySessionId(UUID.fromString(sessionToken))).thenReturn(ROLE.USER);
        when(moduleImplementationRepository.findById(1L)).thenReturn(Optional.of(new ModuleImplementationEntity()));

        // Act and Assert
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.DELETE, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.UPDATE, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.ADD, sessionToken, 1L, 1L));
        assertThrows(InsufficientPermissionsException.class, () ->
                validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ_DETAILS, sessionToken, 1L, 1L));
        assertDoesNotThrow(() -> validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE.USER, PRIVILEGES.READ, sessionToken, 1L, 1L));
    }
}
