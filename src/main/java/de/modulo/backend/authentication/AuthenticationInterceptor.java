package de.modulo.backend.authentication;

import de.modulo.backend.excpetions.SessionInvalidException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.UUID;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    @Autowired
    public AuthenticationInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(1==1){
            return true;
        }

        String sessionToken = SessionTokenHelper.getSessionToken(request);

        if (sessionToken == null) {
            response.setStatus(401);
            return false;
        }

        try {
            sessionService.validateSession(UUID.fromString(sessionToken), request.getRemoteAddr());
        } catch ( SessionInvalidException e) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
