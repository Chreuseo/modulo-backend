package de.modulo.backend.controller;

import de.modulo.backend.dtos.ModuleTypeDTO;
import de.modulo.backend.services.ModuleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/module-type")
public class ModuleTypeController {

    @Autowired
    private ModuleTypeService moduleTypeService;

    @PostMapping("/new")
    public ResponseEntity<ModuleTypeDTO> createModuleType(@RequestBody ModuleTypeDTO moduleTypeDTO) {
        return new ResponseEntity<>(moduleTypeService.add(moduleTypeDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteModuleType(@PathVariable Long id) {
        moduleTypeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
