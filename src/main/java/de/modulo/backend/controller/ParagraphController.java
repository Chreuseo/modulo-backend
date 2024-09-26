package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ParagraphDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
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

    @Autowired
    public ParagraphController(ParagraphService paragraphService,
                               ValidatePrivilegesService validatePrivilegesService) {
        this.paragraphService = paragraphService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    @PostMapping
    public ResponseEntity<ParagraphDTO> addParagraph(@RequestBody ParagraphDTO paragraphDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ParagraphDTO createdParagraph = paragraphService.addParagraph(paragraphDTO);
        return new ResponseEntity<>(createdParagraph, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ParagraphDTO> updateParagraph(@PathVariable Long id, @RequestBody ParagraphDTO paragraphDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        paragraphDTO.setId(id);
        ParagraphDTO updatedParagraph = paragraphService.updateParagraph(paragraphDTO);
        return new ResponseEntity<>(updatedParagraph, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteParagraph(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        paragraphService.deleteParagraph(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/spo/{spoId}")
    public ResponseEntity<List<ParagraphDTO>> getParagraphsBySpo(@PathVariable Long spoId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<ParagraphDTO> paragraphs = paragraphService.getParagraphsBySpo(spoId);
        return new ResponseEntity<>(paragraphs, HttpStatus.OK);
    }
}