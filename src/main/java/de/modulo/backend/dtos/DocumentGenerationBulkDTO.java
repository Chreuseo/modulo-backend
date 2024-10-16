package de.modulo.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class DocumentGenerationBulkDTO {
    private List<SpoDTOFlat> spoDTOFlatList;
    private SemesterDTO semesterDTO;
    private boolean studyGuide;
    private boolean moduleManual;
    private boolean spo;
}
