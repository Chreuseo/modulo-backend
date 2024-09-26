package de.modulo.backend.authentication;

import de.modulo.backend.entities.SessionEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InvalidPasswordException;
import de.modulo.backend.excpetions.SessionInvalidException;
import de.modulo.backend.repositories.SessionRepository;
import de.modulo.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void deleteSession(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    public void deleteAllSessions() {
        sessionRepository.deleteAll();
    }

    public void deleteAllSessionsByUserId(Long userId) {
        sessionRepository.deleteAllByUserId(userId);
    }

    public UUID login(String userMail,
                         String password,
                         String ip) throws InvalidPasswordException {
        SessionEntity session = new SessionEntity();
        UUID token = UUID.randomUUID();
        while(sessionRepository.existsById(token)) {
            token = UUID.randomUUID();
        }
        session.setToken(token);
        UserEntity user = userRepository.findByMail(userMail).orElseThrow();
        session.setUser(user);
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Password is invalid");
        }
        session.setCreationDate(System.currentTimeMillis());
        session.setLastAccessDate(System.currentTimeMillis());
        session.setExpirationDate(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        session.setIp(ip);
        sessionRepository.save(session);

        return token;
    }

    public void logout(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    private boolean isSessionValid(UUID sessionId,
                                  String ip) {
        SessionEntity session = sessionRepository.findById(sessionId).orElse(null);
        if(session == null) {
            return false;
        }
        if(session.getExpirationDate() < System.currentTimeMillis()) {
            sessionRepository.deleteById(sessionId);
            return false;
        }
        if(!session.getIp().equals(ip)) {
            System.out.println("IP is not equal: " + session.getIp() + " != " + ip);
            sessionRepository.deleteById(sessionId);
            return false;
        }
        return true;
    }

    public void validateSession(UUID sessionId,
                           String ip) throws SessionInvalidException {
        if(!isSessionValid(sessionId, ip)) {
            throw new SessionInvalidException("Session is not valid");
        }else{
            SessionEntity session = sessionRepository.findById(sessionId).orElseThrow();
            session.setLastAccessDate(System.currentTimeMillis());
            session.setExpirationDate(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
            sessionRepository.save(session);
        }
    }

    public UserEntity getUserBySessionId(UUID sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow().getUser();
    }

    public ROLE getRoleBySessionId(UUID sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow().getUser().getRole();
    }

    @Scheduled(cron = "0 0 3 * * SUN")
    public void deleteExpiredSessions() {
        sessionRepository.deleteAllByExpirationDateBefore(System.currentTimeMillis());
    }
}
