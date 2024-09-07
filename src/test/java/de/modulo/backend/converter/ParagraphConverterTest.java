package de.modulo.backend.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.modulo.backend.converters.ParagraphConverter;
import de.modulo.backend.dtos.ParagraphDTO;
import de.modulo.backend.entities.ParagraphEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ParagraphConverterTest {

    private ParagraphConverter paragraphConverter;

    @BeforeEach
    public void setUp() {
        paragraphConverter = new ParagraphConverter();
    }

    @Test
    public void testToDtoWithNullEntity() {
        ParagraphDTO result = paragraphConverter.toDto(null);
        assertNull(result, "toDto should return null when input entity is null");
    }

    @Test
    public void testToDtoWithEntity() {
        // Given
        int id = 1;
        String title = "Paragraph Title";
        String text = "This is the paragraph text.";

        ParagraphEntity nextParagraph = new ParagraphEntity();
        nextParagraph.setId(2); // Example Next Paragraph Id

        ParagraphEntity entity = new ParagraphEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setText(text);

        // When
        ParagraphDTO result = paragraphConverter.toDto(entity);

        // Then
        assertEquals(id, result.getId());
        assertEquals(title, result.getTitle());
        assertEquals(text, result.getText());
    }

    @Test
    public void testToEntityWithNullDto() {
        ParagraphEntity result = paragraphConverter.toEntity(null);
        assertNull(result, "toEntity should return null when input DTO is null");
    }

    @Test
    public void testToEntity() {
        // Given
        int id = 1;
        String title = "Paragraph Title";
        String text = "This is the paragraph text.";

        ParagraphDTO dto = new ParagraphDTO();
        dto.setId(id);
        dto.setTitle(title);
        dto.setText(text);

        // When
        ParagraphEntity result = paragraphConverter.toEntity(dto);

        // Then
        assertEquals(id, result.getId());
        assertEquals(title, result.getTitle());
        assertEquals(text, result.getText());
        // We cannot assert the nextParagraph directly here
        // as it requires a full ParagraphEntity instance
    }
}
