package de.modulo.backend.repositories;

import de.modulo.backend.entities.MaternityProtectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaternityProtectionRepository extends JpaRepository<MaternityProtectionEntity, Long> {
    Optional<MaternityProtectionEntity> findByName(String name);
}
