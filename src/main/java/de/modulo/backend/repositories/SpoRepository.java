package de.modulo.backend.repositories;

import de.modulo.backend.entities.SpoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpoRepository extends JpaRepository<SpoEntity, Long> {
}
