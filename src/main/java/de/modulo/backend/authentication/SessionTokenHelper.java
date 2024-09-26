package de.modulo.backend.authentication;

import jakarta.servlet.http.HttpServletRequest;

public class SessionTokenHelper {
    public static String getSessionToken(HttpServletRequest request) {
        String sessionToken = null;

        // Look for the "Authorization" cookie
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("Authorization".equals(cookie.getName())) {
                    sessionToken = cookie.getValue();
                    break; // No need to continue looping once we've found the cookie
                }
            }
        }

        return sessionToken;
    }
}
