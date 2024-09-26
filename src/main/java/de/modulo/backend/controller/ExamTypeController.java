package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.services.ExamTypeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam-types")
public class ExamTypeController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.SPO_SETTINGS;

    private final ExamTypeService examTypeService;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public ExamTypeController(ExamTypeService examTypeService,
                              ValidatePrivilegesService validatePrivilegesService) {
        this.examTypeService = examTypeService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    @GetMapping("/spo/{spoId}")
    public ResponseEntity<List<ExamTypeDTO>> getBySpo(@PathVariable long spoId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<ExamTypeDTO> examTypes = examTypeService.getBySpo(spoId);
        return new ResponseEntity<>(examTypes, HttpStatus.OK);
    }

    @GetMapping("/module-frame/{moduleFrameId}")
    public ResponseEntity<List<ExamTypeDTO>> getByModuleFrame(@PathVariable long moduleFrameId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<ExamTypeDTO> examTypes = examTypeService.getByModuleFrame(moduleFrameId);
        return new ResponseEntity<>(examTypes, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ExamTypeDTO> add(@RequestBody ExamTypeDTO dto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ExamTypeDTO createdDto = examTypeService.add(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> remove(@PathVariable long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        examTypeService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<ExamTypeDTO> update(@RequestBody ExamTypeDTO dto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ExamTypeDTO updatedDto = examTypeService.update(dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }
}
