package de.modulo.backend.converters;

import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.entities.ExamTypeEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.SpoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExamTypeConverterTest {

    @Mock
    SpoRepository spoRepository;

    @InjectMocks
    private ExamTypeConverter examTypeConverter;

    @Test
    void testToDto() {
        // Arrange
        ExamTypeEntity examTypeEntity = new ExamTypeEntity();
        examTypeEntity.setId(1L);
        examTypeEntity.setName("Klausur");
        examTypeEntity.setAbbreviation("schrP");
        examTypeEntity.setLength("90 - 120 Minuten");

        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(2L);
        spoEntity.setName("SPO 2020");
        examTypeEntity.setSpo(spoEntity);

        // Act
        ExamTypeDTO examTypeDto = examTypeConverter.toDto(examTypeEntity);

        // Assert
        assertNotNull(examTypeDto);
        assertEquals(examTypeEntity.getId(), examTypeDto.getId());
        assertEquals(examTypeEntity.getName(), examTypeDto.getName());
        assertEquals(examTypeEntity.getAbbreviation(), examTypeDto.getAbbreviation());
        assertEquals(examTypeEntity.getLength(), examTypeDto.getLength());
        assertEquals(examTypeEntity.getSpo().getId(), examTypeDto.getSpoId());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        ExamTypeDTO examTypeDto = examTypeConverter.toDto(null);

        // Assert
        assertNull(examTypeDto);
    }

    @Test
    void testToEntity() {
        // Arrange
        ExamTypeDTO examTypeDto = new ExamTypeDTO();
        examTypeDto.setId(1L);
        examTypeDto.setName("Klausur");
        examTypeDto.setAbbreviation("schrP");
        examTypeDto.setLength("90 - 120 Minuten");

        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(2L);
        spoEntity.setName("SPO 2020");
        examTypeDto.setSpoId(spoEntity.getId());

        when(spoRepository.findById(spoEntity.getId())).thenReturn(java.util.Optional.of(spoEntity));

        // Act
        ExamTypeEntity examTypeEntity = examTypeConverter.toEntity(examTypeDto);

        // Assert
        assertNotNull(examTypeEntity);
        assertEquals(examTypeDto.getId(), examTypeEntity.getId());
        assertEquals(examTypeDto.getName(), examTypeEntity.getName());
        assertEquals(examTypeDto.getAbbreviation(), examTypeEntity.getAbbreviation());
        assertEquals(examTypeDto.getLength(), examTypeEntity.getLength());
        assertEquals(examTypeDto.getSpoId(), examTypeEntity.getSpo().getId());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        ExamTypeEntity examTypeEntity = examTypeConverter.toEntity(null);

        // Assert
        assertNull(examTypeEntity);
    }
}
