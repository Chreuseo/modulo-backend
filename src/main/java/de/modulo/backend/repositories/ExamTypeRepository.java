package de.modulo.backend.repositories;

import de.modulo.backend.entities.ExamTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamTypeRepository extends JpaRepository<ExamTypeEntity, Long> {
    List<ExamTypeEntity> findBySpoId(long spoId);
}
