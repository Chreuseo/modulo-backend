package de.modulo.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CourseTypeEntityTest {

    @InjectMocks
    private CourseTypeEntity courseTypeEntity;

    private String name;
    private String abbreviation;

    @BeforeEach
    public void setUp() {
        name = "Course Name";
        abbreviation = "CN";
        courseTypeEntity = new CourseTypeEntity();
        courseTypeEntity.setName(name);
        courseTypeEntity.setAbbreviation(abbreviation);
    }

    @Test
    public void testSettersAndGetters() {
        assertEquals(name, courseTypeEntity.getName());
        assertEquals(abbreviation, courseTypeEntity.getAbbreviation());
    }

    @Test
    public void testIdInitialization() {
        // Test that ID is initialized to null
        assertNull(courseTypeEntity.getId());
    }

    @Test
    public void testEqualsAndHashCode() {
        CourseTypeEntity otherEntity = new CourseTypeEntity();
        otherEntity.setId(1L);
        otherEntity.setName("Other Course");
        otherEntity.setAbbreviation("OC");

        courseTypeEntity.setId(1L);

        // Both entities should be equal since IDs match
        assertEquals(courseTypeEntity, otherEntity);

        otherEntity.setId(2L);
        // Now they should not be equal
        assertNotEquals(courseTypeEntity, otherEntity);
    }

    @Test
    public void testEqualsDifferentObjects() {
        // Different object types
        assertNotEquals(courseTypeEntity, new Object());
    }

    @Test
    public void testEqualsNull() {
        // Comparing with null should return false
        assertNotEquals(courseTypeEntity, null);
    }
}
