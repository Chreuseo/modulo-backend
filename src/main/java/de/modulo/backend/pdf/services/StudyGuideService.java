package de.modulo.backend.pdf.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import de.modulo.backend.repositories.ModuleFrameModuleImplementationRepository;
import de.modulo.backend.repositories.ModuleFrameRepository;
import de.modulo.backend.repositories.SpoRepository;
import de.modulo.backend.services.ModuleFrameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class StudyGuideService {
    private final SpoRepository spoRepository;
    private final ModuleFrameService moduleFrameService;
    private final ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;
    private final ModuleFrameRepository moduleFrameRepository;

    @Autowired
    public StudyGuideService(SpoRepository spoRepository,
                             ModuleFrameService moduleFrameService,
                             ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository,
                             ModuleFrameRepository moduleFrameRepository){
        this.spoRepository = spoRepository;
        this.moduleFrameService = moduleFrameService;
        this.moduleFrameModuleImplementationRepository = moduleFrameModuleImplementationRepository;
        this.moduleFrameRepository = moduleFrameRepository;
    }

    public ByteArrayOutputStream generateStudyGuide(Long spoId, Long semesterId) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating pdf.", e);
        }

        return byteArrayOutputStream;    }
}
