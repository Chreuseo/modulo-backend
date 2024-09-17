package de.modulo.backend.controller;

import de.modulo.backend.dtos.CycleDTO;
import de.modulo.backend.services.CycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cycle")
public class CycleController {

    @Autowired
    private CycleService cycleService;

    @PostMapping("/add")
    public ResponseEntity<CycleDTO> addCycle(@RequestBody CycleDTO cycleDto) {
        CycleDTO createdCycle = cycleService.addCycle(cycleDto);
        return new ResponseEntity<>(createdCycle, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<CycleDTO> updateCycle(@RequestBody CycleDTO cycleDto) {
        CycleDTO updatedCycle = cycleService.updateCycle(cycleDto);
        return new ResponseEntity<>(updatedCycle, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeCycle(@PathVariable Long id) {
        cycleService.removeCycle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CycleDTO>> getAllCycles() {
        List<CycleDTO> cycles = cycleService.getAllCycles();
        return new ResponseEntity<>(cycles, HttpStatus.OK);
    }
}
