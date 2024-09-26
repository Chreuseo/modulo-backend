package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionService;
import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ModuleImplementationDTO;
import de.modulo.backend.dtos.ModuleImplementationDTOFlat;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.services.ModuleImplementationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/module-implementation")
public class ModuleImplementationController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.MODULE;

    private final SessionService sessionService;
    private final ModuleImplementationService moduleImplementationService;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public ModuleImplementationController(SessionService sessionService,
                                          ModuleImplementationService moduleImplementationService,
                                          ValidatePrivilegesService validatePrivilegesService) {
        this.sessionService = sessionService;
        this.moduleImplementationService = moduleImplementationService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ModuleImplementationDTOFlat>> getAllModuleImplementations(HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            Long userId = sessionService.getUserBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request))).getId();
            return new ResponseEntity<>(moduleImplementationService.getAllAssignedModuleImplementations(userId), HttpStatus.OK);
        }

        return new ResponseEntity<>(moduleImplementationService.getAllModuleImplementations(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<ModuleImplementationDTOFlat> addModuleImplementation(@RequestBody ModuleImplementationDTOFlat moduleImplementationDTOFlat, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(moduleImplementationService.addModuleImplementation(moduleImplementationDTOFlat), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ModuleImplementationDTO> updateModuleImplementation(@RequestBody ModuleImplementationDTO moduleImplementationDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request), moduleImplementationDTO.getId());
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(moduleImplementationService.updateModuleImplementation(moduleImplementationDTO), HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteModuleImplementation(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request), id);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        moduleImplementationService.deleteModuleImplementation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleImplementationDTO> getModuleImplementationById(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), id);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(moduleImplementationService.getModuleImplementationById(id), HttpStatus.OK);
    }

    @GetMapping("/flat/{id}")
    public ResponseEntity<ModuleImplementationDTOFlat> getModuleImplementationFlatById(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), id);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(moduleImplementationService.getModuleImplementationFlatById(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/lecturer/add/{lecturerId}")
    public ResponseEntity<ModuleImplementationDTO> addLecturerToModuleImplementation(@PathVariable Long id, @PathVariable Long lecturerId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(moduleImplementationService.addLecturerToModuleImplementation(id, lecturerId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/lecturer/remove/{lecturerId}")
    public ResponseEntity<ModuleImplementationDTO> removeLecturerFromModuleImplementation(@PathVariable Long id, @PathVariable Long lecturerId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request), id);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        moduleImplementationService.removeLecturerFromModuleImplementation(id, lecturerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
