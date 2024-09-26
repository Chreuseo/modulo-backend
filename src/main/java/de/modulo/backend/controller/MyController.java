package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionService;
import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.converters.UserConverter;
import de.modulo.backend.dtos.PasswordDTO;
import de.modulo.backend.dtos.UserDTO;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/my")
public class MyController {

    private final SessionService sessionService;
    private final UserConverter userConverter;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MyController(SessionService sessionService,
                        UserConverter userConverter,
                        UserService userService,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.sessionService = sessionService;
        this.userConverter = userConverter;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/data")
    public ResponseEntity<UserDTO> getUser(HttpServletRequest request){
        UserEntity user = sessionService.getUserBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request)));
        UserDTO userDTO = userConverter.toDto(user);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, HttpServletRequest request){
        UserEntity user = sessionService.getUserBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request)));
        userDTO.setRole(user.getRole().toString());

        if(!user.getId().equals(userDTO.getId())){
            return ResponseEntity.badRequest().build();
        }

        UserDTO updatedUserDTO = userService.updateUser(userDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @PutMapping("/update-password")
    public ResponseEntity<UserDTO> updatePassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request){
        UserEntity user = sessionService.getUserBySessionId(UUID.fromString(SessionTokenHelper.getSessionToken(request)));

        if(!Objects.requireNonNull(bCryptPasswordEncoder.encode(passwordDTO.getPassword())).equals(user.getPassword())){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userService.changePassword(user, bCryptPasswordEncoder.encode(passwordDTO.getNewPassword())));
    }
}
