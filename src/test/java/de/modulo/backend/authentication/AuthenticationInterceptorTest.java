package de.modulo.backend.authentication;

import de.modulo.backend.excpetions.SessionInvalidException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationInterceptorTest {

    @InjectMocks
    private AuthenticationInterceptor authenticationInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private SessionService sessionService;

    @Test
    void testPreHandle_WhenSessionTokenIsNull_ShouldReturnFalseAndSetStatus401() {
        // Arrange
        when(request.getCookies()).thenReturn(null); // No cookies

        // Act
        boolean result = authenticationInterceptor.preHandle(request, response, new Object());

        // Assert
        verify(response).setStatus(401);
        assertEquals(false, result);
    }

    @Test
    void testPreHandle_WhenSessionTokenIsPresentAndValid_ShouldReturnTrue() throws SessionInvalidException {
        // Arrange
        String validToken = UUID.randomUUID().toString();
        when(request.getCookies()).thenReturn(new Cookie[] { new Cookie("Authorization", validToken) });
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        // Act
        boolean result = authenticationInterceptor.preHandle(request, response, new Object());

        // Assert
        assertEquals(true, result);
        verify(sessionService).validateSession(UUID.fromString(validToken), "127.0.0.1");
    }

    @Test
    void testPreHandle_WhenSessionTokenIsPresentButInvalid_ShouldReturnFalseAndSetStatus401() throws SessionInvalidException {
        // Arrange
        String invalidToken = UUID.randomUUID().toString();
        when(request.getCookies()).thenReturn(new Cookie[] { new Cookie("Authorization", invalidToken) });
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        // Set up the session service to throw a SessionInvalidException when validateSession is called
        doThrow(new SessionInvalidException("Session is not valid.")).when(sessionService).validateSession(UUID.fromString(invalidToken), "127.0.0.1");

        // Act
        boolean result = authenticationInterceptor.preHandle(request, response, new Object());

        // Assert
        verify(response).setStatus(401);
        assertEquals(false, result);
    }
}
