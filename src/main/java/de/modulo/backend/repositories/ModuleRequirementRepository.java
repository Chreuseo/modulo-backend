package de.modulo.backend.repositories;

import de.modulo.backend.entities.ModuleRequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRequirementRepository extends JpaRepository<ModuleRequirementEntity, Long> {
    List<ModuleRequirementEntity> findBySpoId(long spoId);
}
