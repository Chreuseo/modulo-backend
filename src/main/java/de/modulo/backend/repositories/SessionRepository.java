package de.modulo.backend.repositories;

import de.modulo.backend.entities.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {
    void deleteAllByUserId(Long userId);
    void deleteAllByExpirationDateBefore(Long expirationDate);
}
