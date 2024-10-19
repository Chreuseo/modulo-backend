package de.modulo.backend.converters;

import de.modulo.backend.dtos.NotificationDTO;
import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter {

    private final UserRepository userRepository;

    @Autowired
    public NotificationConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public NotificationDTO toDto(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setRead(entity.isRead());
        dto.setUserId(entity.getUser().getId());

        return dto;
    }

    public NotificationEntity toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        NotificationEntity entity = new NotificationEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setMessage(dto.getMessage());
        entity.setRead(dto.isRead());
        entity.setUser(userRepository.findById(dto.getUserId()).orElseThrow());

        return entity;
    }

}
