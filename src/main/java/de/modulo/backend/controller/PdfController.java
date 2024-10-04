package de.modulo.backend.controller;


import de.modulo.backend.pdf.services.ModuleManualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final ModuleManualService moduleManualService;

    @Autowired
    public PdfController(ModuleManualService moduleManualService) {
        this.moduleManualService = moduleManualService;
    }

    @GetMapping("generateModuleManual/{spoId}")
    public ResponseEntity<ByteArrayResource> getPdf(@PathVariable Long spoId) {
        // Generate the PDF using the provided spoId
        ByteArrayOutputStream pdfOutputStream = moduleManualService.generateModuleManual(spoId);

        // Convert the output stream to a byte array
        byte[] pdfBytes = pdfOutputStream.toByteArray();

        // Prepare responses with the appropriate headers
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=module_manual.pdf")
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
