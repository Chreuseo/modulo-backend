package de.modulo.backend.pdf.services;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class SpoPdfService {
    public ByteArrayOutputStream generateSpo(Long spoId) {
        // Generate SPO
        return new ByteArrayOutputStream();
    }
}
