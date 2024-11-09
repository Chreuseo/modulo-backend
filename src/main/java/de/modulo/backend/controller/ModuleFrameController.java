package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.dtos.ModuleFrameSetDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.ModuleFrameRepository;
import de.modulo.backend.services.data.ModuleFrameService;
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
    private final ModuleFrameRepository moduleFrameRepository;

    @Autowired
    public ModuleFrameController(ModuleFrameService moduleFrameService,
                                 ValidatePrivilegesService validatePrivilegesService, ModuleFrameRepository moduleFrameRepository) {
        this.moduleFrameService = moduleFrameService;
        this.validatePrivilegesService = validatePrivilegesService;
        this.moduleFrameRepository = moduleFrameRepository;
    }

    // Endpoint to get ModuleFrameSetDTO by SPO ID
    @GetMapping("/spo/{spoId}")
    public ResponseEntity<ModuleFrameSetDTO> getModuleFrameSetBySpoId(@PathVariable Long spoId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), spoId);
            ModuleFrameSetDTO moduleFrameSetDTO = moduleFrameService.getModuleFrameSetDTOBySpoId(spoId);
            return ResponseEntity.ok(moduleFrameSetDTO);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Endpoint to add a new ModuleFrame
    @PostMapping("/new")
    public ResponseEntity<ModuleFrameDTO> addModuleFrame(@RequestBody ModuleFrameDTO moduleFrameDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request), moduleFrameDTO.getSpoDTOFlat().getId());
            ModuleFrameDTO createdModuleFrame = moduleFrameService.addModuleFrame(moduleFrameDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdModuleFrame);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Endpoint to update an existing ModuleFrame
    @PutMapping("/update")
    public ResponseEntity<ModuleFrameDTO> updateModuleFrame(@RequestBody ModuleFrameDTO moduleFrameDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request), moduleFrameDTO.getSpoDTOFlat().getId());
            ModuleFrameDTO result = moduleFrameService.updateModuleFrame(moduleFrameDTO);
            return ResponseEntity.ok(result);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Endpoint to delete a ModuleFrame by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteModuleFrame(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request), moduleFrameRepository.findById(id).orElseThrow().getSpo().getId());
            moduleFrameService.deleteModuleFrame(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
}
