package de.modulo.backend.authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionTokenHelperTest {

    @InjectMocks
    private SessionTokenHelper sessionTokenHelper;

    @Mock
    private HttpServletRequest request;

    @Test
    void testGetSessionToken_WithAuthorizationCookie() {
        // Arrange
        String expectedToken = "test-token";
        Cookie authCookie = new Cookie("Authorization", expectedToken);
        Cookie[] cookies = { authCookie };
        when(request.getCookies()).thenReturn(cookies);

        // Act
        String sessionToken = sessionTokenHelper.getSessionToken(request);

        // Assert
        assertEquals(expectedToken, sessionToken);
    }

    @Test
    void testGetSessionToken_WithoutAuthorizationCookie() {
        // Arrange
        Cookie[] cookies = { new Cookie("OtherCookie", "value") };
        when(request.getCookies()).thenReturn(cookies);

        // Act
        String sessionToken = sessionTokenHelper.getSessionToken(request);

        // Assert
        assertEquals(null, sessionToken);
    }

    @Test
    void testGetSessionToken_WithNullCookies() {
        // Arrange
        when(request.getCookies()).thenReturn(null);

        // Act
        String sessionToken = sessionTokenHelper.getSessionToken(request);

        // Assert
        assertEquals(null, sessionToken);
    }

    @Test
    void testGetSessionToken_WithMultipleCookies() {
        // Arrange
        Cookie cookies[] = {
                new Cookie("OtherCookie", "value"),
                new Cookie("Authorization", "token-123"),
                new Cookie("AnotherCookie", "value")
        };
        when(request.getCookies()).thenReturn(cookies);

        // Act
        String sessionToken = sessionTokenHelper.getSessionToken(request);

        // Assert
        assertEquals("token-123", sessionToken);
    }
}
