package de.modulo.backend.converters;

import de.modulo.backend.dtos.UserDTO;
import de.modulo.backend.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserConverterTest {

    @InjectMocks
    private UserConverter userConverter;

    @Test
    void testToDto() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Max");
        userEntity.setLastName("Mustermann");
        userEntity.setCode("123456");
        userEntity.setMail("test@mail.de");

        // Act
        UserDTO userDto = userConverter.toDto(userEntity);

        // Assert
        assertNotNull(userDto);
        assertEquals(userEntity.getId(), userDto.getId());
        assertEquals(userEntity.getFirstName(), userDto.getFirstName());
        assertEquals(userEntity.getLastName(), userDto.getLastName());
        assertEquals(userEntity.getCode(), userDto.getCode());
        assertEquals(userEntity.getMail(), userDto.getMail());
    }

    @Test
    void testToEntity() {
        // Arrange
        UserDTO userDto = new UserDTO();
        userDto.setId(1L);
        userDto.setFirstName("Max");
        userDto.setLastName("Mustermann");
        userDto.setCode("123456");
        userDto.setMail("test@test.de");

        // Act
        UserEntity userEntity = userConverter.toEntity(userDto);

        // Assert
        assertNotNull(userEntity);
        assertEquals(userDto.getId(), userEntity.getId());
        assertEquals(userDto.getFirstName(), userEntity.getFirstName());
        assertEquals(userDto.getLastName(), userEntity.getLastName());
        assertEquals(userDto.getCode(), userEntity.getCode());
        assertEquals(userDto.getMail(), userEntity.getMail());
    }
}
