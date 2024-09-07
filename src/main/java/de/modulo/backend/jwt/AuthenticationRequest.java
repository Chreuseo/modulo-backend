package de.modulo.backend.jwt;

import de.modulo.backend.dtos.UserDTOAuth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String mail;
    private String password;
}