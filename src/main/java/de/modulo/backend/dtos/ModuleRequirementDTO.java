package de.modulo.backend.dtos;

import lombok.Data;

@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class ModuleRequirementDTO {

    private long id;
    private long spoId; // Assuming that SpoDTOFlat has a long id
    private String name;
}
