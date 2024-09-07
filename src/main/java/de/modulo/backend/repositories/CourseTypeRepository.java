package de.modulo.backend.repositories;

import de.modulo.backend.entities.CourseTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseTypeRepository extends JpaRepository<CourseTypeEntity, Long> {
}
