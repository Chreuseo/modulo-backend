package de.modulo.backend.services.data;

import de.modulo.backend.converters.LanguageConverter;
import de.modulo.backend.dtos.LanguageDTO;
import de.modulo.backend.entities.LanguageEntity;
import de.modulo.backend.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageConverter languageConverter;

    @Autowired
    public LanguageService(LanguageRepository languageRepository, LanguageConverter languageConverter) {
        this.languageRepository = languageRepository;
        this.languageConverter = languageConverter;
    }

    // Get all languages
    public List<LanguageDTO> getAll() {
        return languageRepository.findAll().stream()
                .map(languageConverter::toDto)
                .collect(Collectors.toList());
    }

    // Add a new language
    public LanguageDTO add(LanguageDTO languageDto) {
        // Check if the language already exists
        if (languageRepository.findByName(languageDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Language already exists with name: " + languageDto.getName());
        }

        LanguageEntity languageEntity = languageConverter.toEntity(languageDto);
        LanguageEntity savedEntity = languageRepository.save(languageEntity);
        return languageConverter.toDto(savedEntity);
    }

    // Delete a language by ID
    public void delete(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new IllegalArgumentException("Language not found with id: " + id);
        }
        languageRepository.deleteById(id);
    }
}
