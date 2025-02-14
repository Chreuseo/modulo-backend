package de.modulo.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExamTypeModuleImplementationEntityTest {

    @InjectMocks
    private ExamTypeModuleImplementationEntity examTypeModuleImplementationEntity;

    @Mock
    private ExamTypeEntity mockExamTypeEntity;

    @Mock
    private ModuleImplementationEntity mockModuleImplementationEntity;

    private ExamTypeModuleImplementationEntity.ExamTypeModuleImplementationId compositeId;

    private String length;
    private String description;

    @BeforeEach
    public void setUp() {
        Long examTypeId = 1L;
        Long moduleImplementationId = 2L;
        compositeId = new ExamTypeModuleImplementationEntity.ExamTypeModuleImplementationId(examTypeId, moduleImplementationId);

        examTypeModuleImplementationEntity.setId(compositeId);
        examTypeModuleImplementationEntity.setExamType(mockExamTypeEntity);
        examTypeModuleImplementationEntity.setModuleImplementation(mockModuleImplementationEntity);
        length = "90 minutes";
        description = "Final Exam";
        examTypeModuleImplementationEntity.setLength(length);
        examTypeModuleImplementationEntity.setDescription(description);
    }

    @Test
    public void testSettersAndGetters() {
        assertEquals(compositeId, examTypeModuleImplementationEntity.getId());
        assertEquals(mockExamTypeEntity, examTypeModuleImplementationEntity.getExamType());
        assertEquals(mockModuleImplementationEntity, examTypeModuleImplementationEntity.getModuleImplementation());
        assertEquals(length, examTypeModuleImplementationEntity.getLength());
        assertEquals(description, examTypeModuleImplementationEntity.getDescription());
    }

    @Test
    public void testEqualsAndHashCode() {
        ExamTypeModuleImplementationEntity otherEntity = new ExamTypeModuleImplementationEntity();
        ExamTypeModuleImplementationEntity.ExamTypeModuleImplementationId otherId = new ExamTypeModuleImplementationEntity.ExamTypeModuleImplementationId(1L, 2L);

        otherEntity.setId(otherId);

        examTypeModuleImplementationEntity.setId(compositeId);

        // Both entities should be equal since IDs match
        assertEquals(examTypeModuleImplementationEntity, otherEntity);

        otherEntity.setId(new ExamTypeModuleImplementationEntity.ExamTypeModuleImplementationId(3L, 4L));

        // Now they should not be equal
        assertNotEquals(examTypeModuleImplementationEntity, otherEntity);
    }

    @Test
    public void testEqualsDifferentObjects() {
        // Different object types
        assertNotEquals(examTypeModuleImplementationEntity, new Object());
    }

    @Test
    public void testEqualsNull() {
        // Comparing with null should return false
        assertNotEquals(examTypeModuleImplementationEntity, null);
    }
}
