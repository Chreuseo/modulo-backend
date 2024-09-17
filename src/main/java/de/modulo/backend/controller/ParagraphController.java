package de.modulo.backend.controller;

import de.modulo.backend.dtos.ParagraphDTO;
import de.modulo.backend.services.ParagraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paragraphs")
public class ParagraphController {

    private final ParagraphService paragraphService;

    @Autowired
    public ParagraphController(ParagraphService paragraphService) {
        this.paragraphService = paragraphService;
    }

    @PostMapping
    public ResponseEntity<ParagraphDTO> addParagraph(@RequestBody ParagraphDTO paragraphDTO) {
        ParagraphDTO createdParagraph = paragraphService.addParagraph(paragraphDTO);
        return new ResponseEntity<>(createdParagraph, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ParagraphDTO> updateParagraph(@PathVariable Long id, @RequestBody ParagraphDTO paragraphDTO) {
        // Ensure the ID in the DTO matches the path variable
        paragraphDTO.setId(id);
        ParagraphDTO updatedParagraph = paragraphService.updateParagraph(paragraphDTO);
        return new ResponseEntity<>(updatedParagraph, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteParagraph(@PathVariable Long id) {
        paragraphService.deleteParagraph(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/spo/{spoId}")
    public ResponseEntity<List<ParagraphDTO>> getParagraphsBySpo(@PathVariable Long spoId) {
        List<ParagraphDTO> paragraphs = paragraphService.getParagraphsBySpo(spoId);
        return new ResponseEntity<>(paragraphs, HttpStatus.OK);
    }
}