package de.modulo.backend.controller;

import de.modulo.backend.dtos.ModuleImplementationDTOFlat;
import de.modulo.backend.services.ModuleImplementationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/module-implementation")
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
}
