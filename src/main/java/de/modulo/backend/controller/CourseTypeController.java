package de.modulo.backend.controller;
import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.CourseTypeDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.services.data.CourseTypeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-types")
public class CourseTypeController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.SPO_SETTINGS;

    private final CourseTypeService courseTypeService;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public CourseTypeController(CourseTypeService courseTypeService,
                                ValidatePrivilegesService validatePrivilegesService) {
        this.courseTypeService = courseTypeService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    // Get all Course Types
    @GetMapping("/all")
    public ResponseEntity<List<CourseTypeDTO>> getAllCourseTypes(HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
            List<CourseTypeDTO> courseTypes = courseTypeService.getAll();
            return new ResponseEntity<>(courseTypes, HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            List<CourseTypeDTO> courseTypes = courseTypeService.getAll();
            return new ResponseEntity<>(courseTypes, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Add a new Course Type
    @PostMapping("/new")
    public ResponseEntity<CourseTypeDTO> addCourseType(@RequestBody CourseTypeDTO courseTypeDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
            CourseTypeDTO createdCourseType = courseTypeService.add(courseTypeDTO);
            return new ResponseEntity<>(createdCourseType, HttpStatus.CREATED);
        }catch(NotifyException e){
            e.sendNotification();
            CourseTypeDTO createdCourseType = courseTypeService.add(courseTypeDTO);
            return new ResponseEntity<>(createdCourseType, HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Delete a Course Type by ID
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteCourseType(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
            courseTypeService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(NotifyException e){
            e.sendNotification();
            courseTypeService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}

