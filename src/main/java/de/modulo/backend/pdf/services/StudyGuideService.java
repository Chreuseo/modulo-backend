package de.modulo.backend.pdf.services;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
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

                        Table table = new Table(new float[]{1, 8, 2, 1, 1, 1, 3, 4, 5, 2, 2});
                        table.setWidth(800);
                        table.setFixedLayout();
                        table.setFontSize(7);
                        table.addHeaderCell("Nr.");
                        table.addHeaderCell("Fächer");
                        table.addHeaderCell("Kurz. Bez.");
                        table.addHeaderCell("SWS");
                        table.addHeaderCell("ECTS");
                        table.addHeaderCell("Semester");
                        table.addHeaderCell("Art der Lehrveranstaltung");
                        table.addHeaderCell("Arten des Leistungsnachweises");
                        table.addHeaderCell("Zugelassene Hilfsmittel");
                        table.addHeaderCell("Erstprüfer");
                        table.addHeaderCell("Zweitprüfer");

                        for (ModuleFrameDTO moduleFrame : moduleType.getModuleFrames()) {
                            List<ModuleFrameModuleImplementationEntity> moduleImplementationEntities = moduleFrameModuleImplementationRepository
                                    .findModuleFrameModuleImplementationEntitiesByModuleFrameId(moduleFrame.getId());

                            List<CourseTypeModuleFrameEntity> courseTypeModuleFrameEntities = courseTypeModuleFrameRepository.findCourseTypeModuleFrameEntitiesByModuleFrameId(moduleFrame.getId());
                            StringBuilder courseTypes = new StringBuilder();
                            for (int i = 0; i < courseTypeModuleFrameEntities.size(); i++) {
                                courseTypes.append(courseTypeModuleFrameEntities.get(i).getCourseType().getAbbreviation());
                                if (i < courseTypeModuleFrameEntities.size() - 1) {
                                    courseTypes.append("/");
                                }
                            }

                            for (ModuleFrameModuleImplementationEntity moduleFrameModuleImplementationEntity : moduleImplementationEntities) {
                                table.addCell(++moduleCounter + "");
                                table.addCell(moduleFrameModuleImplementationEntity.getModuleImplementation().getName());
                                table.addCell(moduleFrameModuleImplementationEntity.getModuleImplementation().getAbbreviation());
                                table.addCell(moduleFrame.getSws() + "");
                                table.addCell(moduleFrame.getCredits() + "");
                                table.addCell(moduleFrameModuleImplementationEntity.getSemester());
                                table.addCell(courseTypes.toString());
                                List<ExamTypeModuleImplementationEntity> examTypeModuleImplementationEntities = examTypeModuleImplementationRepository.findExamTypeModuleImplementationEntitiesByModuleImplementationId(moduleFrameModuleImplementationEntity.getModuleImplementation().getId());
                                Cell cell = new Cell();
                                for (int i = 0; i < examTypeModuleImplementationEntities.size(); i++) {
                                    paragraph = new Paragraph();
                                    paragraph.add(examTypeModuleImplementationEntities.get(i).getExamType().getAbbreviation())
                                            .add(" (")
                                            .add(examTypeModuleImplementationEntities.get(i).getLength());
                                    if (examTypeModuleImplementationEntities.get(i).getDescription() != null) {
                                        paragraph.add(", ").add(getParagraphFromHtmlString(examTypeModuleImplementationEntities.get(i).getDescription(), PdfFontFactory.createFont("Helvetica"),7));
                                    }
                                    paragraph.add(")");
                                    if (i < examTypeModuleImplementationEntities.size() - 1) {
                                        paragraph.add(", ");
                                    }
                                    cell.add(paragraph);
                                }
                                table.addCell(cell);
                                table.addCell(getCellFromHtmlString(moduleFrameModuleImplementationEntity.getModuleImplementation().getAllowedResources()));
                                table.addCell(moduleFrameModuleImplementationEntity.getModuleImplementation().getFirstExaminant() != null ? moduleFrameModuleImplementationEntity.getModuleImplementation().getFirstExaminant().getCode() : "-");
                                table.addCell(moduleFrameModuleImplementationEntity.getModuleImplementation().getSecondExaminant() != null ? moduleFrameModuleImplementationEntity.getModuleImplementation().getSecondExaminant().getCode() : "-");
                            }
                        }
                        document.add(table);
                    }
                }
            }

            if(spoEntity.getStudyPlanAppendix() != null){
                document.add(new AreaBreak());
                document.add(getParagraphFromHtmlString(spoEntity.getStudyPlanAppendix().replaceAll("</p>", "</p><br/>")));
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

    private Paragraph getParagraphFromHtmlString(String htmlString) {
        Paragraph paragraph = new Paragraph();
        for (IElement element : HtmlConverter.convertToElements(htmlString)) {
            if (element instanceof IBlockElement) {
                paragraph.add((IBlockElement) element);
            } else {
                System.out.println("Element is not a block element: " + element);
            }
        }
        return paragraph;
    }
    private Paragraph getParagraphFromHtmlString(String htmlString, PdfFont font, float fontSize) {
        Paragraph paragraph = new Paragraph();
        for(IElement element : HtmlConverter.convertToElements(htmlString)) {
            if(element instanceof IBlockElement) {
                if (element instanceof Paragraph childParagraph) {
                    childParagraph.setFont(font);
                    childParagraph.setFontSize(fontSize);
                    childParagraph.setMarginBottom(0);
                    childParagraph.setMarginTop(0);
                    paragraph.add(childParagraph);
                } else {
                    paragraph.add((IBlockElement) element);
                }
            }else {
                System.out.println("Element is not a block element: " + element);
            }
        }
        return paragraph;
    }
}
