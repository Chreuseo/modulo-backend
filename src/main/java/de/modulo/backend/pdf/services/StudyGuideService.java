package de.modulo.backend.pdf.services;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.dtos.ModuleFrameSetDTO;
import de.modulo.backend.entities.*;
import de.modulo.backend.repositories.*;
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
    private final SemesterRepository semesterRepository;
    private final CourseTypeModuleFrameRepository courseTypeModuleFrameRepository;
    private final ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository;

    @Autowired
    public StudyGuideService(SpoRepository spoRepository,
                             ModuleFrameService moduleFrameService,
                             ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository,
                             SemesterRepository semesterRepository,
                             CourseTypeModuleFrameRepository courseTypeModuleFrameRepository,
                             ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository) {
        this.spoRepository = spoRepository;
        this.moduleFrameService = moduleFrameService;
        this.moduleFrameModuleImplementationRepository = moduleFrameModuleImplementationRepository;
        this.semesterRepository = semesterRepository;
        this.courseTypeModuleFrameRepository = courseTypeModuleFrameRepository;
        this.examTypeModuleImplementationRepository = examTypeModuleImplementationRepository;
    }

    public ByteArrayOutputStream generateStudyGuide(Long spoId, Long semesterId) {
        SpoEntity spoEntity = spoRepository.findById(spoId).orElseThrow();
        SemesterEntity semesterEntity = semesterRepository.findById(semesterId).orElseThrow();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4.rotate());

            document.setMargins(20, 20, 20, 20);

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

            int moduleCounter = 0;
            int sectionCounter = 0;
            for(ModuleFrameSetDTO.Section section : moduleFrameSetDTO.getSections()) {
                if(!section.getModuleTypes().isEmpty()) {
                    String headline = (++sectionCounter) + ". " + section.getName();
                    paragraph = new Paragraph(headline);
                    document.add(paragraph);
                }
                int moduleTypeCounter = 0;
                for(ModuleFrameSetDTO.Section.ModuleType moduleType : section.getModuleTypes()) {
                    if(!moduleType.getModuleFrames().isEmpty()) {
                        String headline = sectionCounter + "." + (++moduleTypeCounter) + ". " + moduleType.getName();
                        paragraph = new Paragraph(headline);
                        document.add(paragraph);

                        Table table = new Table(new float[]{1, 8, 2, 1, 1, 2, 4, 5, 2, 2});
                        table.setWidth(800);
                        table.setFixedLayout();
                        table.addHeaderCell("Nr.");
                        table.addHeaderCell("Fächer");
                        table.addHeaderCell("Kurz. Bez.");
                        table.addHeaderCell("SWS");
                        table.addHeaderCell("ECTS");
                        table.addHeaderCell("Art der Lehrveranstaltung");
                        table.addHeaderCell("Arten des Leistungsnachweises");
                        table.addHeaderCell("Zugelassene Hilfsmittel");
                        table.addHeaderCell("Erstprüfer");
                        table.addHeaderCell("Zweitprüfer");

                        for (ModuleFrameDTO moduleFrame : moduleType.getModuleFrames()) {
                            List<ModuleImplementationEntity> moduleImplementationEntities = moduleFrameModuleImplementationRepository
                                    .findModuleFrameModuleImplementationEntitiesByModuleFrameId(moduleFrame.getId()).stream()
                                    .map(ModuleFrameModuleImplementationEntity::getModuleImplementation).toList();

                            List<CourseTypeModuleFrameEntity> courseTypeModuleFrameEntities = courseTypeModuleFrameRepository.findCourseTypeModuleFrameEntitiesByModuleFrameId(moduleFrame.getId());
                            StringBuilder courseTypes = new StringBuilder();
                            for (int i = 0; i < courseTypeModuleFrameEntities.size(); i++) {
                                courseTypes.append(courseTypeModuleFrameEntities.get(i).getCourseType().getAbbreviation());
                                if (i < courseTypeModuleFrameEntities.size() - 1) {
                                    courseTypes.append("/");
                                }
                            }

                            for (ModuleImplementationEntity moduleImplementationEntity : moduleImplementationEntities) {
                                table.addCell(++moduleCounter + "");
                                table.addCell(moduleImplementationEntity.getName());
                                table.addCell(moduleImplementationEntity.getAbbreviation());
                                table.addCell(moduleFrame.getSws() + "");
                                table.addCell(moduleFrame.getCredits() + "");
                                table.addCell(courseTypes.toString());
                                List<ExamTypeModuleImplementationEntity> examTypeModuleImplementationEntities = examTypeModuleImplementationRepository.findExamTypeModuleImplementationEntitiesByModuleImplementationId(moduleImplementationEntity.getId());
                                StringBuilder examTypes = new StringBuilder();
                                for (int i = 0; i < examTypeModuleImplementationEntities.size(); i++) {
                                    examTypes.append(examTypeModuleImplementationEntities.get(i).getExamType().getAbbreviation())
                                            .append(" (")
                                            .append(examTypeModuleImplementationEntities.get(i).getExamType().getLength());
                                    if (examTypeModuleImplementationEntities.get(i).getDescription() != null) {
                                        examTypes.append(", ").append(examTypeModuleImplementationEntities.get(i).getDescription());
                                    }
                                    examTypes.append(")");
                                    if (i < examTypeModuleImplementationEntities.size() - 1) {
                                        examTypes.append(", ");
                                    }
                                }
                                table.addCell(examTypes.toString());
                                table.addCell(getCellFromHtmlString(moduleImplementationEntity.getAllowedResources()));
                                table.addCell(moduleImplementationEntity.getFirstExaminant() != null ? moduleImplementationEntity.getFirstExaminant().getCode() : "-");
                                table.addCell(moduleImplementationEntity.getSecondExaminant() != null ? moduleImplementationEntity.getSecondExaminant().getCode() : "-");
                            }
                        }
                        document.add(table);
                    }
                }
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating pdf.", e);
        }

        return byteArrayOutputStream;
    }

    private Cell getCellFromHtmlString(String htmlString) {
        // Create a new Cell to hold the HTML content
        Cell htmlCell = new Cell();

        if(htmlString == null) {
            return htmlCell.add(new Paragraph("-"));
        }

        for(IElement element : HtmlConverter.convertToElements(htmlString)) {
            if(element instanceof IBlockElement) {
                htmlCell.add((IBlockElement) element);
            }else {
                System.out.println("Element is not a block element: " + element);
            }
        }

        return htmlCell; // Return the populated Cell
    }
}
