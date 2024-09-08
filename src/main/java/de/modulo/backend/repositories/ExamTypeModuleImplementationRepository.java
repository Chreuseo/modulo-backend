package de.modulo.backend.repositories;

import de.modulo.backend.entities.ExamTypeModuleImplementationEntity;
import de.modulo.backend.entities.ModuleImplementationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamTypeModuleImplementationRepository extends JpaRepository<ExamTypeModuleImplementationEntity, ExamTypeModuleImplementationEntity.ExamTypeModuleImplementationId> {
}
