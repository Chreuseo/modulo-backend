package de.modulo.backend.dtos;

import lombok.Data;

@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class ExamTypeDTO {

    private long id;
    private String name;
    private String length;
    private boolean enabled;
    private boolean mandatory;
}
