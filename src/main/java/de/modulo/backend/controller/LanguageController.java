package de.modulo.backend.controller;

import de.modulo.backend.dtos.LanguageDTO;
import de.modulo.backend.services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/language")
public class LanguageController {

    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    // Endpoint to get all languages
    @GetMapping("/all")
    public ResponseEntity<List<LanguageDTO>> getAllLanguages() {
        List<LanguageDTO> languages = languageService.getAll();
        return new ResponseEntity<>(languages, HttpStatus.OK);
    }

    // Endpoint to add a new language
    @PostMapping("/new")
    public ResponseEntity<LanguageDTO> addLanguage(@RequestBody LanguageDTO languageDto) {
        LanguageDTO savedLanguage = languageService.add(languageDto);
        return new ResponseEntity<>(savedLanguage, HttpStatus.CREATED);
    }

    // Endpoint to delete a language by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        languageService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
