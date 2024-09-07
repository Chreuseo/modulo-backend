package de.modulo.backend.controller;

import de.modulo.backend.dtos.SectionDTO;
import de.modulo.backend.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @PostMapping("/new")
    public ResponseEntity<SectionDTO> createSection(@RequestBody SectionDTO sectionDTO) {
        SectionDTO createdSection = sectionService.add(sectionDTO);
        return new ResponseEntity<>(createdSection, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        sectionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
