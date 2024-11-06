package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.CycleDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.services.CycleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cycle")
public class CycleController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.GENERAL_SETTINGS;

    private final CycleService cycleService;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public CycleController(CycleService cycleService,
                           ValidatePrivilegesService validatePrivilegesService) {
        this.cycleService = cycleService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    @PostMapping("/add")
    public ResponseEntity<CycleDTO> addCycle(@RequestBody CycleDTO cycleDto, HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
            CycleDTO createdCycle = cycleService.addCycle(cycleDto);
            return new ResponseEntity<>(createdCycle, HttpStatus.CREATED);
        }catch (NotifyException e){
            e.sendNotification();
            CycleDTO createdCycle = cycleService.addCycle(cycleDto);
            return new ResponseEntity<>(createdCycle, HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<CycleDTO> updateCycle(@RequestBody CycleDTO cycleDto, HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request));
            CycleDTO updatedCycle = cycleService.updateCycle(cycleDto);
            return new ResponseEntity<>(updatedCycle, HttpStatus.OK);
        }catch (NotifyException e){
            e.sendNotification();
            CycleDTO updatedCycle = cycleService.updateCycle(cycleDto);
            return new ResponseEntity<>(updatedCycle, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeCycle(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
            cycleService.removeCycle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NotifyException e){
            e.sendNotification();
            cycleService.removeCycle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CycleDTO>> getAllCycles(HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
            List<CycleDTO> cycles = cycleService.getAllCycles();
            return new ResponseEntity<>(cycles, HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            List<CycleDTO> cycles = cycleService.getAllCycles();
            return new ResponseEntity<>(cycles, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
