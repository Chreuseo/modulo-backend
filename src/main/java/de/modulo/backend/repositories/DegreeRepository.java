package de.modulo.backend.repositories;

import de.modulo.backend.entities.DegreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DegreeRepository extends JpaRepository<DegreeEntity, Long> {
    Optional<DegreeEntity> findByName(String name);
}
