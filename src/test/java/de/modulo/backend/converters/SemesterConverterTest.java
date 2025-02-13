package de.modulo.backend.converters;

import de.modulo.backend.dtos.SemesterDTO;
import de.modulo.backend.entities.SemesterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class SemesterConverterTest {

    @InjectMocks
    private SemesterConverter semesterConverter;

    @Test
    void testToDto() {
        // Arrange
        SemesterEntity semesterEntity = new SemesterEntity();
        semesterEntity.setId(1L);
        semesterEntity.setName("1. Semester");

        // Act
        SemesterDTO semesterDto = semesterConverter.toDTO(semesterEntity);

        // Assert
        assertNotNull(semesterDto);
        assertEquals(semesterEntity.getId(), semesterDto.getId());
        assertEquals(semesterEntity.getName(), semesterDto.getName());
    }

    @Test
    void testToEntity() {
        // Arrange
        SemesterDTO semesterDto = new SemesterDTO();
        semesterDto.setId(1L);
        semesterDto.setName("2. Semester");

        // Act
        SemesterEntity semesterEntity = semesterConverter.toEntity(semesterDto);

        // Assert
        assertNotNull(semesterEntity);
        assertEquals(semesterDto.getId(), semesterEntity.getId());
        assertEquals(semesterDto.getName(), semesterEntity.getName());
    }
}
