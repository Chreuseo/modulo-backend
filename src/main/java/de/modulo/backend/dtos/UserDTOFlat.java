package de.modulo.backend.dtos;

import lombok.Data;

@Data
public class UserDTOFlat {

    private Long id;
    private String mail;
    private String name;
    private String code;
}
