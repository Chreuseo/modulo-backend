package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.ModuleFrameModuleImplementationDTO;
import de.modulo.backend.entities.ModuleFrameModuleImplementationEntity;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.repositories.ModuleFrameModuleImplementationRepository;
import de.modulo.backend.services.data.ModuleFrameModuleImplementationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/module-frame-module-implementations")
public class ModuleFrameModuleImplementationController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.MODULE_FRAME_MODULE_IMPLEMENTATION;

    private final ModuleFrameModuleImplementationService service;
    private final ValidatePrivilegesService validatePrivilegesService;
    private final ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;

    @Autowired
    public ModuleFrameModuleImplementationController(ModuleFrameModuleImplementationService service,
                                                     ValidatePrivilegesService validatePrivilegesService, ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository) {
        this.service = service;
        this.validatePrivilegesService = validatePrivilegesService;
        this.moduleFrameModuleImplementationRepository = moduleFrameModuleImplementationRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ModuleFrameModuleImplementationDTO>> getAll(HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
            List<ModuleFrameModuleImplementationDTO> dtoList = service.findAll();
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            List<ModuleFrameModuleImplementationDTO> dtoList = service.findAll();
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleFrameModuleImplementationDTO> getById(@PathVariable Long id, HttpServletRequest request) {
        try {
            ModuleFrameModuleImplementationEntity entity = moduleFrameModuleImplementationRepository.findById(id).orElseThrow();
            validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request),
                    entity.getModuleFrame().getSpo().getId(), entity.getModuleImplementation().getId());
            ModuleFrameModuleImplementationDTO dto = service.findById(id);
            return dto != null ? new ResponseEntity<>(dto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(NotifyException e){
            e.sendNotification();
            ModuleFrameModuleImplementationDTO dto = service.findById(id);
            return dto != null ? new ResponseEntity<>(dto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/module-frame/{moduleFrameId}")
    public ResponseEntity<List<ModuleFrameModuleImplementationDTO>> getByModuleFrameId(@PathVariable Long moduleFrameId, HttpServletRequest request) {
        try {
            validatePrivilegesService.validateModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request),
                    moduleFrameModuleImplementationRepository.findById(moduleFrameId).orElseThrow().getModuleFrame().getSpo().getId());
            List<ModuleFrameModuleImplementationDTO> dtoList = service.findByModuleFrameId(moduleFrameId);
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            List<ModuleFrameModuleImplementationDTO> dtoList = service.findByModuleFrameId(moduleFrameId);
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/module-implementation/{moduleImplementationId}")
    public ResponseEntity<List<ModuleFrameModuleImplementationDTO>> getByModuleImplementationId(@PathVariable Long moduleImplementationId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), moduleImplementationId);
            List<ModuleFrameModuleImplementationDTO> dtoList = service.findByModuleImplementationId(moduleImplementationId);
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        }catch (NotifyException e) {
            e.sendNotification();
            List<ModuleFrameModuleImplementationDTO> dtoList = service.findByModuleImplementationId(moduleImplementationId);
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ModuleFrameModuleImplementationDTO> create(@RequestBody ModuleFrameModuleImplementationDTO dto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request),
                    dto.getModuleFrameDTO().getSpoDTOFlat().getId(), dto.getModuleImplementationDTOFlat().getId());
            ModuleFrameModuleImplementationDTO createdDto = service.save(dto);
            return new ResponseEntity<>(createdDto, HttpStatus.CREATED);

        }catch(NotifyException e) {
            e.sendNotification();
            ModuleFrameModuleImplementationDTO createdDto = service.save(dto);
            return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ModuleFrameModuleImplementationDTO> update(@RequestBody ModuleFrameModuleImplementationDTO dto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request),
                    dto.getModuleFrameDTO().getSpoDTOFlat().getId(), dto.getModuleImplementationDTOFlat().getId());
            ModuleFrameModuleImplementationDTO updatedDto = service.save(dto);
            return new ResponseEntity<>(updatedDto, HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            ModuleFrameModuleImplementationDTO updatedDto = service.save(dto);
            return new ResponseEntity<>(updatedDto, HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        try{
            ModuleFrameModuleImplementationEntity moduleFrameModuleImplementationEntity = moduleFrameModuleImplementationRepository.findById(id).orElseThrow();
            validatePrivilegesService.validateSpoOrModuleSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request),
                    moduleFrameModuleImplementationEntity.getModuleFrame().getSpo().getId(),
                    moduleFrameModuleImplementationEntity.getModuleImplementation().getId());
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(NotifyException e){
            e.sendNotification();
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
