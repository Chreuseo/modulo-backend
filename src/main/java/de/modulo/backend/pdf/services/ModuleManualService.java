package de.modulo.backend.pdf.services;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.dtos.ModuleFrameSetDTO;
import de.modulo.backend.entities.*;
import de.modulo.backend.repositories.*;
import de.modulo.backend.services.ModuleFrameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;


@Service
public class ModuleManualService {

    private final ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;
    private final ModuleFrameService moduleFrameService;

    private final SpoRepository spoRepository;
    private final ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;
    private final ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository;
    private final SemesterRepository semesterRepository;

    @Autowired
    public ModuleManualService(ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository,
                               ModuleFrameService moduleFrameService,
                               SpoRepository spoRepository,
                               ModuleImplementationLecturerRepository moduleImplementationLecturerRepository,
                               ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository,
                               SemesterRepository semesterRepository) {
        this.moduleFrameModuleImplementationRepository = moduleFrameModuleImplementationRepository;
        this.moduleFrameService = moduleFrameService;
        this.spoRepository = spoRepository;
        this.moduleImplementationLecturerRepository = moduleImplementationLecturerRepository;
        this.examTypeModuleImplementationRepository = examTypeModuleImplementationRepository;
        this.semesterRepository = semesterRepository;
    }

    public ByteArrayOutputStream generateModuleManual(Long spoId, Long semesterId) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SpoEntity spoEntity = spoRepository.findById(spoId).orElseThrow();

        try {
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.setMargins(40, 40, 40, 40);

            addTitlePage(document, spoEntity, semesterId);

            if(spoEntity.getModuleManualIntroduction() != null){
                document.add(getParagraphFromHtmlString(spoEntity.getModuleManualIntroduction()));
                document.add(new AreaBreak());
            }

            ModuleFrameSetDTO moduleFrameSetDTO = moduleFrameService.getModuleFrameSetDTOBySpoId(spoId);

            Paragraph paragraph = new Paragraph("Inhaltsverzeichnis");
            paragraph.setFontSize(20);
            paragraph.setBold();
            paragraph.setMarginBottom(20);
            document.add(paragraph);

            int sectionCounter = 0;
            for(ModuleFrameSetDTO.Section section : moduleFrameSetDTO.getSections()) {
                if(!section.getModuleTypes().isEmpty()) {
                    String headline = (++sectionCounter) + ". " + section.getName();
                    paragraph = new Paragraph();
                    Link link = new Link(headline, PdfAction.createGoTo(headline));
                    paragraph.add(link);
                    document.add(paragraph);
                }
                int moduleTypeCounter = 0;
                for(ModuleFrameSetDTO.Section.ModuleType moduleType : section.getModuleTypes()) {
                    if(!moduleType.getModuleFrames().isEmpty()){
                        String headline = sectionCounter + "." + (++moduleTypeCounter) + ". " + moduleType.getName();
                        paragraph = new Paragraph();
                        Link link = new Link(headline, PdfAction.createGoTo(headline));
                        paragraph.add(link);
                        document.add(paragraph);
                    }
                    int moduleCounter = 0;
                    for(ModuleFrameDTO moduleFrame : moduleType.getModuleFrames()) {
                        List<ModuleImplementationEntity> moduleImplementationEntities = moduleFrameModuleImplementationRepository
                                .findModuleFrameModuleImplementationEntitiesByModuleFrameId(moduleFrame.getId()).stream()
                                .map(ModuleFrameModuleImplementationEntity::getModuleImplementation).toList();

                        for(ModuleImplementationEntity moduleImplementationEntity : moduleImplementationEntities) {
                            String title = sectionCounter + "." + moduleTypeCounter + "." + (++moduleCounter) + ". " + moduleImplementationEntity.getName();
                            paragraph = new Paragraph();
                            paragraph.setMarginLeft(24);
                            Link link = new Link(title, PdfAction.createGoTo(title));
                            link.setText(moduleImplementationEntity.getName());
                            paragraph.add(link);
                            document.add(paragraph);
                        }
                    }
                }
            }

            document.add(new AreaBreak());

            sectionCounter = 0;
            for(ModuleFrameSetDTO.Section section : moduleFrameSetDTO.getSections()) {
                if(!section.getModuleTypes().isEmpty()) {
                    String headline = (++sectionCounter) + ". " + section.getName();
                    paragraph = new Paragraph(headline);
                    paragraph.setDestination(headline);
                    document.add(paragraph);
                }
                int moduleTypeCounter = 0;
                for(ModuleFrameSetDTO.Section.ModuleType moduleType : section.getModuleTypes()) {
                    if(!moduleType.getModuleFrames().isEmpty()){
                        String headline = sectionCounter + "." + (++moduleTypeCounter) + ". " + moduleType.getName();
                        paragraph = new Paragraph(headline);
                        paragraph.setDestination(headline);
                        document.add(paragraph);
                    }
                    int moduleCounter = 0;
                    for(ModuleFrameDTO moduleFrame : moduleType.getModuleFrames()) {
                        List<ModuleFrameModuleImplementationEntity> moduleImplementationEntitys = moduleFrameModuleImplementationRepository
                                .findModuleFrameModuleImplementationEntitiesByModuleFrameId(moduleFrame.getId()).stream()
                                .toList();

                        for(ModuleFrameModuleImplementationEntity moduleFrameModuleImplementationEntity : moduleImplementationEntitys) {
                            String title = sectionCounter + "." + moduleTypeCounter + "." + (++moduleCounter) + ". " + moduleFrameModuleImplementationEntity.getModuleImplementation().getName();
                            addModuleToDocument(moduleFrameModuleImplementationEntity,
                                    document,
                                    title);
                        }
                    }
                }
            }

            document.close();
            pdf.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating PDF", e);
        }

        return byteArrayOutputStream;
    }

    private void addTitlePage(Document document, SpoEntity spoEntity, Long semesterId) throws IOException {
        SemesterEntity semesterEntity = semesterRepository.findById(semesterId).orElseThrow();

        Paragraph paragraph = new Paragraph();
        paragraph.setFontColor(ColorConstants.WHITE);
        paragraph.setBackgroundColor(ColorConstants.RED);
        paragraph.setFontSize(8);
        paragraph.setWidth(30);
        paragraph.setHeight(24);
        paragraph.setPaddingLeft(2);
        paragraph.setMarginLeft(420);
        paragraph.setMarginBottom(0);
        document.add(paragraph);

        paragraph = new Paragraph();
        paragraph.add(semesterEntity.getAbbreviation());
        paragraph.setFontColor(ColorConstants.WHITE);
        paragraph.setBackgroundColor(ColorConstants.RED);
        paragraph.setFontSize(8);
        paragraph.setWidth(30);
        paragraph.setPaddingLeft(2);
        paragraph.setMarginLeft(420);
        paragraph.setMarginTop(0);
        paragraph.setMarginBottom(0);
        document.add(paragraph);

        paragraph = new Paragraph();
        paragraph.add(semesterEntity.getYear());
        paragraph.setFontColor(ColorConstants.WHITE);
        paragraph.setBackgroundColor(ColorConstants.RED);
        paragraph.setFontSize(8);
        paragraph.setWidth(30);
        paragraph.setPaddingLeft(2);
        paragraph.setMarginLeft(420);
        paragraph.setMarginTop(0);
        document.add(paragraph);

        Table table = new Table(new float[] {1, 2});
        table.setWidth(455);
        table.setMarginTop(100);
        table.setMarginLeft(50);
        table.setFixedLayout();

        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        Image logo = new Image(ImageDataFactory
                .create(Objects.requireNonNull(getClass()
                        .getResourceAsStream("/static/hochschule-coburg_logo.png")).readAllBytes()));
        logo.setHeight(60);
        cell.add(logo);
        table.addCell(cell);

        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);

        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        SolidBorder border = new SolidBorder(3);
        border.setColor(ColorConstants.RED);
        cell.setBorderRight(border);
        paragraph = new Paragraph("Hochschule");
        paragraph.setFontColor(ColorConstants.RED);
        paragraph.setFontSize(20);
        cell.add(paragraph);
        paragraph = new Paragraph("Coburg");
        paragraph.setFontColor(ColorConstants.RED);
        paragraph.setFontSize(20);
        paragraph.setBold();
        cell.add(paragraph);
        table.addCell(cell);

        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        paragraph = new Paragraph("Fakultät");
        paragraph.setFontColor(ColorConstants.RED);
        paragraph.setFontSize(20);
        cell.add(paragraph);
        paragraph = new Paragraph("Elektrotechnik und Informatik");
        paragraph.setFontColor(ColorConstants.RED);
        paragraph.setFontSize(20);
        cell.add(paragraph);
        table.addCell(cell);

        document.add(table);

        paragraph = new Paragraph("Modulhandbuch");
        paragraph.setFontSize(30);
        paragraph.setFontColor(ColorConstants.RED);
        paragraph.setMarginTop(100);
        paragraph.setMarginLeft(100);
        document.add(paragraph);

        paragraph = new Paragraph(spoEntity.getDegree().getName().toUpperCase() + " " + spoEntity.getName().toUpperCase() + " - ");
        paragraph.setFontSize(12);
        paragraph.setFontColor(ColorConstants.BLUE);
        paragraph.setMarginTop(30);
        paragraph.setMarginLeft(100);
        document.add(paragraph);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GÜLTIG FÜR STUDIENANFÄNGER");
        if(spoEntity.getValidFrom() != null) {
            stringBuilder.append(" AB ").append(simpleDateFormat.format(spoEntity.getValidFrom()));
        }
        if(spoEntity.getValidUntil() != null) {
            stringBuilder.append(" BIS ").append(simpleDateFormat.format(spoEntity.getValidUntil()));
        }
        paragraph = new Paragraph(stringBuilder.toString());
        paragraph.setFontSize(12);
        paragraph.setFontColor(ColorConstants.BLUE);
        paragraph.setMarginTop(10);
        paragraph.setMarginLeft(100);
        document.add(paragraph);

        document.add(new AreaBreak());
    }

    private void addModuleToDocument(ModuleFrameModuleImplementationEntity moduleFrameModuleImplementationEntity, Document document, String title) {
        ModuleImplementationEntity moduleImplementationEntity = moduleFrameModuleImplementationEntity.getModuleImplementation();
        ModuleFrameEntity moduleFrameEntity = moduleFrameModuleImplementationEntity.getModuleFrame();

        if(title != null) {
            Paragraph paragraph = new Paragraph(moduleImplementationEntity.getName());
            paragraph.setDestination(title);
            document.add(paragraph);
        }

        // Create a new Table with a suitable number of columns adaptable to your fields
        Table table = new Table(new float[] {1, 2}); // Two columns: Label and Value
        table.setWidth(515);
        table.setFixedLayout();

        table.addCell(new Cell().add(new Paragraph("Modulbezeichnung")));
        Paragraph paragraph = new Paragraph(moduleImplementationEntity.getName());
        paragraph.setBold();
        table.addCell(new Cell().add(paragraph));

        table.addCell(new Cell().add(new Paragraph("Kürzel")));
        table.addCell(new Cell().add(new Paragraph(moduleImplementationEntity.getAbbreviation())));

        table.addCell(new Cell().add(new Paragraph("Lehrform / SWS")));
        table.addCell(new Cell().add(new Paragraph((moduleFrameEntity.getSws() + " SWS"))));

        table.addCell(new Cell().add(new Paragraph("ECTS")));
        table.addCell(new Cell().add(new Paragraph((moduleFrameEntity.getCredits() + " ECTS"))));

        table.addCell(new Cell().add(new Paragraph("Workload")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getWorkload()));

        table.addCell(new Cell().add(new Paragraph("Angebotsturnus")));
        table.addCell(new Cell().add(new Paragraph(moduleImplementationEntity.getCycle() != null ?
                moduleImplementationEntity.getCycle().getName() : "-")));

        table.addCell(new Cell().add(new Paragraph("Dauer des Moduls")));
        table.addCell(new Cell().add(new Paragraph(moduleImplementationEntity.getDuration() != null
                ? moduleImplementationEntity.getDuration().getName() : "-")));

        table.addCell(new Cell().add(new Paragraph("Modulverantwortlicher")));
        table.addCell(new Cell().add(new Paragraph(moduleImplementationEntity.getResponsible() != null ?
                moduleImplementationEntity.getResponsible().toString() : "-")));

        table.addCell(new Cell().add(new Paragraph("Dozenten")));
        Cell cell = new Cell();
        for(ModuleImplementationLecturerEntity moduleImplementationLecturerEntity : moduleImplementationLecturerRepository.getModuleImplementationLecturerEntitiesByModuleImplementationId(moduleImplementationEntity.getId())) {
            cell.add(new Paragraph(moduleImplementationLecturerEntity.getLecturer().toString()));
        }
        table.addCell(cell);

        table.addCell(new Cell().add(new Paragraph("Sprache")));
        table.addCell(new Cell().add(new Paragraph(moduleImplementationEntity.getLanguage() != null ?
                moduleImplementationEntity.getLanguage().getName() : "-")));

        table.addCell(new Cell().add(new Paragraph("Gefährdung Mutterschutz")));
        table.addCell(new Cell().add(new Paragraph(moduleImplementationEntity.getMaternityProtection() != null ?
                moduleImplementationEntity.getMaternityProtection().getName() : "-")));

        table.addCell(new Cell().add(new Paragraph("Zulassungsvoraussetzungen")));
        table.addCell(getCellFromHtmlString(moduleFrameModuleImplementationEntity.getModuleRequirement() != null ?
                moduleFrameModuleImplementationEntity.getModuleRequirement().getName() : null));

        table.addCell(new Cell().add(new Paragraph("Inhaltliche Vorraussetzungen")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getRequiredCompetences()));

        table.addCell(new Cell().add(new Paragraph("Qualifikationsziele")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getQualificationTargets()));

        table.addCell(new Cell().add(new Paragraph("Lehrinhalte")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getContent()));

        table.addCell(new Cell().add(new Paragraph("Endnotenbildende Studien- / Prüfungsleistungen")));
        cell = new Cell();
        for(ExamTypeModuleImplementationEntity examTypeModuleImplementationEntity : examTypeModuleImplementationRepository
                .findExamTypeModuleImplementationEntitiesByModuleImplementationId(moduleImplementationEntity.getId())
                .stream().filter(examTypeModuleImplementationEntity -> examTypeModuleImplementationEntity.getExamType().getSpo() == moduleFrameEntity.getSpo()).toList()) {
            cell.add(new Paragraph(examTypeModuleImplementationEntity.getExamType().getName() + " (" + examTypeModuleImplementationEntity.getLength() + ")"));
        }
        table.addCell(cell);

        table.addCell(new Cell().add(new Paragraph("Sonstige Leistungsnachweise")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getAdditionalExams()));

        table.addCell(new Cell().add(new Paragraph("Medienformen")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getMediaTypes()));

        table.addCell(new Cell().add(new Paragraph("Literatur")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getLiterature()));

        // Add the completed table to the document
        document.add(table);

        // Add a page break after this module's details
        document.add(new AreaBreak());
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
        for(IElement element : HtmlConverter.convertToElements(htmlString)) {
            if(element instanceof IBlockElement) {
                paragraph.add((IBlockElement) element);
            }else {
                System.out.println("Element is not a block element: " + element);
            }
        }
        return paragraph;
    }
}
