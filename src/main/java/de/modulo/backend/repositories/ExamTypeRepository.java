package de.modulo.backend.repositories;

import de.modulo.backend.entities.ExamTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamTypeRepository extends JpaRepository<ExamTypeEntity, Long> {
}
