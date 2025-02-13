package de.modulo.backend.converters;

import de.modulo.backend.dtos.NotificationDTO;
import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationConverterTest {

    @Mock
    UserRepository userRepository;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        assertEquals(notificationEntity.getCreatedAt().format(formatter), notificationDto.getCreatedAt());
        assertEquals(notificationEntity.getUser().getId(), notificationDto.getUserId());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        NotificationDTO notificationDto = notificationConverter.toDto(null);

        // Assert
        assertNull(notificationDto);
    }

    @Test
    void testToEntity() {
        // Arrange
        NotificationDTO notificationDto = new NotificationDTO();
        notificationDto.setId(1L);
        notificationDto.setTitle("Test Title");
        notificationDto.setMessage("Test Message");
        notificationDto.setUnread(true);
        notificationDto.setUserId(2L);

        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setId(2L);
        mockUserEntity.setFirstName("Max");
        mockUserEntity.setLastName("Mustermann");
        when(userRepository.findById(notificationDto.getUserId())).thenReturn(java.util.Optional.of(mockUserEntity));

        // Act
        NotificationEntity notificationEntity = notificationConverter.toEntity(notificationDto);

        // Assert
        assertNotNull(notificationEntity);
        assertEquals(notificationDto.getId(), notificationEntity.getId());
        assertEquals(notificationDto.getTitle(), notificationEntity.getTitle());
        assertEquals(notificationDto.getMessage(), notificationEntity.getMessage());
        assertEquals(notificationDto.isUnread(), notificationEntity.isUnread());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        assertEquals(notificationDto.getCreatedAt(), notificationEntity.getCreatedAt().format(formatter));
        assertEquals(notificationDto.getUserId(), notificationEntity.getUser().getId());
        assertEquals(mockUserEntity.getFirstName(), notificationEntity.getUser().getFirstName());
        assertEquals(mockUserEntity.getLastName(), notificationEntity.getUser().getLastName());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        NotificationEntity notificationEntity = notificationConverter.toEntity(null);

        // Assert
        assertNull(notificationEntity);
    }
}
