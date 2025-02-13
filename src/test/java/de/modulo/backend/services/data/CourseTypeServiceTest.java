package de.modulo.backend.services.data;

import de.modulo.backend.converters.CourseTypeConverter;
import de.modulo.backend.dtos.CourseTypeDTO;
import de.modulo.backend.entities.CourseTypeEntity;
import de.modulo.backend.repositories.CourseTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseTypeServiceTest {

    @InjectMocks
    private CourseTypeService courseTypeService;

    @Mock
    private CourseTypeRepository courseTypeRepository;

    @Mock
    private CourseTypeConverter courseTypeConverter;

    @Test
    void testGetAll() {
        // Arrange
        CourseTypeEntity entity1 = new CourseTypeEntity();
        entity1.setId(1L);
        entity1.setName("Course Type 1");
        entity1.setAbbreviation("CT1");

        CourseTypeEntity entity2 = new CourseTypeEntity();
        entity2.setId(2L);
        entity2.setName("Course Type 2");
        entity2.setAbbreviation("CT2");

        when(courseTypeRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        CourseTypeDTO dto1 = new CourseTypeDTO();
        dto1.setId(1L);
        dto1.setName("Course Type 1");
        dto1.setAbbreviation("CT1");

        CourseTypeDTO dto2 = new CourseTypeDTO();
        dto2.setId(2L);
        dto2.setName("Course Type 2");
        dto2.setAbbreviation("CT2");

        when(courseTypeConverter.toDto(entity1)).thenReturn(dto1);
        when(courseTypeConverter.toDto(entity2)).thenReturn(dto2);

        // Act
        var result = courseTypeService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Course Type 1", result.get(0).getName());
        assertEquals("Course Type 2", result.get(1).getName());
    }

    @Test
    void testAdd() {
        // Arrange
        CourseTypeDTO courseTypeDTO = new CourseTypeDTO();
        courseTypeDTO.setName("New Course Type");
        courseTypeDTO.setAbbreviation("NCT");

        CourseTypeEntity courseTypeEntity = new CourseTypeEntity();
        courseTypeEntity.setName(courseTypeDTO.getName());
        courseTypeEntity.setAbbreviation(courseTypeDTO.getAbbreviation());

        CourseTypeEntity savedEntity = new CourseTypeEntity();
        savedEntity.setId(1L);
        savedEntity.setName(courseTypeDTO.getName());
        savedEntity.setAbbreviation(courseTypeDTO.getAbbreviation());

        when(courseTypeConverter.toEntity(courseTypeDTO)).thenReturn(courseTypeEntity);
        when(courseTypeRepository.save(courseTypeEntity)).thenReturn(savedEntity);
        when(courseTypeConverter.toDto(savedEntity)).thenReturn(courseTypeDTO);

        // Act
        CourseTypeDTO result = courseTypeService.add(courseTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Course Type", result.getName());
        verify(courseTypeRepository).save(any(CourseTypeEntity.class));
    }

    @Test
    void testDelete() {
        // Arrange
        CourseTypeEntity courseTypeEntity = new CourseTypeEntity();
        courseTypeEntity.setId(1L);
        when(courseTypeRepository.findById(1L)).thenReturn(Optional.of(courseTypeEntity));

        // Act
        courseTypeService.delete(1L);

        // Assert
        verify(courseTypeRepository).delete(courseTypeEntity);
    }

    @Test
    void testDeleteNotFound() {
        // Arrange
        when(courseTypeRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            courseTypeService.delete(2L);
        });

        assertEquals("Course Type not found with id: 2", exception.getMessage());
    }
}
