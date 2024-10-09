package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.LanguageDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.services.LanguageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/language")
public class LanguageController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.GENERAL_SETTINGS;

    private final LanguageService languageService;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public LanguageController(LanguageService languageService,
                              ValidatePrivilegesService validatePrivilegesService) {
        this.languageService = languageService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    // Endpoint to get all languages
    @GetMapping("/all")
    public ResponseEntity<List<LanguageDTO>> getAllLanguages(HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<LanguageDTO> languages = languageService.getAll();
        return new ResponseEntity<>(languages, HttpStatus.OK);
    }

    // Endpoint to add a new language
    @PostMapping("/new")
    public ResponseEntity<LanguageDTO> addLanguage(@RequestBody LanguageDTO languageDto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        LanguageDTO savedLanguage = languageService.add(languageDto);
        return new ResponseEntity<>(savedLanguage, HttpStatus.CREATED);
    }

    // Endpoint to delete a language by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        languageService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
