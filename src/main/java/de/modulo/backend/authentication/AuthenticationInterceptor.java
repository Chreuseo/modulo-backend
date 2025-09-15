package de.modulo.backend.authentication;

import de.modulo.backend.excpetions.SessionInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;
import java.util.UUID;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    private static final Set<String> WHITELIST = Set.of(
            "/auth/login", "/auth/logout",
            "/api/auth/login", "/api/auth/logout",
            "/error"
    );

    @Autowired
    public AuthenticationInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String rawUri = request.getRequestURI();
        // Normalisiere führende doppelte Slashes: //auth/login -> /auth/login
        String uri = rawUri.replaceFirst("^/+/", "/");
        // Debug
        System.out.println("AuthenticationInterceptor preHandle rawURI=" + rawUri + " normalized=" + uri);

        if (WHITELIST.contains(uri)) {
            return true; // Login/Logout nicht blockieren
        }

        String sessionToken = SessionTokenHelper.getSessionToken(request);
        if (sessionToken == null) {
            response.setHeader("X-Debug-Auth","missing-token");
            response.setStatus(401);
            return false;
        }
        try {
            sessionService.validateSession(UUID.fromString(sessionToken), request.getRemoteAddr());
        } catch (SessionInvalidException | IllegalArgumentException e) {
            response.setHeader("X-Debug-Auth","invalid-session");
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
