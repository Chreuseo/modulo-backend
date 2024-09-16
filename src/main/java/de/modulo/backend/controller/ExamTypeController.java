package de.modulo.backend.controller;

import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.services.ExamTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-types")
public class ExamTypeController {

    @Autowired
    private ExamTypeService examTypeService;

    @GetMapping("/spo/{spoId}")
    public ResponseEntity<List<ExamTypeDTO>> getBySpo(@PathVariable long spoId) {
        List<ExamTypeDTO> examTypes = examTypeService.getBySpo(spoId);
        return new ResponseEntity<>(examTypes, HttpStatus.OK);
    }

    @GetMapping("/module-frame/{moduleFrameId}")
    public ResponseEntity<List<ExamTypeDTO>> getByModuleFrame(@PathVariable long moduleFrameId) {
        List<ExamTypeDTO> examTypes = examTypeService.getByModuleFrame(moduleFrameId);
        return new ResponseEntity<>(examTypes, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ExamTypeDTO> add(@RequestBody ExamTypeDTO dto) {
        ExamTypeDTO createdDto = examTypeService.add(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> remove(@PathVariable long id) {
        examTypeService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<ExamTypeDTO> update(@RequestBody ExamTypeDTO dto) {
        ExamTypeDTO updatedDto = examTypeService.update(dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }
}
