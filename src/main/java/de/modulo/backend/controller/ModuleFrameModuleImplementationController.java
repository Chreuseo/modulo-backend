package de.modulo.backend.controller;

import de.modulo.backend.dtos.ModuleFrameModuleImplementationDTO;
import de.modulo.backend.services.ModuleFrameModuleImplementationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/module-frame-module-implementations")
public class ModuleFrameModuleImplementationController {

    private final ModuleFrameModuleImplementationService service;

    @Autowired
    public ModuleFrameModuleImplementationController(ModuleFrameModuleImplementationService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ModuleFrameModuleImplementationDTO>> getAll() {
        List<ModuleFrameModuleImplementationDTO> dtoList = service.findAll();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleFrameModuleImplementationDTO> getById(@PathVariable Long id) {
        ModuleFrameModuleImplementationDTO dto = service.findById(id);
        return dto != null ? new ResponseEntity<>(dto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/module-frame/{moduleFrameId}")
    public ResponseEntity<List<ModuleFrameModuleImplementationDTO>> getByModuleFrameId(@PathVariable Long moduleFrameId) {
        List<ModuleFrameModuleImplementationDTO> dtoList = service.findByModuleFrameId(moduleFrameId);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/module-implementation/{moduleImplementationId}")
    public ResponseEntity<List<ModuleFrameModuleImplementationDTO>> getByModuleImplementationId(@PathVariable Long moduleImplementationId) {
        List<ModuleFrameModuleImplementationDTO> dtoList = service.findByModuleImplementationId(moduleImplementationId);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ModuleFrameModuleImplementationDTO> create(@RequestBody ModuleFrameModuleImplementationDTO dto) {
        ModuleFrameModuleImplementationDTO createdDto = service.save(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ModuleFrameModuleImplementationDTO> update(@RequestBody ModuleFrameModuleImplementationDTO dto) {
        ModuleFrameModuleImplementationDTO updatedDto = service.save(dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
