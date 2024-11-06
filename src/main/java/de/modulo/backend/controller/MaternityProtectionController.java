package de.modulo.backend.controller;
import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.MaternityProtectionDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
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
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
            List<MaternityProtectionDTO> protections = maternityProtectionService.getAll();
            return new ResponseEntity<>(protections, HttpStatus.OK);
        }catch (NotifyException e){
            e.sendNotification();
            List<MaternityProtectionDTO> protections = maternityProtectionService.getAll();
            return new ResponseEntity<>(protections, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Endpoint to add a new maternity protection entry
    @PostMapping("/new")
    public ResponseEntity<MaternityProtectionDTO> addMaternityProtection(@RequestBody MaternityProtectionDTO maternityProtectionDto, HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
            MaternityProtectionDTO savedProtection = maternityProtectionService.add(maternityProtectionDto);
            return new ResponseEntity<>(savedProtection, HttpStatus.CREATED);
        }catch (NotifyException e){
            e.sendNotification();
            MaternityProtectionDTO savedProtection = maternityProtectionService.add(maternityProtectionDto);
            return new ResponseEntity<>(savedProtection, HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Endpoint to delete a maternity protection entry by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteMaternityProtection(@PathVariable Long id, HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
            maternityProtectionService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NotifyException e){
            e.sendNotification();
            maternityProtectionService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
