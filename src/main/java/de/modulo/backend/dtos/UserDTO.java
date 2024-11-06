package de.modulo.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String mail;
    private String title;
    private String firstName;
    private String lastName;
    private String code;
    private String role; // Assuming role is a String; you can change it accordingly
    private boolean sendMailNotifications = false;
}
