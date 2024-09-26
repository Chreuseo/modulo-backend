package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.dtos.ModuleFrameSetDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.services.ModuleFrameService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/module-frames")
public class ModuleFrameController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.SPO_SETTINGS;

    private final ModuleFrameService moduleFrameService;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public ModuleFrameController(ModuleFrameService moduleFrameService,
                                 ValidatePrivilegesService validatePrivilegesService) {
        this.moduleFrameService = moduleFrameService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    // Endpoint to get ModuleFrameSetDTO by SPO ID
    @GetMapping("/spo/{spoId}")
    public ResponseEntity<ModuleFrameSetDTO> getModuleFrameSetBySpoId(@PathVariable Long spoId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ModuleFrameSetDTO moduleFrameSetDTO = moduleFrameService.getModuleFrameSetDTOBySpoId(spoId);
        return ResponseEntity.ok(moduleFrameSetDTO);
    }

    // Endpoint to add a new ModuleFrame
    @PostMapping("/new")
    public ResponseEntity<ModuleFrameDTO> addModuleFrame(@RequestBody ModuleFrameDTO moduleFrameDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ModuleFrameDTO createdModuleFrame = moduleFrameService.addModuleFrame(moduleFrameDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModuleFrame);
    }

    // Endpoint to update an existing ModuleFrame
    @PutMapping("/update")
    public ResponseEntity<ModuleFrameDTO> updateModuleFrame(@RequestBody ModuleFrameDTO moduleFrameDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ModuleFrameDTO result = moduleFrameService.updateModuleFrame(moduleFrameDTO);
        return ResponseEntity.ok(result);
    }

    // Endpoint to delete a ModuleFrame by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteModuleFrame(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        moduleFrameService.deleteModuleFrame(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
