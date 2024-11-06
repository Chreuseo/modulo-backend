package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionService;
import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.SpoDTO;
import de.modulo.backend.dtos.SpoDTOFlat;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.repositories.SpoRepository;
import de.modulo.backend.services.SpoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/spo")
public class SpoController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.SPO;

    private final SpoService spoService;
    private final ValidatePrivilegesService validatePrivilegesService;
    private final SpoRepository spoRepository;
    private final SessionService sessionService;

    @Autowired
    public SpoController(SpoService spoService,
                         ValidatePrivilegesService validatePrivilegesService, SpoRepository spoRepository, SessionService sessionService) {
        this.spoService = spoService;
        this.validatePrivilegesService = validatePrivilegesService;
        this.spoRepository = spoRepository;
        this.sessionService = sessionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SpoDTOFlat>> getAllSpos(HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
            return new ResponseEntity<>(spoService.getAllSpos(), HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(spoService.getAllSpos(), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<SpoDTOFlat> addSpo(@RequestBody SpoDTOFlat spoDto, HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));

            if(sessionService.getRoleBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request))) == ROLE.ADMIN){
                return new ResponseEntity<>(spoService.add(spoDto), HttpStatus.CREATED);
            }else{
                SpoDTOFlat spo = spoService.add(spoDto);
                spoService.addResponsible(spoDto.getId(), sessionService.getUserIdBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request))));
                return new ResponseEntity<>(spo, HttpStatus.CREATED);
            }
        }catch (NotifyException e){
            SpoDTOFlat spo = spoService.add(spoDto);
            spoService.addResponsible(spo.getId(), sessionService.getUserIdBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request))));
            SpoEntity spoEntity = spoRepository.findById(spo.getId()).orElseThrow();
            e.setEditedObject(new Object[] {spoEntity});

            e.sendNotification();

            return new ResponseEntity<>(spo, HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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
