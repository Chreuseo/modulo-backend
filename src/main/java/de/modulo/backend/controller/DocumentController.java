package de.modulo.backend.controller;

import de.modulo.backend.authentication.SessionTokenHelper;
import de.modulo.backend.authentication.ValidatePrivilegesService;
import de.modulo.backend.dtos.SpoDocumentsDTO;
import de.modulo.backend.enums.DOCUMENT_TYPE;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.pdf.services.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    private final DocumentService documentService;
    private final ValidatePrivilegesService validatePrivilegesService;

    private final ENTITY_TYPE CURRENT_ENTITY_TYPE = ENTITY_TYPE.DOCUMENT;


    @Autowired
    public DocumentController(DocumentService documentService, ValidatePrivilegesService validatePrivilegesService) {
        this.documentService = documentService;
        this.validatePrivilegesService = validatePrivilegesService;
    }

    @GetMapping("get")
    public ResponseEntity<byte[]> getDocument(Long spoId, Long semesterId, String documentType, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request), spoId);
            byte[] document = documentService.getDocument(spoId, semesterId, DOCUMENT_TYPE.valueOf(documentType));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", documentType + ".pdf");
            headers.setContentLength(document.length);

            return new ResponseEntity<>(document, headers, HttpStatus.OK);
        } catch (InsufficientPermissionsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("generate")
    public ResponseEntity<Void> generateDocument(Long spoId, Long semesterId, String documentType, HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.ADD, SessionTokenHelper.getSessionToken(request), spoId);
            documentService.generateDocument(spoId, semesterId, DOCUMENT_TYPE.valueOf(documentType));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (InsufficientPermissionsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("spos")
    public ResponseEntity<List<SpoDocumentsDTO>> getSpoDocuments(HttpServletRequest request) {
        try{
            validatePrivilegesService.validatePrivileges(CURRENT_ENTITY_TYPE, PRIVILEGES.READ, SessionTokenHelper.getSessionToken(request));
            List<SpoDocumentsDTO> spoDocuments = documentService.getDocuments();
            return new ResponseEntity<>(spoDocuments, HttpStatus.OK);
        } catch (InsufficientPermissionsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
