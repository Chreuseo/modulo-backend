package de.modulo.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentDTO {
    public String name;
    public byte[] content;
}
