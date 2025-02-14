package de.modulo.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SectionEntityTest {

    @InjectMocks
    private SectionEntity sectionEntity;

    @Mock
    private SpoEntity mockSpoEntity;

    private String name;
    private int orderNumber;

    @BeforeEach
    public void setUp() {
        name = "Section Name";
        orderNumber = 1;
        sectionEntity = new SectionEntity();
        sectionEntity.setSpo(mockSpoEntity);
        sectionEntity.setName(name);
        sectionEntity.setOrderNumber(orderNumber);
    }

    @Test
    public void testSettersAndGetters() {
        assertEquals(mockSpoEntity, sectionEntity.getSpo());
        assertEquals(name, sectionEntity.getName());
        assertEquals(orderNumber, sectionEntity.getOrderNumber());
    }

    @Test
    public void testIdInitialization() {
        // Test that ID is initialized to null
        assertNull(sectionEntity.getId());
    }

    @Test
    public void testEqualsAndHashCode() {
        SectionEntity otherEntity = new SectionEntity();
        otherEntity.setId(1L);
        otherEntity.setSpo(mockSpoEntity);
        otherEntity.setName("Other Section");
        otherEntity.setOrderNumber(2);

        sectionEntity.setId(1L);

        // Both entities should be equal since IDs match
        assertEquals(sectionEntity, otherEntity);

        otherEntity.setId(2L);
        // Now they should not be equal
        assertNotEquals(sectionEntity, otherEntity);
    }

    @Test
    public void testEqualsDifferentObjects() {
        // Different object types
        assertNotEquals(sectionEntity, new Object());
    }

    @Test
    public void testEqualsNull() {
        // Comparing with null should return false
        assertNotEquals(sectionEntity, null);
    }
}
