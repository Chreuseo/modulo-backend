package de.modulo.backend.repositories;

import de.modulo.backend.entities.SpoResponsibleUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpoResponsibleUserRepository extends JpaRepository<SpoResponsibleUserEntity, SpoResponsibleUserEntity.SpoResponsibleUserId> {
    List<SpoResponsibleUserEntity> findAllBySpoId(Long spoId);
    boolean existsBySpoIdAndUserId(Long spoId, Long userId);
    void deleteSpoResponsibleUserEntitiesByUserId(Long userId);
}
