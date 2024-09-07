package de.modulo.backend.controller;

import de.modulo.backend.dtos.SpoDTO;
import de.modulo.backend.dtos.SpoDTOFlat;
import de.modulo.backend.services.SpoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spo")
public class SpoController {

    private final SpoService spoService;

    @Autowired
    public SpoController(SpoService spoService) {
        this.spoService = spoService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SpoDTOFlat>> getAllSpos() {
        return new ResponseEntity<>(spoService.getAllSpos(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<SpoDTOFlat> addSpo(@RequestBody SpoDTOFlat spoDto) {
        return new ResponseEntity<>(spoService.add(spoDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteSpo(@PathVariable Long id) {
        spoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<SpoDTO> updateSpo(@RequestBody SpoDTO spoDto) {
        return new ResponseEntity<>(spoService.update(spoDto), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SpoDTO> getSpo(@PathVariable Long id) {
        return new ResponseEntity<>(spoService.getSpo(id), HttpStatus.OK);
    }
}
