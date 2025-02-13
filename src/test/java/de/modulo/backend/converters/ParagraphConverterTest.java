package de.modulo.backend.converters;

import de.modulo.backend.dtos.ParagraphDTO;
import de.modulo.backend.entities.ParagraphEntity;
import de.modulo.backend.entities.SpoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParagraphConverterTest {

    private ParagraphConverter paragraphConverter;

    @BeforeEach
    void setUp() {
        paragraphConverter = new ParagraphConverter();
    }

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

        // Act
        ParagraphEntity paragraphEntity = paragraphConverter.toEntity(paragraphDto);

        // Assert
        assertNotNull(paragraphEntity);
        assertEquals(paragraphDto.getId(), paragraphEntity.getId());
        assertEquals(paragraphDto.getTitle(), paragraphEntity.getTitle());
        assertEquals(paragraphDto.getText(), paragraphEntity.getText());
    }
}
