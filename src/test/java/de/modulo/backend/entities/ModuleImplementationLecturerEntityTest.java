package de.modulo.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleImplementationLecturerEntityTest {

    private ModuleImplementationLecturerEntity moduleImplementationLecturerEntity;

    @BeforeEach
    void setUp() {
        moduleImplementationLecturerEntity = new ModuleImplementationLecturerEntity();
    }

    @Test
    void testIdSetterAndGetter() {
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId id = new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId();
        id.setModuleImplementation(1L);
        id.setLecturer(2L);

        moduleImplementationLecturerEntity.setId(id);

        assertEquals(id, moduleImplementationLecturerEntity.getId());
    }

    @Test
    void testModuleImplementationSetterAndGetter() {
        ModuleImplementationEntity moduleImplementation = new ModuleImplementationEntity();
        moduleImplementationLecturerEntity.setModuleImplementation(moduleImplementation);

        assertEquals(moduleImplementation, moduleImplementationLecturerEntity.getModuleImplementation());
    }

    @Test
    void testLecturerSetterAndGetter() {
        UserEntity lecturer = new UserEntity();
        moduleImplementationLecturerEntity.setLecturer(lecturer);

        assertEquals(lecturer, moduleImplementationLecturerEntity.getLecturer());
    }

    @Test
    void testEquals() {
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId id = new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId();
        id.setModuleImplementation(1L);
        id.setLecturer(2L);

        moduleImplementationLecturerEntity.setId(id);

        ModuleImplementationLecturerEntity anotherEntity = new ModuleImplementationLecturerEntity();
        anotherEntity.setId(id);

        assertEquals(moduleImplementationLecturerEntity, anotherEntity);


    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(moduleImplementationLecturerEntity, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertNotEquals(moduleImplementationLecturerEntity, new Object());
    }

    // Additional tests for ModuleImplementationLecturerEntityId if necessary
    @Test
    void testModuleImplementationLecturerEntityIdEquals() {
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId id1 =
                new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId(1L, 2L);
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId id2 =
                new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId(1L, 2L);
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId id3 =
                new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId(2L, 3L);

        assertEquals(id1, id2); // Same values should be equal
        assertNotEquals(id1, id3); // Different values should not be equal
    }
}
