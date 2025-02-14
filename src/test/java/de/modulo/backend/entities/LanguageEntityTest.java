package de.modulo.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageEntityTest {

    private LanguageEntity languageEntity = new LanguageEntity();

    @BeforeEach
    void setUp() {
        languageEntity.setId(1L);
    }

    @Test
    void testIdSetterAndGetter() {
        Long id = 1L;
        languageEntity.setId(id);
        assertEquals(id, languageEntity.getId());
    }

    @Test
    void testNameSetterAndGetter() {
        String name = "English";
        languageEntity.setName(name);
        assertEquals(name, languageEntity.getName());
    }

    @Test
    void testEquals() {
        LanguageEntity anotherLanguage = new LanguageEntity();
        anotherLanguage.setId(languageEntity.getId());

        // Test equality with the same ID
        assertEquals(languageEntity, anotherLanguage);

        // Test inequality when IDs differ
        anotherLanguage.setId(2L);
        assertNotEquals(languageEntity, anotherLanguage);
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(languageEntity, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertNotEquals(languageEntity, new Object());
    }
}
