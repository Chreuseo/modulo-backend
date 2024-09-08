package de.modulo.backend.controller;

import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.dtos.ModuleFrameSetDTO;
import de.modulo.backend.services.ModuleFrameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/module-frames")
public class ModuleFrameController {

    @Autowired
    private ModuleFrameService moduleFrameService;

    // Endpoint to get ModuleFrameSetDTO by SPO ID
    @GetMapping("/spo/{spoId}")
    public ResponseEntity<ModuleFrameSetDTO> getModuleFrameSetBySpoId(@PathVariable Long spoId) {
        ModuleFrameSetDTO moduleFrameSetDTO = moduleFrameService.getModuleFrameSetDTOBySpoId(spoId);
        return ResponseEntity.ok(moduleFrameSetDTO);
    }

    // Endpoint to add a new ModuleFrame
    @PostMapping("/new")
    public ResponseEntity<ModuleFrameDTO> addModuleFrame(@RequestBody ModuleFrameDTO moduleFrameDTO) {
        ModuleFrameDTO createdModuleFrame = moduleFrameService.addModuleFrame(moduleFrameDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModuleFrame);
    }

    // Endpoint to update an existing ModuleFrame
    @PutMapping("/update")
    public ResponseEntity<ModuleFrameDTO> updateModuleFrame(@RequestBody ModuleFrameDTO moduleFrameDTO) {
        ModuleFrameDTO result = moduleFrameService.updateModuleFrame(moduleFrameDTO);
        return ResponseEntity.ok(result);
    }

    // Endpoint to delete a ModuleFrame by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModuleFrame(@PathVariable Long id) {
        moduleFrameService.deleteModuleFrame(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
