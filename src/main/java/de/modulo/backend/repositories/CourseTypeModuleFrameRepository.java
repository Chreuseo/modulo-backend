package de.modulo.backend.repositories;

import de.modulo.backend.entities.CourseTypeModuleFrameEntity;
import de.modulo.backend.entities.ModuleFrameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseTypeModuleFrameRepository extends JpaRepository<CourseTypeModuleFrameEntity, CourseTypeModuleFrameEntity.CourseTypeModuleFrameId> {    public List<CourseTypeModuleFrameEntity> getCourseTypeModuleFrameEntitiesByModuleFrame(ModuleFrameEntity moduleFrame);
}
