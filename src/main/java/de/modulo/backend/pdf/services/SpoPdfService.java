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
import de.modulo.backend.repositories.ParagraphRepository;
import de.modulo.backend.repositories.SpoRepository;
import de.modulo.backend.services.ModuleFrameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class SpoPdfService {

    private final SpoRepository spoRepository;
    private final ParagraphRepository paragraphRepository;
    private final ModuleFrameService moduleFrameService;

    @Autowired
    public SpoPdfService(SpoRepository spoRepository,
                         ParagraphRepository paragraphRepository,
                         ModuleFrameService moduleFrameService){
        this.spoRepository = spoRepository;
        this.paragraphRepository = paragraphRepository;
        this.moduleFrameService = moduleFrameService;
    }

    public ByteArrayOutputStream generateSpo(Long spoId) {
        SpoEntity spoEntity = spoRepository.findById(spoId).orElseThrow();
        List<ParagraphEntity> paragraphEntities = paragraphRepository.findBySpoId(spoId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.setMargins(40, 40, 40, 40);

            Paragraph paragraph = new Paragraph();
            paragraph.add("Studien- und Prüfungsordung für den Studiengang ");
            paragraph.add(spoEntity.getDegree().getName()).add(" ");
            paragraph.add(spoEntity.getName()).add(" ");
            paragraph.add("an der Hochschule für angewandte Wissenschaften Coburg");
            paragraph.setBold();
            paragraph.setFontSize(14);
            paragraph.setTextAlignment(TextAlignment.CENTER);
            document.add(paragraph);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            if (spoEntity.getPublication() != null){
                paragraph = new Paragraph();
                paragraph.add("Vom ");
                paragraph.add(simpleDateFormat.format(spoEntity.getPublication()));
                paragraph.setFontSize(14);
                paragraph.setTextAlignment(TextAlignment.CENTER);
                document.add(paragraph);
            }

            if(spoEntity.getHeader() != null) {
                paragraph = getParagraphFromHtmlString(spoEntity.getHeader());
                paragraph.setFontSize(11);
                paragraph.setTextAlignment(TextAlignment.CENTER);
                paragraph.setPaddingTop(24);
                document.add(paragraph);
            }

            for(ParagraphEntity paragraphEntity : paragraphEntities){
                paragraph = new Paragraph();
                paragraph.add(paragraphEntity.getTitle());
                paragraph.setFontSize(11);
                paragraph.setMarginTop(12);
                paragraph.setTextAlignment(TextAlignment.CENTER);
                document.add(paragraph);

                paragraph = getParagraphFromHtmlString(paragraphEntity.getText());
                paragraph.setFontSize(11);
                document.add(paragraph);
            }

            if(spoEntity.getFooter() != null){
                paragraph = getParagraphFromHtmlString(spoEntity.getFooter());
                paragraph.setFontSize(11);
                paragraph.setTextAlignment(TextAlignment.CENTER);
                paragraph.setPaddingTop(24);
                document.add(paragraph);
            }

            pdf.addNewPage(PageSize.A4.rotate());
            document.add(new AreaBreak());
            document.setMargins(20, 20, 20, 20);


            Table table = new Table(new float[]{1, 5, 2, 2, 4, 4, 3, 3});
            table.setWidth(800);
            table.setFixedLayout();
            table.setFontSize(7);
            table.setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell("1");
            table.addHeaderCell("2");
            table.addHeaderCell("3");
            table.addHeaderCell("4");
            table.addHeaderCell("5");
            table.addHeaderCell("6");
            table.addHeaderCell("7");
            table.addHeaderCell("8");

            Cell cell = new Cell(2, 1);
            cell.add(new Paragraph("lfd. Nr."));
            table.addCell(cell);
            cell = new Cell(1, 3);
            cell.add(new Paragraph("Lehrveranstaltungen"));
            cell.setBold();
            table.addCell(cell);
            cell = new Cell(1, 4);
            cell.add(new Paragraph("Prüfungen"));
            cell.setBold();
            table.addCell(cell);
            table.addCell("Module");
            table.addCell("SWS");
            table.addCell("Art der Lehrveranstaltung");
            table.addCell("Art");
            table.addCell("Umfang");
            table.addCell("Gewicht der Endnote für die Prüfungsgesamtnote");
            table.addCell("Leistungspunkte (ECTS)");

            document.add(table);

            ModuleFrameSetDTO moduleFrameSetDTO = moduleFrameService.getModuleFrameSetDTOBySpoId(spoId);

            int moduleCounter = 0;
            int sectionCounter = 0;
            for(ModuleFrameSetDTO.Section section : moduleFrameSetDTO.getSections()) {
                if (!section.getModuleTypes().isEmpty()) {
                    String headline = (++sectionCounter) + ". " + section.getName();
                    paragraph = new Paragraph(headline);
                    document.add(paragraph);
                }
                int moduleTypeCounter = 0;
                for (ModuleFrameSetDTO.Section.ModuleType moduleType : section.getModuleTypes()) {
                    if (!moduleType.getModuleFrames().isEmpty()) {
                        String headline = sectionCounter + "." + (++moduleTypeCounter) + ". " + moduleType.getName();
                        paragraph = new Paragraph(headline);
                        document.add(paragraph);


                        for (ModuleFrameDTO moduleFrame : moduleType.getModuleFrames()) {
                        }
                    }
                }
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating pdf.", e);
        }

        return byteArrayOutputStream;
    }

    private Paragraph getParagraphFromHtmlString(String htmlString) {
        Paragraph paragraph = new Paragraph();
        if(htmlString == null){
            return null;
        }
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
