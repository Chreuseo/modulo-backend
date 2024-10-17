package de.modulo.backend.pdf.services;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import de.modulo.backend.entities.ParagraphEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.ParagraphRepository;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
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
            paragraph = new Paragraph();
            paragraph.add("Vom ");
            paragraph.add(simpleDateFormat.format(spoEntity.getValidFrom()));
            paragraph.setFontSize(14);
            paragraph.setTextAlignment(TextAlignment.CENTER);
            document.add(paragraph);

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
                PdfCanvas pdfCanvas = new PdfCanvas(pdf.addNewPage());
                pdfCanvas.setLineWidth(1);
                pdfCanvas.setStrokeColor(ColorConstants.BLACK);
                // Draw the horizontal line
                float yPosition = pdf.getDefaultPageSize().getHeight() - 150; // Y-position of the line
                pdfCanvas.moveTo(40, yPosition); // Start Point
                pdfCanvas.lineTo(pdf.getDefaultPageSize().getWidth() - 40, yPosition); // End Point
                pdfCanvas.stroke(); // Execute the drawing

                paragraph = getParagraphFromHtmlString(spoEntity.getFooter());
                paragraph.setFontSize(11);
                paragraph.setTextAlignment(TextAlignment.CENTER);
                paragraph.setPaddingTop(24);
                document.add(paragraph);
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
