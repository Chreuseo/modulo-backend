package de.modulo.backend.repositories;

import de.modulo.backend.entities.ExamTypeModuleFrameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamTypeModuleFrameRepository extends JpaRepository<ExamTypeModuleFrameEntity, ExamTypeModuleFrameEntity.ExamTypeModuleFrameId> {
    List<ExamTypeModuleFrameEntity> getExamTypeModuleFrameEntitiesByModuleFrameId(Long moduleFrameId);
}
