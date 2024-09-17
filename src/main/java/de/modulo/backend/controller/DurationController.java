package de.modulo.backend.controller;

import de.modulo.backend.dtos.DurationDTO;
import de.modulo.backend.services.DurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/duration")
public class DurationController {

    private final DurationService durationService;

    @Autowired
    public DurationController(DurationService durationService) {
        this.durationService = durationService;
    }

    // Endpoint to get all durations
    @GetMapping("/all")
    public ResponseEntity<List<DurationDTO>> getAllDurations() {
        List<DurationDTO> durations = durationService.getAll();
        return new ResponseEntity<>(durations, HttpStatus.OK);
    }

    // Endpoint to add a new duration
    @PostMapping("/new")
    public ResponseEntity<DurationDTO> addDuration(@RequestBody DurationDTO durationDto) {
        DurationDTO savedDuration = durationService.add(durationDto);
        return new ResponseEntity<>(savedDuration, HttpStatus.CREATED);
    }

    // Endpoint to delete a duration by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteDuration(@PathVariable Long id) {
        durationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
