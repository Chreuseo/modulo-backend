package de.modulo.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SpoDocumentsDTO {
    private SpoDTOFlat spo;
    private List<Document> documents;
    private List<Semester> semesters;

    @Data
    public static class Semester {
        private SemesterDTO semester;
        private List<Document> documents;
    }

    @Data
    public static class Document {
        private String name;
        private String friendlyName;
    }
}
