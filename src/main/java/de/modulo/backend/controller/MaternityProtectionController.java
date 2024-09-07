package de.modulo.backend.controller;
import de.modulo.backend.dtos.MaternityProtectionDTO;
import de.modulo.backend.services.MaternityProtectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maternity-protection")
public class MaternityProtectionController {

    private final MaternityProtectionService maternityProtectionService;

    @Autowired
    public MaternityProtectionController(MaternityProtectionService maternityProtectionService) {
        this.maternityProtectionService = maternityProtectionService;
    }

    // Endpoint to get all maternity protection entries
    @GetMapping("/all")
    public ResponseEntity<List<MaternityProtectionDTO>> getAllMaternityProtections() {
        List<MaternityProtectionDTO> protections = maternityProtectionService.getAll();
        return new ResponseEntity<>(protections, HttpStatus.OK);
    }

    // Endpoint to add a new maternity protection entry
    @PostMapping("/new")
    public ResponseEntity<MaternityProtectionDTO> addMaternityProtection(@RequestBody MaternityProtectionDTO maternityProtectionDto) {
        MaternityProtectionDTO savedProtection = maternityProtectionService.add(maternityProtectionDto);
        return new ResponseEntity<>(savedProtection, HttpStatus.CREATED);
    }

    // Endpoint to delete a maternity protection entry by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteMaternityProtection(@PathVariable Long id) {
        maternityProtectionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
