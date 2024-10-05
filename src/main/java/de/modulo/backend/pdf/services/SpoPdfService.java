package de.modulo.backend.pdf.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import de.modulo.backend.entities.ParagraphEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.ParagraphRepository;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class SpoPdfService {

    private final SpoRepository spoRepository;
    private final ParagraphRepository paragraphRepository;

    @Autowired
    public SpoPdfService(SpoRepository spoRepository,
                         ParagraphRepository paragraphRepository){
        this.spoRepository = spoRepository;
        this.paragraphRepository = paragraphRepository;
    }

    public ByteArrayOutputStream generateSpo(Long spoId) {
        SpoEntity spoEntity = spoRepository.findById(spoId).orElseThrow();
        List<ParagraphEntity> paragraphEntities = paragraphRepository.findBySpoId(spoId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating pdf.", e);
        }

        return byteArrayOutputStream;
    }
}
