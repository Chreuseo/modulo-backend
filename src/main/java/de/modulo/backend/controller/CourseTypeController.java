package de.modulo.backend.controller;
import de.modulo.backend.dtos.CourseTypeDTO;
import de.modulo.backend.services.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-types")
public class CourseTypeController {

    private final CourseTypeService courseTypeService;

    @Autowired
    public CourseTypeController(CourseTypeService courseTypeService) {
        this.courseTypeService = courseTypeService;
    }

    // Get all Course Types
    @GetMapping("/all")
    public ResponseEntity<List<CourseTypeDTO>> getAllCourseTypes() {
        List<CourseTypeDTO> courseTypes = courseTypeService.getAll();
        return new ResponseEntity<>(courseTypes, HttpStatus.OK);
    }

    // Add a new Course Type
    @PostMapping("/new")
    public ResponseEntity<CourseTypeDTO> addCourseType(@RequestBody CourseTypeDTO courseTypeDTO) {
        CourseTypeDTO createdCourseType = courseTypeService.add(courseTypeDTO);
        return new ResponseEntity<>(createdCourseType, HttpStatus.CREATED);
    }

    // Delete a Course Type by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteCourseType(@PathVariable Long id) {
        courseTypeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

