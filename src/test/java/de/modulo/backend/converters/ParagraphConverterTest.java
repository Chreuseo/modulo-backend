package de.modulo.backend.converters;

import de.modulo.backend.dtos.ParagraphDTO;
import de.modulo.backend.entities.ParagraphEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.SpoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParagraphConverterTest {

    @Mock
    private SpoRepository spoRepository;

    @InjectMocks
    private ParagraphConverter paragraphConverter;

    @Test
    void testToDto() {
        // Arrange
        ParagraphEntity paragraphEntity = new ParagraphEntity();
        SpoEntity spoEntity = new SpoEntity();
        spoEntity.setId(2L);
        spoEntity.setName("SPO 2020");

        paragraphEntity.setId(1L);
        paragraphEntity.setTitle("Title");
        paragraphEntity.setText("Text");
        paragraphEntity.setSpo(spoEntity);

        // Act
        ParagraphDTO paragraphDto = paragraphConverter.toDto(paragraphEntity);

        // Assert
        assertNotNull(paragraphDto);
        assertEquals(paragraphEntity.getId(), paragraphDto.getId());
        assertEquals(paragraphEntity.getTitle(), paragraphDto.getTitle());
        assertEquals(paragraphEntity.getText(), paragraphDto.getText());
        assertEquals(paragraphEntity.getSpo().getId(), paragraphDto.getSpoId());
    }

    @Test
    void testToEntity() {
        // Arrange
        ParagraphDTO paragraphDto = new ParagraphDTO();
        paragraphDto.setId(1L);
        paragraphDto.setTitle("Title");
        paragraphDto.setText("Text");
        paragraphDto.setSpoId(2L);

        // Create a mock SpoEntity that we expect to receive from the repository
        SpoEntity mockSpoEntity = new SpoEntity();
        mockSpoEntity.setId(2L);
        mockSpoEntity.setName("SPO 2020");

        // Mock the behavior of spoRepository.findById for the specific SPO ID
        when(spoRepository.findById(2L)).thenReturn(java.util.Optional.of(mockSpoEntity));


        // Act
        ParagraphEntity paragraphEntity = paragraphConverter.toEntity(paragraphDto);

        // Assert
        assertNotNull(paragraphEntity);
        assertEquals(paragraphDto.getId(), paragraphEntity.getId());
        assertEquals(paragraphDto.getTitle(), paragraphEntity.getTitle());
        assertEquals(paragraphDto.getText(), paragraphEntity.getText());
        assertEquals(paragraphDto.getSpoId(), paragraphEntity.getSpo().getId());
        assertEquals(mockSpoEntity.getName(), paragraphEntity.getSpo().getName());
    }
}
