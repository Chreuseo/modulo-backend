package de.modulo.backend.converters;

import de.modulo.backend.dtos.*;
import de.modulo.backend.entities.*;
import de.modulo.backend.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleFrameConverterTest {

    @Mock
    SectionConverter sectionConverter;
    @Mock
    ModuleTypeConverter moduleTypeConverter;
    @Mock
    CourseTypeConverter courseTypeConverter;
    @Mock
    ExamTypeConverter examTypeConverter;
    @Mock
    SpoConverter spoConverter;

    @Mock
    SpoRepository spoRepository;
    @Mock
    SectionRepository sectionRepository;
    @Mock
    ModuleTypeRepository moduleTypeRepository;
    @Mock
    CourseTypeModuleFrameRepository courseTypeModuleFrameRepository;
    @Mock
    ExamTypeModuleFrameRepository examTypeModuleFrameRepository;

    @InjectMocks
    ModuleFrameConverter moduleFrameConverter;

    @Test
    public void testToDto_NullEntity() {
        ModuleFrameDTO dto = moduleFrameConverter.toDto(null);
        assertNull(dto);
    }

    @Test
    public void testToDto_ValidEntity() {
        // Arrange
        ModuleFrameEntity entity = new ModuleFrameEntity();
        entity.setId(1L);
        entity.setQuantity(3);
        entity.setName("ModuleFrame");
        entity.setSws(4);
        entity.setWeight(5);
        entity.setCredits(6);

        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(1L);
        spoEntity.setName("SPO Name");
        entity.setSpo(spoEntity);

        SectionEntity sectionEntity = new SectionEntity();
        sectionEntity.setId(2L);
        entity.setSection(sectionEntity);

        ModuleTypeEntity moduleTypeEntity = new ModuleTypeEntity();
        moduleTypeEntity.setId(3L);
        entity.setModuleType(moduleTypeEntity);



        when(spoConverter.toDtoFlat(spoEntity)).thenReturn(new SpoDTOFlat());
        when(sectionConverter.toDto(any())).thenReturn(new SectionDTO());
        when(moduleTypeConverter.toDto(any())).thenReturn(new ModuleTypeDTO());

        // Act
        ModuleFrameDTO dto = moduleFrameConverter.toDto(entity);

        // Assert
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getQuantity(), dto.getQuantity());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getSws(), dto.getSws());
        assertEquals(entity.getWeight(), dto.getWeight());
        assertEquals(entity.getCredits(), dto.getCredits());
    }

    @Test
    public void testToDto_ValidEntity_nullValues() {
        // Arrange
        ModuleFrameEntity entity = new ModuleFrameEntity();
        entity.setId(1L);
        entity.setQuantity(3);
        entity.setName("ModuleFrame");
        entity.setSws(4);
        entity.setWeight(5);
        entity.setCredits(6);

        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(1L);
        spoEntity.setName("SPO Name");
        entity.setSpo(spoEntity);


        when(spoConverter.toDtoFlat(spoEntity)).thenReturn(new SpoDTOFlat());

        // Act
        ModuleFrameDTO dto = moduleFrameConverter.toDto(entity);

        // Assert
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getQuantity(), dto.getQuantity());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getSws(), dto.getSws());
        assertEquals(entity.getWeight(), dto.getWeight());
        assertEquals(entity.getCredits(), dto.getCredits());
    }

    @Test
    public void testToEntity_NullDto() {
        ModuleFrameEntity entity = moduleFrameConverter.toEntity(null);
        assertNull(entity);
    }

    @Test
    public void testToEntity_ValidDto() {
        // Arrange
        ModuleFrameDTO dto = new ModuleFrameDTO();
        dto.setId(1L);
        dto.setQuantity(3);
        dto.setName("ModuleFrame");
        dto.setSws(4);
        dto.setWeight(5);
        dto.setCredits(6);

        SpoDTOFlat spoDTOFlat = new SpoDTOFlat();
        spoDTOFlat.setId(1L);
        dto.setSpoDTOFlat(spoDTOFlat);

        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(2L);
        dto.setSection(sectionDTO);

        ModuleTypeDTO moduleTypeDTO = new ModuleTypeDTO();
        moduleTypeDTO.setId(3L);
        dto.setModuleType(moduleTypeDTO);

        when(spoRepository.findById(spoDTOFlat.getId())).thenReturn(Optional.of(new SpoEntity()));
        when(sectionRepository.findById(sectionDTO.getId())).thenReturn(Optional.of(new SectionEntity()));
        when(moduleTypeRepository.findById(moduleTypeDTO.getId())).thenReturn(Optional.of(new ModuleTypeEntity()));

        // Act
        ModuleFrameEntity entity = moduleFrameConverter.toEntity(dto);

        // Assert
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getQuantity(), entity.getQuantity());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getSws(), entity.getSws());
        assertEquals(dto.getWeight(), entity.getWeight());
        assertEquals(dto.getCredits(), entity.getCredits());
    }

    @Test
    public void testToEntity_ValidDto_nullValues() {
        // Arrange
        ModuleFrameDTO dto = new ModuleFrameDTO();
        dto.setId(1L);
        dto.setQuantity(3);
        dto.setName("ModuleFrame");
        dto.setSws(4);
        dto.setWeight(5);
        dto.setCredits(6);

        SpoDTOFlat spoDTOFlat = new SpoDTOFlat();
        spoDTOFlat.setId(1L);
        dto.setSpoDTOFlat(spoDTOFlat);

        when(spoRepository.findById(spoDTOFlat.getId())).thenReturn(Optional.of(new SpoEntity()));

        // Act
        ModuleFrameEntity entity = moduleFrameConverter.toEntity(dto);

        // Assert
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getQuantity(), entity.getQuantity());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getSws(), entity.getSws());
        assertEquals(dto.getWeight(), entity.getWeight());
        assertEquals(dto.getCredits(), entity.getCredits());
    }
}
