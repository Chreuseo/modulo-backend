package de.modulo.backend.authentication;

import de.modulo.backend.dtos.UserDTOAuth;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SessionService sessionService;

    @Autowired
    public AuthController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTOAuth userDTOAuth, HttpServletRequest request, HttpServletResponse response) {
        try{
            Cookie cookie = new Cookie("Authorization", sessionService.login(userDTOAuth.getMail(),
                    userDTOAuth.getPassword(),
                    request.getRemoteAddr()).toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);
            return ResponseEntity.ok().body("Login successful");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
