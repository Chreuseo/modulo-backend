package de.modulo.backend.services.data;

import de.modulo.backend.converters.LanguageConverter;
import de.modulo.backend.dtos.LanguageDTO;
import de.modulo.backend.entities.LanguageEntity;
import de.modulo.backend.repositories.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LanguageServiceTest {

    @InjectMocks
    private LanguageService languageService;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private LanguageConverter languageConverter;

    private LanguageEntity entity;
    private LanguageDTO dto;

    @BeforeEach
    void setUp() {
        entity = new LanguageEntity();
        entity.setId(1L);
        entity.setName("English");

        dto = new LanguageDTO();
        dto.setId(1L);
        dto.setName("English");
    }

    @Test
    void testGetAllLanguages() {
        List<LanguageEntity> entities = Arrays.asList(entity);
        when(languageRepository.findAll()).thenReturn(entities);
        when(languageConverter.toDto(any(LanguageEntity.class))).thenReturn(dto);

        List<LanguageDTO> result = languageService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(languageRepository, times(1)).findAll();
        verify(languageConverter, times(1)).toDto(entity);
    }

    @Test
    void testAddNewLanguage() {
        when(languageRepository.findByName("English")).thenReturn(Optional.empty());
        when(languageConverter.toEntity(dto)).thenReturn(entity);
        when(languageRepository.save(any(LanguageEntity.class))).thenReturn(entity);
        when(languageConverter.toDto(entity)).thenReturn(dto);

        LanguageDTO result = languageService.add(dto);

        assertNotNull(result);
        assertEquals("English", result.getName());
        verify(languageRepository, times(1)).findByName("English");
        verify(languageRepository, times(1)).save(entity);
    }

    @Test
    void testAddExistingLanguage() {
        when(languageRepository.findByName("English")).thenReturn(Optional.of(entity));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            languageService.add(dto);
        });

        assertEquals("Language already exists with name: English", thrown.getMessage());
        verify(languageRepository, times(1)).findByName("English");
        verify(languageRepository, never()).save(any(LanguageEntity.class));
    }

    @Test
    void testDeleteExistingLanguage() {
        when(languageRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> languageService.delete(1L));
        verify(languageRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNonExistingLanguage() {
        when(languageRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            languageService.delete(1L);
        });

        assertEquals("Language not found with id: 1", thrown.getMessage());
        verify(languageRepository, never()).deleteById(1L);
    }
}
