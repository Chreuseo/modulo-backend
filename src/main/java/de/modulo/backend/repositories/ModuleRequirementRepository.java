package de.modulo.backend.repositories;

import de.modulo.backend.entities.ModuleRequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRequirementRepository extends JpaRepository<ModuleRequirementEntity, Long> {
}
