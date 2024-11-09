package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.SemesterDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.services.data.SemesterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/semester")
public class SemesterController {

    private final SemesterService semesterService;
    private final ValidatePrivilegesService validatePrivilegesService;

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.GENERAL_SETTINGS;

    public SemesterController(SemesterService semesterService,
                              ValidatePrivilegesService validatePrivilegesService) {
        this.semesterService = semesterService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SemesterDTO>> getAllSemesters(HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
            return ResponseEntity.ok(semesterService.getAllSemesters());
        }catch(NotifyException e){
            e.sendNotification();
            return ResponseEntity.ok(semesterService.getAllSemesters());
        } catch (InsufficientPermissionsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<SemesterDTO> addSemester(@RequestBody SemesterDTO semesterDTO, HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
            return ResponseEntity.ok(semesterService.addSemester(semesterDTO));
        }catch(NotifyException e){
            e.sendNotification();
            return ResponseEntity.ok(semesterService.addSemester(semesterDTO));
        } catch (InsufficientPermissionsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<SemesterDTO> updateSemester(@RequestBody SemesterDTO semesterDTO, HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request));
            return ResponseEntity.ok(semesterService.updateSemester(semesterDTO));
        }catch(NotifyException e){
            e.sendNotification();
            return ResponseEntity.ok(semesterService.updateSemester(semesterDTO));
        } catch (InsufficientPermissionsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeSemester(@PathVariable long id, HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
            semesterService.removeSemester(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(NotifyException e){
            e.sendNotification();
            semesterService.removeSemester(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InsufficientPermissionsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
