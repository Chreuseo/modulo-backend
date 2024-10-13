package de.modulo.backend.repositories;

import de.modulo.backend.entities.SemesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<SemesterEntity, Long> {
}
