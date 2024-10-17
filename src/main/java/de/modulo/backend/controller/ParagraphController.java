package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ParagraphDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.ParagraphRepository;
import de.modulo.backend.services.ParagraphService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paragraphs")
public class ParagraphController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.SPO_SETTINGS;

    private final ParagraphService paragraphService;
    private final ValidatePrivilegesService validatePrivilegesService;
    private final ParagraphRepository paragraphRepository;

    @Autowired
    public ParagraphController(ParagraphService paragraphService,
                               ValidatePrivilegesService validatePrivilegesService, ParagraphRepository paragraphRepository) {
        this.paragraphService = paragraphService;
        this.validatePrivilegesService = validatePrivilegesService;
        this.paragraphRepository = paragraphRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<ParagraphDTO> addParagraph(@RequestBody ParagraphDTO paragraphDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request), paragraphDTO.getSpoId());
            ParagraphDTO createdParagraph = paragraphService.addParagraph(paragraphDTO);
            return new ResponseEntity<>(createdParagraph, HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ParagraphDTO> updateParagraph(@RequestBody ParagraphDTO paragraphDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request), paragraphDTO.getSpoId());
            ParagraphDTO updatedParagraph = paragraphService.updateParagraph(paragraphDTO);
            return new ResponseEntity<>(updatedParagraph, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteParagraph(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request),
                    paragraphRepository.findById(id).orElseThrow().getSpo().getId());
            paragraphService.deleteParagraph(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/spo/{spoId}")
    public ResponseEntity<List<ParagraphDTO>> getParagraphsBySpo(@PathVariable Long spoId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), spoId);
            List<ParagraphDTO> paragraphs = paragraphService.getParagraphsBySpo(spoId);
            return new ResponseEntity<>(paragraphs, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}