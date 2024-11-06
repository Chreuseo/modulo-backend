package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionService;
import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ModuleImplementationDTO;
import de.modulo.backend.dtos.ModuleImplementationDTOFlat;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.repositories.ModuleFrameModuleImplementationRepository;
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
    private final ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;

    @Autowired
    public ModuleImplementationController(SessionService sessionService,
                                          ModuleImplementationService moduleImplementationService,
                                          ValidatePrivilegesService validatePrivilegesService,
                                          ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository) {
        this.sessionService = sessionService;
        this.moduleImplementationService = moduleImplementationService;
        this.validatePrivilegesService = validatePrivilegesService;
        this.moduleFrameModuleImplementationRepository = moduleFrameModuleImplementationRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ModuleImplementationDTOFlat>> getAllModuleImplementations(HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
            return new ResponseEntity<>(moduleImplementationService.getAllModuleImplementations(), HttpStatus.OK);
        }catch (NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(moduleImplementationService.getAllModuleImplementations(), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            Long userId = sessionService.getUserBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request))).getId();
            return new ResponseEntity<>(moduleImplementationService.getAllAssignedModuleImplementations(userId), HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<ModuleImplementationDTOFlat> addModuleImplementation(@RequestBody ModuleImplementationDTOFlat moduleImplementationDTOFlat, HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));

            if(sessionService.getRoleBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request))).equals(ROLE.ADMIN)){
                return new ResponseEntity<>(moduleImplementationService.addModuleImplementation(moduleImplementationDTOFlat), HttpStatus.CREATED);
            }
            else{
                UserEntity user = sessionService.getUserBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request)));
                ModuleImplementationDTOFlat moduleImplementation = moduleImplementationService.addModuleImplementationAndSetResponsible(moduleImplementationDTOFlat, user);
                return new ResponseEntity<>(moduleImplementation, HttpStatus.CREATED);
            }

        }catch (NotifyException e){
            UserEntity user = sessionService.getUserBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request)));
            ModuleImplementationDTOFlat moduleImplementation = moduleImplementationService.addModuleImplementationAndSetResponsible(moduleImplementationDTOFlat, user);

            e.setEditedObject(new Object[]{moduleFrameModuleImplementationRepository.findById(moduleImplementation.getId()).orElseThrow()});
            e.sendNotification();

            return new ResponseEntity<>(moduleImplementation, HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ModuleImplementationDTO> updateModuleImplementation(@RequestBody ModuleImplementationDTO moduleImplementationDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request), moduleImplementationDTO.getId());
            return new ResponseEntity<>(moduleImplementationService.updateModuleImplementation(moduleImplementationDTO, sessionService.getUserBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request)))), HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            try{
                return new ResponseEntity<>(moduleImplementationService.updateModuleImplementation(moduleImplementationDTO, sessionService.getUserBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request)))), HttpStatus.OK);
            }catch (InsufficientPermissionsException ex){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteModuleImplementation(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request), id);
            moduleImplementationService.deleteModuleImplementation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(NotifyException e){
            e.sendNotification();
            moduleImplementationService.deleteModuleImplementation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleImplementationDTO> getModuleImplementationById(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), id);
            return new ResponseEntity<>(moduleImplementationService.getModuleImplementationById(id), HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(moduleImplementationService.getModuleImplementationById(id), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/flat/{id}")
    public ResponseEntity<ModuleImplementationDTOFlat> getModuleImplementationFlatById(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), id);
            return new ResponseEntity<>(moduleImplementationService.getModuleImplementationFlatById(id), HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(moduleImplementationService.getModuleImplementationFlatById(id), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/{id}/lecturer/add/{lecturerId}")
    public ResponseEntity<ModuleImplementationDTO> addLecturerToModuleImplementation(@PathVariable Long id, @PathVariable Long lecturerId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request), id);
            return new ResponseEntity<>(moduleImplementationService.addLecturerToModuleImplementation(id, lecturerId), HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(moduleImplementationService.addLecturerToModuleImplementation(id, lecturerId), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("/{id}/lecturer/remove/{lecturerId}")
    public ResponseEntity<ModuleImplementationDTO> removeLecturerFromModuleImplementation(@PathVariable Long id, @PathVariable Long lecturerId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request), id);
            moduleImplementationService.removeLecturerFromModuleImplementation(id, lecturerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(NotifyException e){
            e.sendNotification();
            moduleImplementationService.removeLecturerFromModuleImplementation(id, lecturerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
