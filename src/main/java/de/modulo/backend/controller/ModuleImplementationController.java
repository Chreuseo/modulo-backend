package de.modulo.backend.controller;

import de.modulo.backend.dtos.ModuleImplementationDTO;
import de.modulo.backend.dtos.ModuleImplementationDTOFlat;
import de.modulo.backend.services.ModuleImplementationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/module-implementation")
public class ModuleImplementationController {

    private final ModuleImplementationService moduleImplementationService;

    @Autowired
    public ModuleImplementationController(ModuleImplementationService moduleImplementationService) {
        this.moduleImplementationService = moduleImplementationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ModuleImplementationDTOFlat>> getAllModuleImplementations() {
        return new ResponseEntity<>(moduleImplementationService.getAllModuleImplementations(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<ModuleImplementationDTOFlat> addModuleImplementation(@RequestBody ModuleImplementationDTOFlat moduleImplementationDTOFlat) {
        return new ResponseEntity<>(moduleImplementationService.addModuleImplementation(moduleImplementationDTOFlat), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ModuleImplementationDTO> updateModuleImplementation(@RequestBody ModuleImplementationDTO moduleImplementationDTO) {
        return new ResponseEntity<>(moduleImplementationService.updateModuleImplementation(moduleImplementationDTO), HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteModuleImplementation(@PathVariable Long id) {
        moduleImplementationService.deleteModuleImplementation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleImplementationDTO> getModuleImplementationById(@PathVariable Long id) {
        return new ResponseEntity<>(moduleImplementationService.getModuleImplementationById(id), HttpStatus.OK);
    }

    @GetMapping("/flat/{id}")
    public ResponseEntity<ModuleImplementationDTOFlat> getModuleImplementationFlatById(@PathVariable Long id) {
        return new ResponseEntity<>(moduleImplementationService.getModuleImplementationFlatById(id), HttpStatus.OK);
    }
}
