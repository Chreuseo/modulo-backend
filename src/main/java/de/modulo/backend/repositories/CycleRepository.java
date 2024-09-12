package de.modulo.backend.repositories;

import de.modulo.backend.entities.CycleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<CycleEntity, Long> {
}
