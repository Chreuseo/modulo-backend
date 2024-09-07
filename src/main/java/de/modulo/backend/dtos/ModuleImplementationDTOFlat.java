package de.modulo.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ModuleImplementationDTOFlat {
    private Long id;
    private String name;
    private String abbreviation;
    private List<SpoDTOFlat> spos;
}
