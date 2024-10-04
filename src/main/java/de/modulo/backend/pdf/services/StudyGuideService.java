package de.modulo.backend.pdf.services;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class StudyGuideService {
    public ByteArrayOutputStream generateStudyGuide(Long spoId, Long semesterId) {
        // Generate study guide
        return new ByteArrayOutputStream();
    }
}
