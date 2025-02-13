package de.modulo.backend.converters;

import de.modulo.backend.dtos.NotificationDTO;
import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class NotificationConverterTest {

    @InjectMocks
    private NotificationConverter notificationConverter;

    @Test
    void testToDto() {
        // Arrange
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(1L);
        notificationEntity.setTitle("Test Title");
        notificationEntity.setMessage("Test Message");
        notificationEntity.setUnread(true);
        LocalDateTime createdAt = LocalDateTime.now();
        notificationEntity.setCreatedAt(createdAt);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);
        notificationEntity.setUser(userEntity);

        // Act
        NotificationDTO notificationDto = notificationConverter.toDto(notificationEntity);

        // Assert
        assertNotNull(notificationDto);
        assertEquals(notificationEntity.getId(), notificationDto.getId());
        assertEquals(notificationEntity.getTitle(), notificationDto.getTitle());
        assertEquals(notificationEntity.getMessage(), notificationDto.getMessage());
        assertEquals(notificationEntity.isUnread(), notificationDto.isUnread());
        assertEquals(notificationEntity.getCreatedAt().toString(), notificationDto.getCreatedAt());
        assertEquals(notificationEntity.getUser().getId(), notificationDto.getUserId());
    }
}
