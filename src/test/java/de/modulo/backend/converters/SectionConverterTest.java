package de.modulo.backend.converters;

import de.modulo.backend.dtos.SectionDTO;
import de.modulo.backend.entities.SectionEntity;
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
public class SectionConverterTest {

    @Mock
    SpoRepository spoRepository;

    @InjectMocks
    private SectionConverter sectionConverter;

    @Test
    void testToDto() {
        // Arrange
        SectionEntity sectionEntity = new SectionEntity();
        sectionEntity.setId(1L);
        sectionEntity.setName("Section");
        sectionEntity.setOrderNumber(1);

        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(2L);
        spoEntity.setName("SPO 2020");
        sectionEntity.setSpo(spoEntity);

        // Act
        SectionDTO sectionDto = sectionConverter.toDto(sectionEntity);

        // Assert
        assertNotNull(sectionDto);
        assertEquals(sectionEntity.getId(), sectionDto.getId());
        assertEquals(sectionEntity.getName(), sectionDto.getName());
        assertEquals(sectionEntity.getSpo().getId(), sectionDto.getSpoId());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        SectionDTO sectionDto = sectionConverter.toDto(null);

        // Assert
        assertNull(sectionDto);
    }

    @Test
    void testToEntity() {
        // Arrange
        SectionDTO sectionDto = new SectionDTO();
        sectionDto.setId(1L);
        sectionDto.setName("Section");
        sectionDto.setSpoId(2L);

        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(2L);
        spoEntity.setName("SPO 2020");

        when(spoRepository.findById(2L)).thenReturn(java.util.Optional.of(spoEntity));

        // Act
        SectionEntity sectionEntity = sectionConverter.toEntity(sectionDto);

        // Assert
        assertNotNull(sectionEntity);
        assertEquals(sectionDto.getId(), sectionEntity.getId());
        assertEquals(sectionDto.getName(), sectionEntity.getName());
        assertEquals(sectionDto.getSpoId(), sectionEntity.getSpo().getId());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        SectionEntity sectionEntity = sectionConverter.toEntity(null);

        // Assert
        assertNull(sectionEntity);
    }
}
