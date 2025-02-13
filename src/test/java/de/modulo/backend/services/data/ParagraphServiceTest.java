package de.modulo.backend.services.data;

import de.modulo.backend.converters.ParagraphConverter;
import de.modulo.backend.dtos.ParagraphDTO;
import de.modulo.backend.entities.ParagraphEntity;
import de.modulo.backend.repositories.ParagraphRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParagraphServiceTest {

    @Mock
    private ParagraphRepository paragraphRepository;

    @Mock
    private ParagraphConverter paragraphConverter;

    @InjectMocks
    private ParagraphService paragraphService;

    @Test
    public void testDeleteParagraph() {
        Long paragraphId = 1L;

        paragraphService.deleteParagraph(paragraphId);

        verify(paragraphRepository, times(1)).deleteById(paragraphId);
    }

    @Test
    public void testAddParagraph() {
        ParagraphDTO paragraphDTO = new ParagraphDTO();
        ParagraphEntity paragraphEntity = new ParagraphEntity();
        paragraphDTO.setId(1L);
        paragraphDTO.setTitle("Test Title");
        paragraphDTO.setText("Test Text");
        paragraphDTO.setSpoId(1L);

        when(paragraphConverter.toEntity(paragraphDTO)).thenReturn(paragraphEntity);
        when(paragraphRepository.save(paragraphEntity)).thenReturn(paragraphEntity);
        when(paragraphConverter.toDto(paragraphEntity)).thenReturn(paragraphDTO);

        ParagraphDTO result = paragraphService.addParagraph(paragraphDTO);

        assertThat(result).isEqualTo(paragraphDTO);
        verify(paragraphConverter, times(1)).toEntity(paragraphDTO);
        verify(paragraphRepository, times(1)).save(paragraphEntity);
        verify(paragraphConverter, times(1)).toDto(paragraphEntity);
    }

    @Test
    public void testUpdateParagraph() {
        ParagraphDTO paragraphDTO = new ParagraphDTO();
        paragraphDTO.setId(1L);
        paragraphDTO.setTitle("Updated Title");
        paragraphDTO.setText("Updated Text");
        paragraphDTO.setSpoId(1L);

        ParagraphEntity paragraphEntity = new ParagraphEntity();
        paragraphEntity.setId(1L);

        when(paragraphRepository.existsById(paragraphDTO.getId())).thenReturn(true);
        when(paragraphConverter.toEntity(paragraphDTO)).thenReturn(paragraphEntity);
        when(paragraphRepository.save(paragraphEntity)).thenReturn(paragraphEntity);
        when(paragraphConverter.toDto(paragraphEntity)).thenReturn(paragraphDTO);

        ParagraphDTO result = paragraphService.updateParagraph(paragraphDTO);

        assertThat(result).isEqualTo(paragraphDTO);
        verify(paragraphRepository, times(1)).existsById(paragraphDTO.getId());
        verify(paragraphConverter, times(1)).toEntity(paragraphDTO);
        verify(paragraphRepository, times(1)).save(paragraphEntity);
        verify(paragraphConverter, times(1)).toDto(paragraphEntity);
    }

    @Test
    public void testUpdateParagraph_NonExistentId() {
        ParagraphDTO paragraphDTO = new ParagraphDTO();
        paragraphDTO.setId(99L);

        when(paragraphRepository.existsById(paragraphDTO.getId())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paragraphService.updateParagraph(paragraphDTO);
        });

        assertThat(exception.getMessage()).isEqualTo("Paragraph not found with id: 99");
        verify(paragraphRepository, times(1)).existsById(paragraphDTO.getId());
    }

    @Test
    public void testGetParagraphsBySpo() {
        Long spoId = 1L;
        ParagraphDTO paragraphDTO1 = new ParagraphDTO();
        paragraphDTO1.setTitle("C Title");
        ParagraphDTO paragraphDTO2 = new ParagraphDTO();
        paragraphDTO2.setTitle("A Title");

        ParagraphEntity entity1 = new ParagraphEntity();
        ParagraphEntity entity2 = new ParagraphEntity();
        entity1.setId(1L);
        entity2.setId(2L);
        entity1.setTitle("C Title");
        entity2.setTitle("A Title");

        when(paragraphRepository.findBySpoId(spoId)).thenReturn(Arrays.asList(entity1, entity2));
        when(paragraphConverter.toDto(entity1)).thenReturn(paragraphDTO1);
        when(paragraphConverter.toDto(entity2)).thenReturn(paragraphDTO2);

        List<ParagraphDTO> result = paragraphService.getParagraphsBySpo(spoId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("A Title");
        assertThat(result.get(1).getTitle()).isEqualTo("C Title");

        verify(paragraphRepository, times(1)).findBySpoId(spoId);
    }
}
