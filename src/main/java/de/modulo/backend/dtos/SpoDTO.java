package de.modulo.backend.dtos;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class SpoDTO {
    private long id;
    private String header;
    private String footer;
    private String name;
    private Date publication;
    private Date validFrom;
    private Date validUntil;
    private List<SectionDTO> sectionDTOs;     // List of SectionDTOs
    private List<ModuleTypeDTO> moduleTypeDTOs; // List of ModuleTypeDTOs
    private DegreeDTO degree;                // Full DegreeDTO instead of an ID
}
