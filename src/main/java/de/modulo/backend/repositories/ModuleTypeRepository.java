package de.modulo.backend.repositories;

import de.modulo.backend.entities.ModuleTypeEntity;
import de.modulo.backend.entities.SpoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleTypeRepository extends JpaRepository<ModuleTypeEntity, Long> {
    List<ModuleTypeEntity> findBySpoId(Long spoId);
}
