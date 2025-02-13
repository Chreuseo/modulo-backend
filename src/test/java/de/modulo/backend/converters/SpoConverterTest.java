package de.modulo.backend.converters;

import de.modulo.backend.dtos.*;
import de.modulo.backend.entities.DegreeEntity;
import de.modulo.backend.entities.SpoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpoConverterTest {

    @Mock
    DegreeConverter degreeConverter;

    @InjectMocks
    private SpoConverter spoConverter;

    @Test
    public void testToDtoFlat() {
        // Arrange
        SpoEntity entity = new SpoEntity();
        entity.setId(1L);
        entity.setName("Test Name");
        entity.setPublication(new Date());
        entity.setValidFrom(new Date());
        entity.setValidUntil(new Date());

        DegreeEntity degreeEntity = new DegreeEntity();
        degreeEntity.setId(1L);
        when(degreeConverter.toDto(any(DegreeEntity.class))).thenReturn(new DegreeDTO());

        entity.setDegree(degreeEntity);

        // Act
        SpoDTOFlat dto = spoConverter.toDtoFlat(entity);

        // Assert
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        verify(degreeConverter).toDto(degreeEntity);
    }

    @Test
    public void testToDtoFlat_WithNullEntity() {
        // Act
        SpoDTOFlat dto = spoConverter.toDtoFlat(null);

        // Assert
        assertNull(dto);
    }

    @Test
    public void testToDto() {
        // Arrange
        SpoEntity entity = new SpoEntity();
        entity.setId(2L);
        entity.setHeader("Header");
        entity.setFooter("Footer");
        entity.setName("Test Name");
        entity.setPublication(new Date());
        entity.setValidFrom(new Date());
        entity.setValidUntil(new Date());
        entity.setModuleManualIntroduction("Module Introduction");
        entity.setStudyPlanAppendix("Study Plan");

        DegreeEntity degreeEntity = new DegreeEntity();
        degreeEntity.setId(2L);
        when(degreeConverter.toDto(any(DegreeEntity.class))).thenReturn(new DegreeDTO());

        entity.setDegree(degreeEntity);

        // Act
        SpoDTO dto = spoConverter.toDto(entity);

        // Assert
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getHeader(), dto.getHeader());
        assertEquals(entity.getFooter(), dto.getFooter());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getModuleManualIntroduction(), dto.getModuleManualIntroduction());
        assertEquals(entity.getStudyPlanAppendix(), dto.getStudyPlanAppendix());
        verify(degreeConverter).toDto(degreeEntity);
    }

    @Test
    public void testToDto_WithNullEntity() {
        // Act
        SpoDTO dto = spoConverter.toDto(null);

        // Assert
        assertNull(dto);
    }

    @Test
    public void testToEntity() {
        // Arrange
        SpoDTO dto = new SpoDTO();
        dto.setId(3L);
        dto.setHeader("Header");
        dto.setFooter("Footer");
        dto.setName("Test Name");
        dto.setPublication(new Date());
        dto.setValidFrom(new Date());
        dto.setValidUntil(new Date());
        dto.setModuleManualIntroduction("Module Introduction");
        dto.setStudyPlanAppendix("Study Plan");

        DegreeDTO degreeDto = new DegreeDTO();
        degreeDto.setId(3L);
        when(degreeConverter.toEntity(any(DegreeDTO.class))).thenReturn(new DegreeEntity());

        dto.setDegree(degreeDto);

        // Act
        SpoEntity entity = spoConverter.toEntity(dto);

        // Assert
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getHeader(), entity.getHeader());
        assertEquals(dto.getFooter(), entity.getFooter());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getModuleManualIntroduction(), entity.getModuleManualIntroduction());
        assertEquals(dto.getStudyPlanAppendix(), entity.getStudyPlanAppendix());
        verify(degreeConverter).toEntity(degreeDto);
    }

    @Test
    public void testToEntity_WithNullDto() {
        // Act
        SpoEntity entity = spoConverter.toEntity((SpoDTO) null);

        // Assert
        assertNull(entity);
    }

    @Test
    void testToEntity_WithNullDtoFlat(){
        // Act
        SpoEntity entity = spoConverter.toEntity((SpoDTOFlat) null);

        // Assert
        assertNull(entity);
    }

    @Test
    public void testToEntityFromDtoFlat() {
        // Arrange
        SpoDTOFlat dtoFlat = new SpoDTOFlat();
        dtoFlat.setId(4L);
        dtoFlat.setName("Test Name");
        dtoFlat.setPublication(new Date());
        dtoFlat.setValidFrom(new Date());
        dtoFlat.setValidUntil(new Date());

        DegreeDTO degreeDto = new DegreeDTO();
        degreeDto.setId(4L);
        when(degreeConverter.toEntity(any(DegreeDTO.class))).thenReturn(new DegreeEntity());

        dtoFlat.setDegree(degreeDto);

        // Act
        SpoEntity entity = spoConverter.toEntity(dtoFlat);

        // Assert
        assertEquals(dtoFlat.getId(), entity.getId());
        assertEquals(dtoFlat.getName(), entity.getName());
        verify(degreeConverter).toEntity(degreeDto);
    }

    @Test
    public void testToEntityFromDtoFlat_WithNullDtoFlat() {
        // Act
        SpoEntity entity = spoConverter.toEntity((SpoDTOFlat) null);

        // Assert
        assertNull(entity);
    }
}
