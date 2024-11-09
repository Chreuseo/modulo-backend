package de.modulo.backend.services.data;

import de.modulo.backend.converters.CourseTypeConverter;
import de.modulo.backend.dtos.CourseTypeDTO;
import de.modulo.backend.entities.CourseTypeEntity;
import de.modulo.backend.repositories.CourseTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseTypeService {

    private final CourseTypeConverter courseTypeConverter;
    private final CourseTypeRepository courseTypeRepository; // Assuming you have a JPA repository for CourseTypeEntity

    @Autowired
    public CourseTypeService(CourseTypeConverter courseTypeConverter, CourseTypeRepository courseTypeRepository) {
        this.courseTypeConverter = courseTypeConverter;
        this.courseTypeRepository = courseTypeRepository;
    }

    // Get all Course Types
    public List<CourseTypeDTO> getAll() {
        return courseTypeRepository.findAll()
                .stream()
                .map(courseTypeConverter::toDto)
                .collect(Collectors.toList());
    }

    // Add a new Course Type
    public CourseTypeDTO add(CourseTypeDTO courseTypeDTO) {
        CourseTypeEntity courseTypeEntity = courseTypeConverter.toEntity(courseTypeDTO);
        CourseTypeEntity savedEntity = courseTypeRepository.save(courseTypeEntity);
        return courseTypeConverter.toDto(savedEntity);
    }

    // Delete Course Type by ID
    public void delete(Long id) {
        Optional<CourseTypeEntity> courseTypeEntity = courseTypeRepository.findById(id);
        if (courseTypeEntity.isPresent()) {
            courseTypeRepository.delete(courseTypeEntity.get());
        } else {
            throw new IllegalArgumentException("Course Type not found with id: " + id);
        }
    }
}
