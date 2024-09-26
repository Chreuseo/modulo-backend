package de.modulo.backend.dtos;

import lombok.Data;

@Data
public class PasswordDTO {
    private String password;
    private String newPassword;
}
