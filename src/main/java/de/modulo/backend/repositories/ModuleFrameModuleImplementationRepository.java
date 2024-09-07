package de.modulo.backend.repositories;

import de.modulo.backend.entities.ModuleFrameModuleImplementationEntity;
import de.modulo.backend.entities.ModuleImplementationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleFrameModuleImplementationRepository extends JpaRepository<ModuleFrameModuleImplementationEntity, Long> {
    public List<ModuleFrameModuleImplementationEntity> getModuleFrameModuleImplementationEntitiesByModuleImplementation(ModuleImplementationEntity moduleImplementation);
}
