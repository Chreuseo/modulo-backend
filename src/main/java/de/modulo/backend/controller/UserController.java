package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.UserDTO;
import de.modulo.backend.dtos.UserDTOFlat;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.USER;

    private final UserService userService;
    private final ValidatePrivilegesService validatePrivilegesService;

    @Autowired
    public UserController(UserService userService,
                          ValidatePrivilegesService validatePrivilegesService) {
        this.userService = userService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTOFlat>> getAllUsers(HttpServletRequest request) {
        try {
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("role/{role}")
    public ResponseEntity<List<UserDTOFlat>> getUsersByRole(@PathVariable String role, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
            return new ResponseEntity<>(userService.getUsersByRole(ROLE.valueOf(role)), HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(userService.getUsersByRole(ROLE.valueOf(role)), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ_DETAILS, SessionTokenHelper.getSessionToken(request));
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request));
            return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
        }catch(NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.DELETE, SessionTokenHelper.getSessionToken(request));
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(NotifyException e){
            e.sendNotification();
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request));
            return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/password-reset/{id}")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id, HttpServletRequest request) {
        try{
            validatePrivilegesService.validateGeneralPrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.UPDATE, SessionTokenHelper.getSessionToken(request));
            userService.resetPassword(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(NotifyException e){
            e.sendNotification();
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (InsufficientPermissionsException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
