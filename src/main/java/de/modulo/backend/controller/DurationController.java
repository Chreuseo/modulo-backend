package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.DurationDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.services.DurationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/duration")
public class DurationController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.GENERAL_SETTINGS;

    private final DurationService durationService;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public DurationController(DurationService durationService,
                              ValidatePrivilegesService validatePrivilegesService) {
        this.durationService = durationService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    // Endpoint to get all durations
    @GetMapping("/all")
    public ResponseEntity<List<DurationDTO>> getAllDurations(HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<DurationDTO> durations = durationService.getAll();
        return new ResponseEntity<>(durations, HttpStatus.OK);
    }

    // Endpoint to add a new duration
    @PostMapping("/new")
    public ResponseEntity<DurationDTO> addDuration(@RequestBody DurationDTO durationDto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        DurationDTO savedDuration = durationService.add(durationDto);
        return new ResponseEntity<>(savedDuration, HttpStatus.CREATED);
    }

    // Endpoint to delete a duration by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteDuration(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        durationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
