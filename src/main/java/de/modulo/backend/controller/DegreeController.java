package de.modulo.backend.controller;

import de.modulo.backend.dtos.DegreeDTO;
import de.modulo.backend.services.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/degree")
public class DegreeController {

    private final DegreeService degreeService;

    @Autowired
    public DegreeController(DegreeService degreeService) {
        this.degreeService = degreeService;
    }

    // Endpoint to get all degrees
    @GetMapping("/all")
    public ResponseEntity<List<DegreeDTO>> getAllDegrees() {
        List<DegreeDTO> degrees = degreeService.getAll();
        return new ResponseEntity<>(degrees, HttpStatus.OK);
    }

    // Endpoint to add a new degree
    @PostMapping("/new")
    public ResponseEntity<DegreeDTO> addDegree(@RequestBody DegreeDTO degreeDTO) {
        DegreeDTO savedDegree = degreeService.add(degreeDTO);
        return new ResponseEntity<>(savedDegree, HttpStatus.CREATED);
    }

    // Endpoint to delete a degree by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteDegree(@PathVariable Long id) {
        degreeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
