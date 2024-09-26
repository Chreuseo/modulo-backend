package de.modulo.backend.controller;
import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.MaternityProtectionDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.services.MaternityProtectionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maternity-protection")
public class MaternityProtectionController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.GENERAL_SETTINGS;

    private final MaternityProtectionService maternityProtectionService;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public MaternityProtectionController(MaternityProtectionService maternityProtectionService,
                                         ValidatePrivilegesService validatePrivilegesService) {
        this.maternityProtectionService = maternityProtectionService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    // Endpoint to get all maternity protection entries
    @GetMapping("/all")
    public ResponseEntity<List<MaternityProtectionDTO>> getAllMaternityProtections(HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<MaternityProtectionDTO> protections = maternityProtectionService.getAll();
        return new ResponseEntity<>(protections, HttpStatus.OK);
    }

    // Endpoint to add a new maternity protection entry
    @PostMapping("/new")
    public ResponseEntity<MaternityProtectionDTO> addMaternityProtection(@RequestBody MaternityProtectionDTO maternityProtectionDto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        MaternityProtectionDTO savedProtection = maternityProtectionService.add(maternityProtectionDto);
        return new ResponseEntity<>(savedProtection, HttpStatus.CREATED);
    }

    // Endpoint to delete a maternity protection entry by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteMaternityProtection(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        maternityProtectionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
