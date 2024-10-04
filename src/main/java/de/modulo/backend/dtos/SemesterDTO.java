package de.modulo.backend.dtos;

import lombok.Data;

@Data
public class SemesterDTO {
    private long id;
    private String name;
    private String abbreviation;
    private String year;
}
