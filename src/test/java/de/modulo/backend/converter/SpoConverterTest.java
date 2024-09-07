package de.modulo.backend.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import de.modulo.backend.converters.*;
import de.modulo.backend.dtos.*;
import de.modulo.backend.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class SpoConverterTest {

    @InjectMocks
    private SpoConverter spoConverter;

    @Mock
    private ParagraphConverter paragraphConverter;

    @Mock
    private SectionConverter sectionConverter;

    @Mock
    private ModuleTypeConverter moduleTypeConverter;

    @Mock
    private DegreeConverter degreeConverter;

    @Mock
    private ParagraphEntity startingParagraph;

    @Mock
    private SectionEntity startingSection;

    @Mock
    private ModuleTypeEntity startingModuleType;

    @Mock
    private DegreeEntity degreeEntity;

    @Mock
    private SpoDTO dto;

    @Mock
    private SpoEntity entity;

    @Mock
    private ParagraphDTO startingParagraphDTO;

    @Mock
    private SectionDTO startingSectionDTO;

    @Mock
    private ModuleTypeDTO startingModuleTypeDTO;

    @Mock
    private DegreeDTO degreeDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToDtoWithNullEntity() {
        SpoDTO result = spoConverter.toDto(null);
        assertNull(result, "toDto should return null when input entity is null");
    }

    @Test
    public void testToDto() {
        // Given
        long id = 1L;
        String header = "Header";
        String footer = "Footer";
        String name = "Spo Name";
        Date publicationDate = new Date();

        when(entity.getId()).thenReturn(id);
        when(entity.getHeader()).thenReturn(header);
        when(entity.getFooter()).thenReturn(footer);
        when(entity.getName()).thenReturn(name);
        when(entity.getPublication()).thenReturn(publicationDate);
        when(entity.getStartingSection()).thenReturn(startingSection);
        when(entity.getStartingModuleType()).thenReturn(startingModuleType);
        when(entity.getDegree()).thenReturn(degreeEntity);

        when(paragraphConverter.toDto(startingParagraph)).thenReturn(startingParagraphDTO);
        when(sectionConverter.toDto(startingSection)).thenReturn(startingSectionDTO);
        when(moduleTypeConverter.toDto(startingModuleType)).thenReturn(startingModuleTypeDTO);
        when(degreeConverter.toDto(degreeEntity)).thenReturn(degreeDTO);

        // When
        SpoDTO result = spoConverter.toDto(entity);

        // Then
        assertEquals(id, result.getId());
        assertEquals(header, result.getHeader());
        assertEquals(footer, result.getFooter());
        assertEquals(name, result.getName());
        assertEquals(publicationDate, result.getPublication());

        assertEquals(degreeDTO, result.getDegree());
    }

    @Test
    public void testToEntityWithNullDto() {
        SpoEntity result = spoConverter.toEntity((SpoDTO) null);
        assertNull(result, "toEntity should return null when input DTO is null");
    }

    @Test
    public void testToEntityWithNullDtoFlat() {
        SpoEntity result = spoConverter.toEntity((SpoDTOFlat) null);
        assertNull(result, "toEntity should return null when input DTO is null");
    }


    @Test
    public void testToEntity() {
        // Given
        long id = 1L;
        String header = "Header";
        String footer = "Footer";
        String name = "Spo Name";
        Date publicationDate = new Date();

        when(dto.getId()).thenReturn(id);
        when(dto.getHeader()).thenReturn(header);
        when(dto.getFooter()).thenReturn(footer);
        when(dto.getName()).thenReturn(name);
        when(dto.getPublication()).thenReturn(publicationDate);
        when(paragraphConverter.toEntity(startingParagraphDTO)).thenReturn(startingParagraph);
        when(sectionConverter.toEntity(startingSectionDTO)).thenReturn(startingSection);
        when(moduleTypeConverter.toEntity(startingModuleTypeDTO)).thenReturn(startingModuleType);
        when(degreeConverter.toEntity(degreeDTO)).thenReturn(degreeEntity);

        // When
        SpoEntity result = spoConverter.toEntity(dto);

        // Then
        assertEquals(id, result.getId());
        assertEquals(header, result.getHeader());
        assertEquals(footer, result.getFooter());
        assertEquals(name, result.getName());
        assertEquals(publicationDate, result.getPublication());
        //assertEquals(startingParagraph, result.getStartingParagraph());
        //assertEquals(startingSection, result.getStartingSection());
        //assertEquals(startingModuleType, result.getStartingModuleType());
        //assertEquals(degreeEntity, result.getDegree());
    }
}
