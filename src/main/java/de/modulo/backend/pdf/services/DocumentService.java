package de.modulo.backend.pdf.services;

import de.modulo.backend.converters.SemesterConverter;
import de.modulo.backend.converters.SpoConverter;
import de.modulo.backend.dtos.SpoDocumentsDTO;
import de.modulo.backend.entities.DocumentEntity;
import de.modulo.backend.entities.SemesterEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.enums.DOCUMENT_TYPE;
import de.modulo.backend.repositories.DocumentRepository;
import de.modulo.backend.repositories.SemesterRepository;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    private final ModuleManualService moduleManualService;
    private final StudyGuideService studyGuideService;
    private final SpoPdfService spoPdfService;
    private final DocumentRepository documentRepository;
    private final SpoRepository spoRepository;
    private final SemesterRepository semesterRepository;
    private final SpoConverter spoConverter;
    private final SemesterConverter semesterConverter;

    @Autowired
    public DocumentService(ModuleManualService moduleManualService,
                           StudyGuideService studyGuideService,
                           SpoPdfService spoPdfService,
                           DocumentRepository documentRepository,
                           SpoRepository spoRepository,
                           SemesterRepository semesterRepository,
                           SpoConverter spoConverter,
                           SemesterConverter semesterConverter) {
        this.moduleManualService = moduleManualService;
        this.studyGuideService = studyGuideService;
        this.spoPdfService = spoPdfService;
        this.documentRepository = documentRepository;
        this.spoRepository = spoRepository;
        this.semesterRepository = semesterRepository;
        this.spoConverter = spoConverter;
        this.semesterConverter = semesterConverter;
    }

    public void generateDocument(Long spoId, Long semesterId, DOCUMENT_TYPE documentType) {
        SpoEntity spoEntity = spoRepository.findById(spoId).orElseThrow();
        SemesterEntity semesterEntity = semesterRepository.findById(semesterId).orElseThrow();

        DocumentEntity documentEntity = null;

        switch (documentType) {
            case MODULE_MANUAL:
                documentEntity =
                        documentRepository.findBySpoIdAndSemesterIdAndType(spoId, semesterId, documentType)
                                .orElse(new DocumentEntity(spoEntity, semesterEntity, documentType, LocalDateTime.now()));

                documentEntity.setData(moduleManualService.generateModuleManual(spoId, semesterId).toByteArray());
                break;
            case SPO:
                documentEntity =
                        documentRepository.findBySpoIdAndType(spoId, documentType)
                                .orElse(new DocumentEntity(spoEntity, null, documentType, LocalDateTime.now()));
                documentEntity.setData(spoPdfService.generateSpo(spoId).toByteArray());
                break;
            case STUDY_GUIDE:
                documentEntity =
                        documentRepository.findBySpoIdAndSemesterIdAndType(spoId, semesterId, documentType)
                                .orElse(new DocumentEntity(spoEntity, semesterEntity, documentType, LocalDateTime.now()));
                documentEntity.setData(studyGuideService.generateStudyGuide(spoId, semesterId).toByteArray());
                break;
        }
        if(documentEntity != null) {
            documentRepository.save(documentEntity);
        }
    }

    public byte[] getDocument(Long spoId, Long semesterId, DOCUMENT_TYPE documentType) {
        return switch (documentType) {
            case MODULE_MANUAL, STUDY_GUIDE -> documentRepository.findBySpoIdAndSemesterIdAndType(spoId, semesterId, documentType).orElseThrow().getData();
            case SPO -> documentRepository.findBySpoIdAndType(spoId, documentType).orElseThrow().getData();
            default -> null;
        };
    }

    public List<SpoDocumentsDTO> getDocuments() {
        List<SemesterEntity> semesterEntities = semesterRepository.findAll();
        List<SpoEntity> spoEntities = spoRepository.findAll();
        List<SpoDocumentsDTO> spoDocumentsDTOs = new ArrayList<>();

        for(SpoEntity spoEntity : spoEntities) {
            SpoDocumentsDTO spoDocumentsDTO = new SpoDocumentsDTO();
            spoDocumentsDTO.setSpo(spoConverter.toDtoFlat(spoEntity));

            List<SpoDocumentsDTO.Document> generalDocuments = new ArrayList<>();
            if(documentRepository.existsBySpoIdAndType(spoEntity.getId(), DOCUMENT_TYPE.SPO)) {
                SpoDocumentsDTO.Document document = new SpoDocumentsDTO.Document();
                document.setName(DOCUMENT_TYPE.SPO.toString());
                document.setFriendlyName("Studien- und Prüfungsordnung");
                generalDocuments.add(document);
            }
            spoDocumentsDTO.setDocuments(generalDocuments);

            List<SpoDocumentsDTO.Semester> semesterDocuments = new ArrayList<>();
            for(SemesterEntity semesterEntity : semesterEntities) {
                SpoDocumentsDTO.Semester semester = new SpoDocumentsDTO.Semester();
                semester.setSemester(semesterConverter.toDTO(semesterEntity));

                List<SpoDocumentsDTO.Document> documents = new ArrayList<>();
                if(documentRepository.existsBySpoIdAndSemesterIdAndType(spoEntity.getId(), semesterEntity.getId(), DOCUMENT_TYPE.MODULE_MANUAL)) {
                    SpoDocumentsDTO.Document document = new SpoDocumentsDTO.Document();
                    document.setName(DOCUMENT_TYPE.MODULE_MANUAL.toString());
                    document.setFriendlyName("Modulhandbuch");
                    documents.add(document);
                }

                if(documentRepository.existsBySpoIdAndSemesterIdAndType(spoEntity.getId(), semesterEntity.getId(), DOCUMENT_TYPE.STUDY_GUIDE)) {
                    SpoDocumentsDTO.Document document = new SpoDocumentsDTO.Document();
                    document.setName(DOCUMENT_TYPE.STUDY_GUIDE.toString());
                    document.setFriendlyName("Studienplan");
                    documents.add(document);
                }

                semester.setDocuments(documents);
                semesterDocuments.add(semester);
            }
            spoDocumentsDTO.setSemesters(semesterDocuments);
            spoDocumentsDTOs.add(spoDocumentsDTO);
        }
        return spoDocumentsDTOs;
    }
}
