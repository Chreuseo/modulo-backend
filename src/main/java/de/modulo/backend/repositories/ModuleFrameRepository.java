package de.modulo.backend.repositories;

import de.modulo.backend.entities.ModuleFrameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleFrameRepository extends JpaRepository<ModuleFrameEntity, Long> {
    public List<ModuleFrameEntity> findBySectionIdAndModuleTypeId(Long sectionId, Long moduleTypeId);
}
