package de.modulo.backend.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.modulo.backend.converters.ExamTypeConverter;
import de.modulo.backend.converters.SpoConverter;
import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.dtos.SpoDTOFlat;
import de.modulo.backend.entities.ExamTypeEntity;
import de.modulo.backend.entities.SpoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ExamTypeConverterTest {

    @InjectMocks
    private ExamTypeConverter examTypeConverter;

    @Mock
    private SpoConverter spoConverter;

    private ExamTypeEntity examTypeEntity;
    private ExamTypeDTO examTypeDTO;

    @BeforeEach
    public void setUp() {
        examTypeEntity = new ExamTypeEntity();
        examTypeEntity.setId(1L);
        examTypeEntity.setName("Math Exam");
        examTypeEntity.setLength("120min");

        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(1L); // Assuming SpoEntity has an ID property
        examTypeEntity.setSpo(spoEntity);

        examTypeDTO = new ExamTypeDTO();
        examTypeDTO.setId(1L);
        examTypeDTO.setName("Math Exam");
        examTypeDTO.setLength("120min");

        SpoDTOFlat spoDTOFlat = new SpoDTOFlat();
        spoDTOFlat.setId(1L); // Assuming SpoDTOFlat has an ID property
        examTypeDTO.setSpo(spoDTOFlat);
    }

    @Test
    public void testToDto_NullEntity_ReturnsNull() {
        assertNull(examTypeConverter.toDto(null));
    }

    @Test
    public void testToDto_ValidEntity_ReturnsDto() {
        when(spoConverter.toDtoFlat(any(SpoEntity.class))).thenReturn(examTypeDTO.getSpo());

        ExamTypeDTO result = examTypeConverter.toDto(examTypeEntity);

        assertNotNull(result);
        assertEquals(examTypeEntity.getId(), result.getId());
        assertEquals(examTypeEntity.getName(), result.getName());
        assertEquals(examTypeEntity.getLength(), result.getLength());
        assertNotNull(result.getSpo());
        assertEquals(examTypeEntity.getSpo().getId(), result.getSpo().getId());
    }

    @Test
    public void testToEntity_NullDto_ReturnsNull() {
        assertNull(examTypeConverter.toEntity(null));
    }

    @Test
    public void testToEntity_ValidDto_ReturnsEntity() {
        when(spoConverter.toEntity(any(SpoDTOFlat.class))).thenReturn(examTypeEntity.getSpo());

        ExamTypeEntity result = examTypeConverter.toEntity(examTypeDTO);

        assertNotNull(result);
        assertEquals(examTypeDTO.getId(), result.getId());
        assertEquals(examTypeDTO.getName(), result.getName());
        assertEquals(examTypeDTO.getLength(), result.getLength());
        assertNotNull(result.getSpo());
        assertEquals(examTypeDTO.getSpo().getId(), result.getSpo().getId());
    }
}
