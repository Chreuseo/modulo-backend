package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ModuleFrameModuleImplementationDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.services.ModuleFrameModuleImplementationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/module-frame-module-implementations")
public class ModuleFrameModuleImplementationController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION;

    private final ModuleFrameModuleImplementationService service;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public ModuleFrameModuleImplementationController(ModuleFrameModuleImplementationService service,
                                                     ValidatePrivilegesService validatePrivilegesService) {
        this.service = service;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ModuleFrameModuleImplementationDTO>> getAll(HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<ModuleFrameModuleImplementationDTO> dtoList = service.findAll();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleFrameModuleImplementationDTO> getById(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ModuleFrameModuleImplementationDTO dto = service.findById(id);
        return dto != null ? new ResponseEntity<>(dto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/module-frame/{moduleFrameId}")
    public ResponseEntity<List<ModuleFrameModuleImplementationDTO>> getByModuleFrameId(@PathVariable Long moduleFrameId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<ModuleFrameModuleImplementationDTO> dtoList = service.findByModuleFrameId(moduleFrameId);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/module-implementation/{moduleImplementationId}")
    public ResponseEntity<List<ModuleFrameModuleImplementationDTO>> getByModuleImplementationId(@PathVariable Long moduleImplementationId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<ModuleFrameModuleImplementationDTO> dtoList = service.findByModuleImplementationId(moduleImplementationId);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ModuleFrameModuleImplementationDTO> create(@RequestBody ModuleFrameModuleImplementationDTO dto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ModuleFrameModuleImplementationDTO createdDto = service.save(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ModuleFrameModuleImplementationDTO> update(@RequestBody ModuleFrameModuleImplementationDTO dto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ModuleFrameModuleImplementationDTO updatedDto = service.save(dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
