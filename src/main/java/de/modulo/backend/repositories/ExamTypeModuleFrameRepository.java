package de.modulo.backend.repositories;

import de.modulo.backend.entities.ExamTypeModuleFrameEntity;
import de.modulo.backend.entities.ModuleFrameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamTypeModuleFrameRepository extends JpaRepository<ExamTypeModuleFrameEntity, ExamTypeModuleFrameEntity.ExamTypeModuleFrameId> {
    public List<ExamTypeModuleFrameEntity> getExamTypeModuleFrameEntitiesByModuleFrame(ModuleFrameEntity moduleFrameId);
}
