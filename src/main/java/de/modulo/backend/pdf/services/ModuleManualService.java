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
import de.modulo.backend.entities.ModuleFrameModuleImplementationEntity;
import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.ModuleFrameModuleImplementationRepository;
import de.modulo.backend.repositories.SpoRepository;
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

    @Autowired
    public ModuleManualService(ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository, ModuleFrameService moduleFrameService, SpoRepository spoRepository) {
        this.moduleFrameModuleImplementationRepository = moduleFrameModuleImplementationRepository;
        this.moduleFrameService = moduleFrameService;

        this.spoRepository = spoRepository;
    }

    public ByteArrayOutputStream generateModuleManual(Long spoId) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.setMargins(40, 40, 40, 40);

            addTitlePage(document, spoId);

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
                        List<ModuleImplementationEntity> moduleImplementationEntitys = moduleFrameModuleImplementationRepository
                                .findModuleFrameModuleImplementationEntitiesByModuleFrameId(moduleFrame.getId()).stream()
                                .map(ModuleFrameModuleImplementationEntity::getModuleImplementation).toList();

                        for(ModuleImplementationEntity moduleImplementationEntity : moduleImplementationEntitys) {
                            String title = sectionCounter + "." + moduleTypeCounter + "." + (++moduleCounter) + ". " + moduleImplementationEntity.getName();
                            paragraph = new Paragraph();
                            Link link = new Link(title, PdfAction.createGoTo(title));
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
                        List<ModuleImplementationEntity> moduleImplementationEntitys = moduleFrameModuleImplementationRepository
                                .findModuleFrameModuleImplementationEntitiesByModuleFrameId(moduleFrame.getId()).stream()
                                .map(ModuleFrameModuleImplementationEntity::getModuleImplementation).toList();

                        for(ModuleImplementationEntity moduleImplementationEntity : moduleImplementationEntitys) {
                            String title = sectionCounter + "." + moduleTypeCounter + "." + (++moduleCounter) + ". " + moduleImplementationEntity.getName();
                            addModuleFrameToDocument(moduleImplementationEntity, document, title);
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

    private void addTitlePage(Document document, Long spoId) throws IOException {
        SpoEntity spoEntity = spoRepository.findById(spoId).orElseThrow();

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
        paragraph.add("WiSe");
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
        paragraph.add("2024/25");
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

    private void addModuleFrameToDocument(ModuleImplementationEntity moduleImplementationEntity, Document document, String title) {
        if(title != null) {
            Paragraph paragraph = new Paragraph(title);
            paragraph.setDestination(title);
            document.add(paragraph);
        }

        // Create a new Table with a suitable number of columns adaptable to your fields
        Table table = new Table(new float[] {1, 2}); // Two columns: Label and Value
        table.setWidth(515);
        table.setFixedLayout();

        // Add rows for each field of the ModuleImplementationEntity
        table.addCell(new Cell().add(new Paragraph("ID")));
        table.addCell(new Cell().add(new Paragraph(moduleImplementationEntity.getId().toString())));

        table.addCell(new Cell().add(new Paragraph("Name")));
        Paragraph paragraph = new Paragraph(moduleImplementationEntity.getName() != null
                ? moduleImplementationEntity.getName() : "N/A");
        table.addCell(new Cell().add(paragraph));

        table.addCell(new Cell().add(new Paragraph("Abbreviation")));
        table.addCell(new Cell().add(new Paragraph(moduleImplementationEntity.getAbbreviation() != null
                ? moduleImplementationEntity.getAbbreviation() : "N/A")));

        table.addCell(new Cell().add(new Paragraph("Allowed Resources")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getAllowedResources()));

        // Repeat similarly for other fields
        // You can customize this section for other fields in a similar manner
        table.addCell(new Cell().add(new Paragraph("Workload")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getWorkload()));

        table.addCell(new Cell().add(new Paragraph("Required Competences")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getRequiredCompetences()));

        table.addCell(new Cell().add(new Paragraph("Qualification Targets")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getQualificationTargets()));

        table.addCell(new Cell().add(new Paragraph("Content")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getContent()));

        // Finish with page break after each module's detail
        table.addCell(new Cell().add(new Paragraph("Additional Exams")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getAdditionalExams()));

        table.addCell(new Cell().add(new Paragraph("Media Types")));
        table.addCell(getCellFromHtmlString(moduleImplementationEntity.getMediaTypes()));

        table.addCell(new Cell().add(new Paragraph("Literature")));
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


}
