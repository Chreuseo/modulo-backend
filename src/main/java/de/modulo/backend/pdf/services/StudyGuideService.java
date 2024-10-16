package de.modulo.backend.pdf.services;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.dtos.ModuleFrameSetDTO;
import de.modulo.backend.entities.ModuleFrameModuleImplementationEntity;
import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.SemesterEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.ModuleFrameModuleImplementationRepository;
import de.modulo.backend.repositories.ModuleFrameRepository;
import de.modulo.backend.repositories.SemesterRepository;
import de.modulo.backend.repositories.SpoRepository;
import de.modulo.backend.services.ModuleFrameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class StudyGuideService {
    private final SpoRepository spoRepository;
    private final ModuleFrameService moduleFrameService;
    private final ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;
    private final ModuleFrameRepository moduleFrameRepository;
    private final SemesterRepository semesterRepository;

    @Autowired
    public StudyGuideService(SpoRepository spoRepository,
                             ModuleFrameService moduleFrameService,
                             ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository,
                             ModuleFrameRepository moduleFrameRepository, SemesterRepository semesterRepository){
        this.spoRepository = spoRepository;
        this.moduleFrameService = moduleFrameService;
        this.moduleFrameModuleImplementationRepository = moduleFrameModuleImplementationRepository;
        this.moduleFrameRepository = moduleFrameRepository;
        this.semesterRepository = semesterRepository;
    }

    public ByteArrayOutputStream generateStudyGuide(Long spoId, Long semesterId) {
        SpoEntity spoEntity = spoRepository.findById(spoId).orElseThrow();
        SemesterEntity semesterEntity = semesterRepository.findById(semesterId).orElseThrow();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4.rotate());

            document.setMargins(40, 40, 40, 40);

            Paragraph paragraph = new Paragraph();
            paragraph.add("Studienplan für die theoretischen und praktischen Studiensemester des Studiengangs "
            + spoEntity.getDegree().getName() + " "
            + spoEntity.getName()
            + " an der Hochschule Coburg");
            paragraph.setFontSize(14);
            paragraph.setBold();
            paragraph.setTextAlignment(TextAlignment.CENTER);
            document.add(paragraph);

            paragraph = new Paragraph();
            paragraph.add("(gültig für Studienanfänger ");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            if(spoEntity.getValidFrom() != null){
                paragraph.add("ab " + simpleDateFormat.format(spoEntity.getValidFrom()));
            }
            if (spoEntity.getValidUntil() != null){
                paragraph.add(" bis " + simpleDateFormat.format(spoEntity.getValidUntil()));
            }
            paragraph.add(")");
            paragraph.setFontSize(14);
            paragraph.setBold();
            paragraph.setTextAlignment(TextAlignment.CENTER);
            document.add(paragraph);

            paragraph = new Paragraph();
            paragraph.add("(" + semesterEntity.getName() + " " + semesterEntity.getYear() + ")");
            paragraph.setFontSize(14);
            paragraph.setBold();
            paragraph.setTextAlignment(TextAlignment.CENTER);
            document.add(paragraph);

            ModuleFrameSetDTO moduleFrameSetDTO = moduleFrameService.getModuleFrameSetDTOBySpoId(spoId);

            int sectionCounter = 0;
            for(ModuleFrameSetDTO.Section section : moduleFrameSetDTO.getSections()) {
                if(!section.getModuleTypes().isEmpty()) {
                    String headline = (++sectionCounter) + ". " + section.getName();
                    paragraph = new Paragraph(headline);
                    document.add(paragraph);
                }
                int moduleTypeCounter = 0;
                for(ModuleFrameSetDTO.Section.ModuleType moduleType : section.getModuleTypes()) {
                    if(!moduleType.getModuleFrames().isEmpty()){
                        String headline = sectionCounter + "." + (++moduleTypeCounter) + ". " + moduleType.getName();
                        paragraph = new Paragraph(headline);
                        document.add(paragraph);
                    }
                    for(ModuleFrameDTO moduleFrame : moduleType.getModuleFrames()) {
                        List<ModuleImplementationEntity> moduleImplementationEntities = moduleFrameModuleImplementationRepository
                                .findModuleFrameModuleImplementationEntitiesByModuleFrameId(moduleFrame.getId()).stream()
                                .map(ModuleFrameModuleImplementationEntity::getModuleImplementation).toList();

                        for(ModuleImplementationEntity moduleImplementationEntity : moduleImplementationEntities) {
                            paragraph = new Paragraph(moduleImplementationEntity.getName());
                            document.add(paragraph);
                        }
                    }
                }
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating pdf.", e);
        }

        return byteArrayOutputStream;    }
}
