package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.SectionDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.SectionRepository;
import de.modulo.backend.services.SectionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/section")
public class SectionController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.SPO_SETTINGS;

    private final SectionService sectionService;
    private final ValidatePrivilegesService validatePrivilegesService;
    private final SectionRepository sectionRepository;

    @Autowired
    public SectionController(SectionService sectionService,
                             ValidatePrivilegesService validatePrivilegesService, SectionRepository sectionRepository) {
        this.sectionService = sectionService;
        this.validatePrivilegesService = validatePrivilegesService;
        this.sectionRepository = sectionRepository;
    }

    @PostMapping("/new")
    public ResponseEntity<SectionDTO> createSection(@RequestBody SectionDTO sectionDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request), sectionDTO.getSpoId());
            SectionDTO createdSection = sectionService.add(sectionDTO);
            return new ResponseEntity<>(createdSection, HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request),
                    sectionRepository.findById(id).orElseThrow().getSpo().getId());
            sectionService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
