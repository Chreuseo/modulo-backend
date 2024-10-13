package de.modulo.backend.repositories;

import de.modulo.backend.entities.DocumentEntity;
import de.modulo.backend.enums.DOCUMENT_TYPE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    Optional<DocumentEntity> findBySpoIdAndSemesterIdAndType(Long spoId, Long semesterId, DOCUMENT_TYPE documentType);
    Optional<DocumentEntity> findBySpoIdAndType(Long spoId, DOCUMENT_TYPE documentType);
    boolean existsBySpoIdAndSemesterIdAndType(Long spoId, Long semesterId, DOCUMENT_TYPE documentType);
    boolean existsBySpoIdAndType(Long spoId, DOCUMENT_TYPE documentType);
}
