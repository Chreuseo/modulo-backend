package de.modulo.backend.controller;

import de.modulo.backend.dtos.ModuleRequirementDTO;
import de.modulo.backend.services.ModuleRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/module-requirements")
public class ModuleRequirementController {

    @Autowired
    private ModuleRequirementService moduleRequirementService;

    @PostMapping("/add")
    public ResponseEntity<ModuleRequirementDTO> addModuleRequirement(@RequestBody ModuleRequirementDTO moduleRequirementDTO) {
        ModuleRequirementDTO createdModuleRequirement = moduleRequirementService.addModuleRequirement(moduleRequirementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModuleRequirement);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        moduleRequirementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<ModuleRequirementDTO> update(@RequestBody ModuleRequirementDTO moduleRequirementDTO) {
        ModuleRequirementDTO updatedModuleRequirement = moduleRequirementService.update(moduleRequirementDTO);
        return ResponseEntity.ok(updatedModuleRequirement);
    }

    @GetMapping("/spo/{spoId}")
    public ResponseEntity<List<ModuleRequirementDTO>> getBySpoId(@PathVariable long spoId) {
        List<ModuleRequirementDTO> moduleRequirements = moduleRequirementService.getBySpoId(spoId);
        return ResponseEntity.ok(moduleRequirements);
    }
}
