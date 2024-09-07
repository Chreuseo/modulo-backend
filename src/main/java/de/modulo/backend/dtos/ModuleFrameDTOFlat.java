package de.modulo.backend.dtos;

import lombok.Data;

@Data
public class ModuleFrameDTOFlat {

    private long id;
    private String name;

    // No additional methods needed as Lombok will generate getters and setters
}
