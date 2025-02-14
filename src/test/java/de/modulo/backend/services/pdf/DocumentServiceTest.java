package de.modulo.backend.services.pdf;

import de.modulo.backend.converters.SemesterConverter;
import de.modulo.backend.converters.SpoConverter;
import de.modulo.backend.dtos.DocumentDTO;
import de.modulo.backend.dtos.DocumentGenerationBulkDTO;
import de.modulo.backend.dtos.SpoDTOFlat;
import de.modulo.backend.dtos.SpoDocumentsDTO;
import de.modulo.backend.entities.*;
import de.modulo.backend.enums.DOCUMENT_TYPE;
import de.modulo.backend.enums.NOTIFICATION;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.repositories.DocumentRepository;
import de.modulo.backend.repositories.SemesterRepository;
import de.modulo.backend.repositories.SpoRepository;
import de.modulo.backend.services.NotifyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    @InjectMocks
    private DocumentService documentService;

    @Mock
    private ModuleManualService moduleManualService;
    @Mock
    private StudyGuideService studyGuideService;
    @Mock
    private SpoPdfService spoPdfService;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private SpoRepository spoRepository;
    @Mock
    private SemesterRepository semesterRepository;
    @Mock
    private SpoConverter spoConverter;
    @Mock
    private SemesterConverter semesterConverter;
    @Mock
    private NotifyService notifyService;

    private SpoEntity spoEntity;
    private SemesterEntity semesterEntity;

    @BeforeEach
    public void setUp() {
        // Initialize common test objects
        spoEntity = new SpoEntity();
        spoEntity.setId(1L);
        spoEntity.setName("Test SPO");
        spoEntity.setDegree(new DegreeEntity());
        spoEntity.getDegree().setName("Test Degree");
        spoEntity.getDegree().setId(1L);
        semesterEntity = new SemesterEntity();
        semesterEntity.setId(1L);
        semesterEntity.setAbbreviation("SS");
        semesterEntity.setYear("2023");
    }

    @Test
    public void testGenerateDocument_ModuleManual() {
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));
        when(semesterRepository.findById(1L)).thenReturn(Optional.of(semesterEntity));
        when(documentRepository.findBySpoIdAndSemesterIdAndType(1L, 1L, DOCUMENT_TYPE.MODULE_MANUAL))
                .thenReturn(Optional.empty());
        when(moduleManualService.generateModuleManual(1L, 1L)).thenReturn(new ByteArrayOutputStream());

        documentService.generateDocument(1L, 1L, DOCUMENT_TYPE.MODULE_MANUAL);

        ArgumentCaptor<DocumentEntity> documentEntityCaptor = ArgumentCaptor.forClass(DocumentEntity.class);
        verify(documentRepository).save(documentEntityCaptor.capture());
        DocumentEntity savedDocument = documentEntityCaptor.getValue();
        assertNotNull(savedDocument);
        assertEquals(DOCUMENT_TYPE.MODULE_MANUAL, savedDocument.getType());
    }

    @Test
    public void testGenerateDocument_SPO() {
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));
        when(spoPdfService.generateSpo(1L)).thenReturn(new ByteArrayOutputStream());
        when(semesterRepository.findById(any())).thenReturn(Optional.of(new SemesterEntity()));

        documentService.generateDocument(1L, null, DOCUMENT_TYPE.SPO);

        ArgumentCaptor<DocumentEntity> documentEntityCaptor = ArgumentCaptor.forClass(DocumentEntity.class);
        verify(documentRepository).save(documentEntityCaptor.capture());
        DocumentEntity savedDocument = documentEntityCaptor.getValue();
        assertNotNull(savedDocument);
        assertEquals(DOCUMENT_TYPE.SPO, savedDocument.getType());
    }

    @Test
    public void testGenerateDocument_StudyGuide() {
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));
        when(semesterRepository.findById(1L)).thenReturn(Optional.of(semesterEntity));
        when(documentRepository.findBySpoIdAndSemesterIdAndType(1L, 1L, DOCUMENT_TYPE.STUDY_GUIDE))
                .thenReturn(Optional.empty());
        when(studyGuideService.generateStudyGuide(1L, 1L)).thenReturn(new ByteArrayOutputStream());

        documentService.generateDocument(1L, 1L, DOCUMENT_TYPE.STUDY_GUIDE);

        ArgumentCaptor<DocumentEntity> documentEntityCaptor = ArgumentCaptor.forClass(DocumentEntity.class);
        verify(documentRepository).save(documentEntityCaptor.capture());
        DocumentEntity savedDocument = documentEntityCaptor.getValue();
        assertNotNull(savedDocument);
        assertEquals(DOCUMENT_TYPE.STUDY_GUIDE, savedDocument.getType());
    }

    @Test
    public void testGetDocument() {
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));
        when(semesterRepository.findById(1L)).thenReturn(Optional.of(semesterEntity));
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setData(new byte[]{1, 2, 3});
        documentEntity.setGeneratedAt(LocalDateTime.now());
        when(documentRepository.findBySpoIdAndSemesterIdAndType(1L, 1L, DOCUMENT_TYPE.MODULE_MANUAL))
                .thenReturn(Optional.of(documentEntity));

        DocumentDTO result = documentService.getDocument(1L, 1L, DOCUMENT_TYPE.MODULE_MANUAL);

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertTrue(result.getContent().length > 0);
    }

    @Test
    public void testUploadDocument() {
        byte[] documentData = new byte[]{1, 2, 3};
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));
        when(semesterRepository.findById(1L)).thenReturn(Optional.of(semesterEntity));
        when(documentRepository.findBySpoIdAndSemesterIdAndType(1L, 1L, DOCUMENT_TYPE.STUDY_GUIDE))
                .thenReturn(Optional.empty());

        documentService.uploadDocument(documentData, 1L, 1L, DOCUMENT_TYPE.STUDY_GUIDE);

        ArgumentCaptor<DocumentEntity> documentEntityCaptor = ArgumentCaptor.forClass(DocumentEntity.class);
        verify(documentRepository).save(documentEntityCaptor.capture());
        DocumentEntity savedDocument = documentEntityCaptor.getValue();
        assertNotNull(savedDocument);
        assertArrayEquals(documentData, savedDocument.getData());
        assertEquals(DOCUMENT_TYPE.STUDY_GUIDE, savedDocument.getType());
    }

    @Test
    public void testGenerateBulkDocuments() {
        DocumentGenerationBulkDTO bulkDTO = new DocumentGenerationBulkDTO();
        // Setup your DTO with appropriate test data.

        UserEntity user = new UserEntity();
        user.setId(1L);

        // Assuming 'isSpo', 'isModuleManual', 'isStudyGuide' methods return true for testing purpose
        SpoDTOFlat spoDTOFlat = new SpoDTOFlat();
        spoDTOFlat.setId(1L);
        bulkDTO.setSpoDTOFlatList(List.of(spoDTOFlat));

        assertThrows(NotifyException.class, () -> {
            documentService.generateBulkDocuments(bulkDTO, user);
        });
    }

    // Continue to implement additional tests...

}
