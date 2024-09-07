package de.modulo.backend.repositories;

import de.modulo.backend.entities.SectionEntity;
import de.modulo.backend.entities.SpoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<SectionEntity, Long> {
    List<SectionEntity> findBySpoId(Long spoId);
}
