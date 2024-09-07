package de.modulo.backend.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.modulo.backend.converters.DegreeConverter;
import de.modulo.backend.dtos.DegreeDTO;
import de.modulo.backend.entities.DegreeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class DegreeConverterTest {

    @InjectMocks
    private DegreeConverter degreeConverter;

    @Mock
    private DegreeEntity degreeEntity;

    @Mock
    private DegreeDTO degreeDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToDtoWithNullEntity() {
        DegreeDTO result = degreeConverter.toDto(null);
        assertNull(result, "toDto should return null when input entity is null");
    }

    @Test
    public void testToDto() {
        // Given
        Long id = 1L;
        String name = "Bachelor of Science";

        DegreeEntity entity = new DegreeEntity();
        entity.setId(id);
        entity.setName(name);

        // When
        DegreeDTO result = degreeConverter.toDto(entity);

        // Then
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
    }

    @Test
    public void testToEntityWithNullDto() {
        DegreeEntity result = degreeConverter.toEntity(null);
        assertNull(result, "toEntity should return null when input DTO is null");
    }

    @Test
    public void testToEntity() {
        // Given
        Long id = 1L;
        String name = "Bachelor of Science";

        DegreeDTO dto = new DegreeDTO();
        dto.setId(id);
        dto.setName(name);

        // When
        DegreeEntity result = degreeConverter.toEntity(dto);

        // Then
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
    }
}
