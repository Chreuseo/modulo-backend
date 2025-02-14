package de.modulo.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleFrameModuleImplementationEntityTest {

    private ModuleFrameModuleImplementationEntity moduleFrameModuleImplementationEntity = new ModuleFrameModuleImplementationEntity();

    @BeforeEach
    void setUp() {
        moduleFrameModuleImplementationEntity.setId(1L);
    }

    @Test
    void testIdSetterAndGetter() {
        Long id = 1L;
        moduleFrameModuleImplementationEntity.setId(id);
        assertEquals(id, moduleFrameModuleImplementationEntity.getId());
    }

    @Test
    void testModuleFrameSetterAndGetter() {
        ModuleFrameEntity moduleFrame = new ModuleFrameEntity();
        moduleFrameModuleImplementationEntity.setModuleFrame(moduleFrame);
        assertEquals(moduleFrame, moduleFrameModuleImplementationEntity.getModuleFrame());
    }

    @Test
    void testModuleImplementationSetterAndGetter() {
        ModuleImplementationEntity moduleImplementation = new ModuleImplementationEntity();
        moduleFrameModuleImplementationEntity.setModuleImplementation(moduleImplementation);
        assertEquals(moduleImplementation, moduleFrameModuleImplementationEntity.getModuleImplementation());
    }

    @Test
    void testModuleRequirementSetterAndGetter() {
        ModuleRequirementEntity moduleRequirement = new ModuleRequirementEntity();
        moduleFrameModuleImplementationEntity.setModuleRequirement(moduleRequirement);
        assertEquals(moduleRequirement, moduleFrameModuleImplementationEntity.getModuleRequirement());
    }

    @Test
    void testSemesterSetterAndGetter() {
        String semester = "2023-1";
        moduleFrameModuleImplementationEntity.setSemester(semester);
        assertEquals(semester, moduleFrameModuleImplementationEntity.getSemester());
    }

    @Test
    void testEquals() {
        ModuleFrameModuleImplementationEntity anotherEntity = new ModuleFrameModuleImplementationEntity();
        anotherEntity.setId(moduleFrameModuleImplementationEntity.getId());

        assertEquals(moduleFrameModuleImplementationEntity, anotherEntity);

        anotherEntity.setId(2L);
        assertNotEquals(moduleFrameModuleImplementationEntity, anotherEntity);
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(moduleFrameModuleImplementationEntity, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertNotEquals(moduleFrameModuleImplementationEntity, new Object());
    }
}
