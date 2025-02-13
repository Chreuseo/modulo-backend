package de.modulo.backend.services.data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.modulo.backend.converters.SectionConverter;
import de.modulo.backend.dtos.SectionDTO;
import de.modulo.backend.entities.SectionEntity;
import de.modulo.backend.repositories.SectionRepository;
import de.modulo.backend.repositories.SpoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SectionServiceTest {

    @InjectMocks
    private SectionService sectionService;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private SectionConverter sectionConverter;

    @Mock
    private SpoRepository spoRepository;

    @Test
    public void add_ShouldSaveSectionAndReturnDto() {
        // Arrange
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setName("Test Section");
        SectionEntity sectionEntity = new SectionEntity();
        sectionEntity.setId(1L);
        sectionEntity.setName("Test Section");

        when(sectionConverter.toEntity(sectionDTO)).thenReturn(sectionEntity);
        when(sectionRepository.save(any(SectionEntity.class))).thenReturn(sectionEntity);
        when(sectionConverter.toDto(sectionEntity)).thenReturn(sectionDTO);

        // Act
        SectionDTO result = sectionService.add(sectionDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Section", result.getName());
        verify(sectionRepository).save(sectionEntity);
    }

    @Test
    public void delete_SectionExists_ShouldDeleteSection() {
        // Arrange
        Long sectionId = 1L;
        when(sectionRepository.existsById(sectionId)).thenReturn(true);

        // Act
        sectionService.delete(sectionId);

        // Assert
        verify(sectionRepository).deleteById(sectionId);
    }

    @Test
    public void delete_SectionDoesNotExist_ShouldThrowException() {
        // Arrange
        Long sectionId = 1L;
        when(sectionRepository.existsById(sectionId)).thenReturn(false);

        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sectionService.delete(sectionId);
        });

        assertEquals("Section not found with id: 1", exception.getMessage());
        verify(sectionRepository, never()).deleteById(any());
    }

    @Test
    public void getBySpo_ShouldReturnListOfSectionDTO() {
        // Arrange
        Long spoId = 1L;
        SectionEntity section1 = new SectionEntity();
        section1.setId(1L);
        section1.setName("Section 1");

        SectionEntity section2 = new SectionEntity();
        section2.setId(2L);
        section2.setName("Section 2");

        List<SectionEntity> sectionEntities = Arrays.asList(section1, section2);
        when(sectionRepository.findBySpoId(spoId)).thenReturn(sectionEntities);

        SectionDTO dto1 = new SectionDTO();
        dto1.setId(1L);
        dto1.setName("Section 1");

        SectionDTO dto2 = new SectionDTO();
        dto2.setId(2L);
        dto2.setName("Section 2");

        when(sectionConverter.toDto(section1)).thenReturn(dto1);
        when(sectionConverter.toDto(section2)).thenReturn(dto2);

        // Act
        List<SectionDTO> result = sectionService.getBySpo(spoId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Section 1", result.get(0).getName());
        assertEquals("Section 2", result.get(1).getName());
        verify(sectionRepository).findBySpoId(spoId);
    }
}
