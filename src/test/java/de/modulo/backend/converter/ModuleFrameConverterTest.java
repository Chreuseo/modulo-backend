package de.modulo.backend.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.modulo.backend.converters.*;
import de.modulo.backend.dtos.*;
import de.modulo.backend.entities.ModuleFrameEntity;
import de.modulo.backend.entities.ModuleTypeEntity;
import de.modulo.backend.entities.SectionEntity;
import de.modulo.backend.entities.SpoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ModuleFrameConverterTest {

    @InjectMocks
    private ModuleFrameConverter moduleFrameConverter;

    @Mock
    private SpoConverter spoConverter;

    @Mock
    private SectionConverter sectionConverter;

    @Mock
    private ModuleTypeConverter moduleTypeConverter;

    @Mock
    private ParagraphConverter paragraphConverter;

    private ModuleFrameEntity moduleFrameEntity;
    private ModuleFrameDTO moduleFrameDTO;

    @BeforeEach
    public void setUp() {
        moduleFrameEntity = new ModuleFrameEntity();
        moduleFrameEntity.setId(1L);
        moduleFrameEntity.setSemester(2);
        moduleFrameEntity.setQuantity(30);
        moduleFrameEntity.setName("Math Module Frame");
        moduleFrameEntity.setSws(4);
        moduleFrameEntity.setCourseType("Regular");
        moduleFrameEntity.setWeight(10);
        moduleFrameEntity.setCredits(5);
        moduleFrameEntity.setAllExamsMandatory(true);

        SpoEntity spoEntity = new SpoEntity();
        moduleFrameEntity.setSpo(spoEntity);

        SectionEntity sectionEntity = new SectionEntity();
        moduleFrameEntity.setSection(sectionEntity);

        ModuleTypeEntity moduleTypeEntity = new ModuleTypeEntity();
        moduleFrameEntity.setModuleType(moduleTypeEntity);

        moduleFrameDTO = new ModuleFrameDTO();
        moduleFrameDTO.setId(1L);
        moduleFrameDTO.setSemester(2);
        moduleFrameDTO.setQuantity(30);
        moduleFrameDTO.setName("Math Module Frame");
        moduleFrameDTO.setSws(4);
        moduleFrameDTO.setCourseType("Regular");
        moduleFrameDTO.setWeight(10);
        moduleFrameDTO.setCredits(5);
        moduleFrameDTO.setAllExamsMandatory(true);

        SpoDTOFlat spoDTO = new SpoDTOFlat();
        moduleFrameDTO.setSpo(spoDTO);

        SectionDTO sectionDTO = new SectionDTO();
        moduleFrameDTO.setSection(sectionDTO);

        ModuleTypeDTO moduleTypeDTO = new ModuleTypeDTO();
        moduleFrameDTO.setModuleType(moduleTypeDTO);
    }

    @Test
    public void testToDto_NullEntity_ReturnsNull() {
        assertNull(moduleFrameConverter.toDto(null));
    }

    @Test
    public void testToDto_ValidEntity_ReturnsDto() {
        when(spoConverter.toDtoFlat(any(SpoEntity.class))).thenReturn(moduleFrameDTO.getSpo());
        when(sectionConverter.toDto(any(SectionEntity.class))).thenReturn(moduleFrameDTO.getSection());
        when(moduleTypeConverter.toDto(any(ModuleTypeEntity.class))).thenReturn(moduleFrameDTO.getModuleType());

        ModuleFrameDTO result = moduleFrameConverter.toDto(moduleFrameEntity);

        assertNotNull(result);
        assertEquals(moduleFrameEntity.getId(), result.getId());
        assertEquals(moduleFrameEntity.getSemester(), result.getSemester());
        assertEquals(moduleFrameEntity.getQuantity(), result.getQuantity());
        assertEquals(moduleFrameEntity.getName(), result.getName());
        assertEquals(moduleFrameEntity.getSws(), result.getSws());
        assertEquals(moduleFrameEntity.getCourseType(), result.getCourseType());
        assertEquals(moduleFrameEntity.getWeight(), result.getWeight());
        assertEquals(moduleFrameEntity.getCredits(), result.getCredits());
        assertEquals(moduleFrameEntity.isAllExamsMandatory(), result.isAllExamsMandatory());

        assertNotNull(result.getSpo());
        assertNotNull(result.getSection());
        assertNotNull(result.getModuleType());
    }

    @Test
    public void testToEntity_NullDto_ReturnsNull() {
        assertNull(moduleFrameConverter.toEntity(null));
    }

    @Test
    public void testToEntity_ValidDto_ReturnsEntity() {
        when(spoConverter.toEntity(any(SpoDTO.class))).thenReturn(moduleFrameEntity.getSpo());
        when(sectionConverter.toEntity(any(SectionDTO.class))).thenReturn(moduleFrameEntity.getSection());
        when(moduleTypeConverter.toEntity(any(ModuleTypeDTO.class))).thenReturn(moduleFrameEntity.getModuleType());

        ModuleFrameEntity result = moduleFrameConverter.toEntity(moduleFrameDTO);

        assertNotNull(result);
        assertEquals(moduleFrameDTO.getId(), result.getId());
        assertEquals(moduleFrameDTO.getSemester(), result.getSemester());
        assertEquals(moduleFrameDTO.getQuantity(), result.getQuantity());
        assertEquals(moduleFrameDTO.getName(), result.getName());
        assertEquals(moduleFrameDTO.getSws(), result.getSws());
        assertEquals(moduleFrameDTO.getCourseType(), result.getCourseType());
        assertEquals(moduleFrameDTO.getWeight(), result.getWeight());
        assertEquals(moduleFrameDTO.getCredits(), result.getCredits());
        assertEquals(moduleFrameDTO.isAllExamsMandatory(), result.isAllExamsMandatory());

        assertNotNull(result.getSpo());
        assertNotNull(result.getSection());
        assertNotNull(result.getModuleType());
    }
}
