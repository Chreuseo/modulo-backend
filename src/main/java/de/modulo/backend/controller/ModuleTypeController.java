package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ModuleTypeDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.ModuleTypeRepository;
import de.modulo.backend.services.ModuleTypeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/module-type")
public class ModuleTypeController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.SPO_SETTINGS;

    private final ModuleTypeService moduleTypeService;
    private final ValidatePrivilegesService validatePrivilegesService;
    private final ModuleTypeRepository moduleTypeRepository;

    @Autowired
    public ModuleTypeController(ModuleTypeService moduleTypeService,
                                ValidatePrivilegesService validatePrivilegesService, ModuleTypeRepository moduleTypeRepository) {
        this.moduleTypeService = moduleTypeService;
        this.validatePrivilegesService = validatePrivilegesService;
        this.moduleTypeRepository = moduleTypeRepository;
    }

    @PostMapping("/new")
    public ResponseEntity<ModuleTypeDTO> createModuleType(@RequestBody ModuleTypeDTO moduleTypeDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request), moduleTypeDTO.getSpoId());
            return new ResponseEntity<>(moduleTypeService.add(moduleTypeDTO), HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteModuleType(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request),
                    moduleTypeRepository.findById(id).orElseThrow().getSpo().getId());
            moduleTypeService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
