package de.modulo.backend.repositories;

import de.modulo.backend.entities.DurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DurationRepository extends JpaRepository<DurationEntity, Long> {
    Optional<DurationEntity> findByName(String name);
}
