package de.modulo.backend.repositories;

import de.modulo.backend.entities.ParagraphEntity;
import de.modulo.backend.entities.SpoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParagraphRepository extends JpaRepository<ParagraphEntity, Long> {
    public List<ParagraphEntity> findBySpoId(Long spoId);
}
