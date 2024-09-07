package de.modulo.backend.dtos;

import lombok.Data;
import java.util.Date;

@Data
public class SpoDTOFlat {
    private long id;
    private String name;
    private DegreeDTO degree; // Full DegreeDTO
    private Date publication;
    private Date validFrom;   // New field
    private Date validUntil;   // New field
}
