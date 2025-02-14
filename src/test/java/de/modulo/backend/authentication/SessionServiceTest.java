package de.modulo.backend.authentication;

import de.modulo.backend.entities.SessionEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InvalidPasswordException;
import de.modulo.backend.excpetions.SessionInvalidException;
import de.modulo.backend.repositories.SessionRepository;
import de.modulo.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void testDeleteSession() {
        // Arrange
        UUID sessionId = UUID.randomUUID();

        // Act
        sessionService.deleteSession(sessionId);

        // Assert
        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    void testDeleteAllSessions() {
        // Act
        sessionService.deleteAllSessions();

        // Assert
        verify(sessionRepository).deleteAll();
    }

    @Test
    void testDeleteAllSessionsByUserId() {
        // Arrange
        Long userId = 1L;

        // Act
        sessionService.deleteAllSessionsByUserId(userId);

        // Assert
        verify(sessionRepository).deleteAllByUserId(userId);
    }

    @Test
    void testLogin_WhenPasswordIsValid_ShouldReturnToken() throws InvalidPasswordException {
        // Arrange
        String userMail = "test@example.com";
        String password = "password";
        String ip = "127.0.0.1";
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(bCryptPasswordEncoder.encode(password)); // Assume the password is hashed
        userEntity.setId(1L);
        userEntity.setRole(ROLE.USER);

        when(userRepository.findByMail(userMail)).thenReturn(Optional.of(userEntity));
        when(bCryptPasswordEncoder.matches(password, userEntity.getPassword())).thenReturn(true);
        when(sessionRepository.existsById(any(UUID.class))).thenReturn(false);

        // Act
        UUID token = sessionService.login(userMail, password, ip);

        // Assert
        assertEquals(36, token.toString().length()); // Token should be a UUID
        verify(sessionRepository).save(any(SessionEntity.class));
    }

    @Test
    void testLogin_WhenPasswordIsInvalid_ShouldThrowInvalidPasswordException() {
        // Arrange
        String userMail = "test@example.com";
        String password = "wrongPassword";
        String ip = "127.0.0.1";
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(bCryptPasswordEncoder.encode("correctPassword")); // Assume the correct password is hashed

        when(userRepository.findByMail(userMail)).thenReturn(Optional.of(userEntity));
        when(bCryptPasswordEncoder.matches(password, userEntity.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> sessionService.login(userMail, password, ip));
    }

    @Test
    void testLogout() {
        // Arrange
        UUID sessionId = UUID.randomUUID();

        // Act
        sessionService.logout(sessionId);

        // Assert
        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    void testValidateSession_WhenSessionIsValid() throws SessionInvalidException {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        String ip = "127.0.0.1";
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setIp(ip);
        sessionEntity.setExpirationDate(System.currentTimeMillis() + 10000); // set expiration date in the future

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(sessionEntity));

        // Act
        sessionService.validateSession(sessionId, ip);

        // Assert
        verify(sessionRepository).save(sessionEntity);
        assertEquals(sessionEntity.getLastAccessDate(), System.currentTimeMillis(), 1000); // in the same second
    }

    @Test
    void testValidateSession_WhenSessionIsInvalid_ShouldThrowSessionInvalidException() {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        String ip = "127.0.0.1";
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SessionInvalidException.class, () -> sessionService.validateSession(sessionId, ip));
    }

    // Additional tests could follow for getUserBySessionId etc.

    @Test
    void testGetUserBySessionId() {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setUser(userEntity);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(sessionEntity));

        // Act
        UserEntity resultUser = sessionService.getUserBySessionId(sessionId);

        // Assert
        assertEquals(userEntity, resultUser);
    }

    @Test
    void testGetUserIdBySessionId() {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setUser(userEntity);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(sessionEntity));

        // Act
        Long userId = sessionService.getUserIdBySessionId(sessionId);

        // Assert
        assertEquals(1L, userId);
    }

    @Test
    void testGetRoleBySessionId() {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setRole(ROLE.ADMIN); // Example role
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setUser(userEntity);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(sessionEntity));

        // Act
        ROLE role = sessionService.getRoleBySessionId(sessionId);

        // Assert
        assertEquals(ROLE.ADMIN, role);
    }

    // Add more tests for scheduled task and edge cases as needed
}
