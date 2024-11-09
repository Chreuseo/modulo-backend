package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ModuleRequirementDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.ModuleRequirementRepository;
import de.modulo.backend.services.data.ModuleRequirementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/module-requirements")
public class ModuleRequirementController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.SPO_SETTINGS;

    private final ModuleRequirementService moduleRequirementService;
    private final ValidatePrivilegesService validatePrivilegesService;
    private final ModuleRequirementRepository moduleRequirementRepository;

    @Autowired
    public ModuleRequirementController(ModuleRequirementService moduleRequirementService,
                                       ValidatePrivilegesService validatePrivilegesService, ModuleRequirementRepository moduleRequirementRepository) {
        this.moduleRequirementService = moduleRequirementService;
        this.validatePrivilegesService = validatePrivilegesService;
        this.moduleRequirementRepository = moduleRequirementRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<ModuleRequirementDTO> addModuleRequirement(@RequestBody ModuleRequirementDTO moduleRequirementDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request), moduleRequirementDTO.getSpoId());
            ModuleRequirementDTO createdModuleRequirement = moduleRequirementService.addModuleRequirement(moduleRequirementDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdModuleRequirement);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request),
                    moduleRequirementRepository.findById(id).orElseThrow().getSpo().getId());
            moduleRequirementService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ModuleRequirementDTO> update(@RequestBody ModuleRequirementDTO moduleRequirementDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request), moduleRequirementDTO.getSpoId());
            ModuleRequirementDTO updatedModuleRequirement = moduleRequirementService.update(moduleRequirementDTO);
            return ResponseEntity.ok(updatedModuleRequirement);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/spo/{spoId}")
    public ResponseEntity<List<ModuleRequirementDTO>> getBySpoId(@PathVariable long spoId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), spoId);
            List<ModuleRequirementDTO> moduleRequirements = moduleRequirementService.getBySpoId(spoId);
            return ResponseEntity.ok(moduleRequirements);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
