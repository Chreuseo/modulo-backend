package de.modulo.backend.repositories;

import de.modulo.backend.entities.ExamTypeModuleImplementationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamTypeModuleImplementationRepository extends JpaRepository<ExamTypeModuleImplementationEntity, ExamTypeModuleImplementationEntity.ExamTypeModuleImplementationId> {
    List<ExamTypeModuleImplementationEntity> findExamTypeModuleImplementationEntitiesByModuleImplementationId(Long moduleImplementationId);
    void deleteExamTypeModuleImplementationEntitiesByModuleImplementationId(Long moduleImplementationId);
}
