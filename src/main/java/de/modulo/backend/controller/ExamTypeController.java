package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.ExamTypeRepository;
import de.modulo.backend.repositories.ModuleFrameRepository;
import de.modulo.backend.services.data.ExamTypeService;
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
    private final ModuleFrameRepository moduleFrameRepository;
    private final ExamTypeRepository examTypeRepository;

    @Autowired
    public ExamTypeController(ExamTypeService examTypeService,
                              ValidatePrivilegesService validatePrivilegesService, ModuleFrameRepository moduleFrameRepository, ExamTypeRepository examTypeRepository) {
        this.examTypeService = examTypeService;
        this.validatePrivilegesService = validatePrivilegesService;
        this.moduleFrameRepository = moduleFrameRepository;
        this.examTypeRepository = examTypeRepository;
    }

    @GetMapping("/spo/{spoId}")
    public ResponseEntity<List<ExamTypeDTO>> getBySpo(@PathVariable long spoId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), spoId);
            List<ExamTypeDTO> examTypes = examTypeService.getBySpo(spoId);
            return new ResponseEntity<>(examTypes, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/module-frame/{moduleFrameId}")
    public ResponseEntity<List<ExamTypeDTO>> getByModuleFrame(@PathVariable long moduleFrameId, HttpServletRequest request) {
        try{
            Long spoId = moduleFrameRepository.findById(moduleFrameId).orElseThrow().getSpo().getId();
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), spoId);
            List<ExamTypeDTO> examTypes = examTypeService.getByModuleFrame(moduleFrameId);
            return new ResponseEntity<>(examTypes, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ExamTypeDTO> add(@RequestBody ExamTypeDTO dto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request), dto.getSpoId());
            ExamTypeDTO createdDto = examTypeService.add(dto);
            return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> remove(@PathVariable long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request), examTypeRepository.findById(id).orElseThrow().getSpo().getId());
            examTypeService.remove(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ExamTypeDTO> update(@RequestBody ExamTypeDTO dto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request), dto.getSpoId());
            ExamTypeDTO updatedDto = examTypeService.update(dto);
            return new ResponseEntity<>(updatedDto, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
}
