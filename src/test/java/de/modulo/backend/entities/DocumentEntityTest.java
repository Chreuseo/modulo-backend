package de.modulo.backend.entities;

import de.modulo.backend.enums.DOCUMENT_TYPE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentEntityTest {

    @InjectMocks
    private DocumentEntity documentEntity;

    @Mock
    private SpoEntity mockSpoEntity;

    @Mock
    private SemesterEntity mockSemesterEntity;

    private DOCUMENT_TYPE documentType;
    private LocalDateTime generatedAt;

    @BeforeEach
    public void setUp() {
        // Initialize the fields for the tests
        documentType = DOCUMENT_TYPE.SPO; // Replace with a valid DOCUMENT_TYPE
        generatedAt = LocalDateTime.now();
        documentEntity = new DocumentEntity(mockSpoEntity, mockSemesterEntity, documentType, generatedAt);
    }

    @Test
    public void testDocumentEntityConstructor() {
        assertEquals(mockSpoEntity, documentEntity.getSpo());
        assertEquals(mockSemesterEntity, documentEntity.getSemester());
        assertEquals(documentType, documentEntity.getType());
        assertEquals(generatedAt, documentEntity.getGeneratedAt());
    }

    @Test
    public void testSettersAndGetters() {
        // Test setters and getters for each field
        DocumentEntity entity = new DocumentEntity();
        entity.setSpo(mockSpoEntity);
        entity.setSemester(mockSemesterEntity);
        entity.setType(documentType);
        entity.setGeneratedAt(generatedAt);

        assertEquals(mockSpoEntity, entity.getSpo());
        assertEquals(mockSemesterEntity, entity.getSemester());
        assertEquals(documentType, entity.getType());
        assertEquals(generatedAt, entity.getGeneratedAt());
    }

    @Test
    public void testIdInitialization() {
        // Test that ID is initialized to null
        assertNull(documentEntity.getId());
    }

    @Test
    public void testDocumentDataField() {
        // Test data field initial state
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        documentEntity.setData(data);

        assertArrayEquals(data, documentEntity.getData());
    }
}
