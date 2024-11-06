package de.modulo.backend.repositories;

import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUser(UserEntity user);
    int countByUserAndUnread(UserEntity user, boolean unread);
}
