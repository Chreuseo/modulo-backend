package de.modulo.backend.repositories;

import de.modulo.backend.entities.ModuleImplementationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleImplementationRepository extends JpaRepository<ModuleImplementationEntity, Long> {
    List<ModuleImplementationEntity> getModuleImplementationEntitiesByResponsibleId(Long userId);
}
