package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.SpoDTO;
import de.modulo.backend.dtos.SpoDTOFlat;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.SpoRepository;
import de.modulo.backend.services.SpoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spo")
public class SpoController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.SPO;

    private final SpoService spoService;
    private final ValidatePrivilegesService validatePrivilegesService;
    private final SpoRepository spoRepository;

    @Autowired
    public SpoController(SpoService spoService,
                         ValidatePrivilegesService validatePrivilegesService, SpoRepository spoRepository) {
        this.spoService = spoService;
        this.validatePrivilegesService = validatePrivilegesService;
        this.spoRepository = spoRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SpoDTOFlat>> getAllSpos(HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(spoService.getAllSpos(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<SpoDTOFlat> addSpo(@RequestBody SpoDTOFlat spoDto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(spoService.add(spoDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteSpo(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request),
                    spoRepository.findById(id).orElseThrow().getId());
            spoService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<SpoDTO> updateSpo(@RequestBody SpoDTO spoDto, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request), spoDto.getId());
            return new ResponseEntity<>(spoService.update(spoDto), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SpoDTO> getSpo(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), id);
            return new ResponseEntity<>(spoService.getSpo(id), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("{id}/responsible/add/{userId}")
    public ResponseEntity<Void> addResponsible(@PathVariable Long id, @PathVariable Long userId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE_PRIVILEGES, SessionTokenHelper.getSessionToken(request), id);
            spoService.addResponsible(id, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("{id}/responsible/remove/{userId}")
    public ResponseEntity<Void> removeResponsible(@PathVariable Long id, @PathVariable Long userId, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateSpoSpecificPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE_PRIVILEGES, SessionTokenHelper.getSessionToken(request), id);
            spoService.removeResponsible(id, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
