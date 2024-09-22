package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionService;
import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.dtos.SpoDTO;
import de.modulo.backend.dtos.SpoDTOFlat;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.services.SpoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/spo")
public class SpoController {

    private final SpoService spoService;
    private final SessionService sessionService;

    @Autowired
    public SpoController(SpoService spoService,
                         SessionService sessionService) {
        this.spoService = spoService;
        this.sessionService = sessionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SpoDTOFlat>> getAllSpos() {
        return new ResponseEntity<>(spoService.getAllSpos(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<SpoDTOFlat> addSpo(@RequestBody SpoDTOFlat spoDto, HttpServletRequest request) {
        if(!sessionService.getRoleBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request))).equals(ROLE.ADMIN)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(spoService.add(spoDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteSpo(@PathVariable Long id, HttpServletRequest request) {
        if(!sessionService.getRoleBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request))).equals(ROLE.ADMIN)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        spoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<SpoDTO> updateSpo(@RequestBody SpoDTO spoDto, HttpServletRequest request) {
        if(!sessionService.getRoleBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request))).equals(ROLE.ADMIN)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(spoService.update(spoDto), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SpoDTO> getSpo(@PathVariable Long id) {
        return new ResponseEntity<>(spoService.getSpo(id), HttpStatus.OK);
    }
}
