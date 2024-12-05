package de.modulo.backend.repositories;

import de.modulo.backend.entities.ModuleImplementationLecturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleImplementationLecturerRepository extends JpaRepository<ModuleImplementationLecturerEntity, ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId> {
    List<ModuleImplementationLecturerEntity> getModuleImplementationLecturerEntitiesByModuleImplementationId(Long moduleImplementationId);
    List<ModuleImplementationLecturerEntity> getModuleImplementationLecturerEntitiesByLecturerId(Long lecturerId);
    void deleteModuleImplementationLecturerEntitiesByLecturerId(Long lecturerId);
    void deleteModuleImplementationLecturerEntitiesByModuleImplementationId(Long moduleImplementationId);
}
