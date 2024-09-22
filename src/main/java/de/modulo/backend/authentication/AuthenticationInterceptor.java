package de.modulo.backend.authentication;

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
        String sessionToken = null;

        // Look for the "Authorization" cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("Authorization".equals(cookie.getName())) {
                    sessionToken = cookie.getValue();
                    break; // No need to continue looping once we've found the cookie
                }
            }
        }


        if (sessionToken == null) {
            response.setStatus(401);
            return false;
        }

        try {
            InetAddress ip = Inet4Address.getByName(request.getRemoteAddr());
            sessionService.validateSession(UUID.fromString(sessionToken), ip.getAddress());
        } catch ( Exception e) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
