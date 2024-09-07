package de.modulo.backend.dtos;

import lombok.Data;

@Data
public class CourseTypeDTO {

    private Long id;
    private String name;
    private String abbreviation;
    private boolean enabled;
}
