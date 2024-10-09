package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.DegreeDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.services.DegreeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/degree")
public class DegreeController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.GENERAL_SETTINGS;

    private final DegreeService degreeService;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public DegreeController(DegreeService degreeService,
                            ValidatePrivilegesService validatePrivilegesService) {
        this.degreeService = degreeService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    // Endpoint to get all degrees
    @GetMapping("/all")
    public ResponseEntity<List<DegreeDTO>> getAllDegrees(HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<DegreeDTO> degrees = degreeService.getAll();
        return new ResponseEntity<>(degrees, HttpStatus.OK);
    }

    // Endpoint to add a new degree
    @PostMapping("/new")
    public ResponseEntity<DegreeDTO> addDegree(@RequestBody DegreeDTO degreeDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        DegreeDTO savedDegree = degreeService.add(degreeDTO);
        return new ResponseEntity<>(savedDegree, HttpStatus.CREATED);
    }

    // Endpoint to delete a degree by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteDegree(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        degreeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
