package de.modulo.backend.dtos;

import lombok.Data;

@Data
public class ParagraphDTO {

    private Long id;
    private Long spoId;
    private String title;
    private String text;
}
