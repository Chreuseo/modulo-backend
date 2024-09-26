package de.modulo.backend.authentication;

import de.modulo.backend.excpetions.SessionInvalidException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class AlreadyLoggedInInterceptor implements HandlerInterceptor {
    private final SessionService sessionService;

    @Autowired
    public AlreadyLoggedInInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String sessionToken = SessionTokenHelper.getSessionToken(request);

        if (sessionToken != null) {
            try {
                sessionService.validateSession(UUID.fromString(sessionToken), request.getRemoteAddr());
                Cookie cookie = new Cookie("Authorization", sessionToken);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24);
                response.addCookie(cookie);
                response.setStatus(200);
                return false;
            } catch (SessionInvalidException e) {
                return true;
            }
        }
        return true;
    }

}
