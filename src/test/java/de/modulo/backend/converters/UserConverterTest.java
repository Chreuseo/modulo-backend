package de.modulo.backend.converters;

import de.modulo.backend.dtos.UserDTO;
import de.modulo.backend.dtos.UserDTOAuth;
import de.modulo.backend.dtos.UserDTOFlat;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

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
        userEntity.setRole(ROLE.USER);

        // Act
        UserDTO userDto = userConverter.toDto(userEntity);

        // Assert
        assertNotNull(userDto);
        assertEquals(userEntity.getId(), userDto.getId());
        assertEquals(userEntity.getFirstName(), userDto.getFirstName());
        assertEquals(userEntity.getLastName(), userDto.getLastName());
        assertEquals(userEntity.getCode(), userDto.getCode());
        assertEquals(userEntity.getMail(), userDto.getMail());
        assertEquals(userEntity.getRole().name(), userDto.getRole());
    }

    @Test
    void testToDto_nullEntity(){
        // Act
        UserDTO userDto = userConverter.toDto(null);

        // Assert
        assertNull(userDto);
    }

    @Test
    void testToDtoFlat() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setTitle("Prof.");
        userEntity.setFirstName("Max");
        userEntity.setLastName("Mustermann");
        userEntity.setCode("123456");
        userEntity.setMail("test@test.de");

        // Act
        UserDTOFlat userDTOFlat = userConverter.toDtoFlat(userEntity);

        // Assert
        assertNotNull(userDTOFlat);
        assertEquals(userEntity.getId(), userDTOFlat.getId());
        assertEquals(userEntity.getTitle() + " " + userEntity.getFirstName() + " " + userEntity.getLastName(), userDTOFlat.getName());
        assertEquals(userEntity.getCode(), userDTOFlat.getCode());
        assertEquals(userEntity.getMail(), userDTOFlat.getMail());
    }

    @Test
    void testToDtoFlat_noTitle(){
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Max");
        userEntity.setLastName("Mustermann");
        userEntity.setCode("123456");
        userEntity.setMail("test@test.de");

        // Act
        UserDTOFlat userDTOFlat = userConverter.toDtoFlat(userEntity);

        // Assert
        assertNotNull(userDTOFlat);
        assertEquals(userEntity.getId(), userDTOFlat.getId());
        assertEquals(userEntity.getFirstName() + " " + userEntity.getLastName(), userDTOFlat.getName());
        assertEquals(userEntity.getCode(), userDTOFlat.getCode());
        assertEquals(userEntity.getMail(), userDTOFlat.getMail());
    }

    @Test
    void testToDtoFlat_nullEntity(){
        // Act
        UserDTOFlat userDTOFlat = userConverter.toDtoFlat(null);

        // Assert
        assertNull(userDTOFlat);
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
        userDto.setRole(ROLE.USER.name());

        // Act
        UserEntity userEntity = userConverter.toEntity(userDto);

        // Assert
        assertNotNull(userEntity);
        assertEquals(userDto.getId(), userEntity.getId());
        assertEquals(userDto.getFirstName(), userEntity.getFirstName());
        assertEquals(userDto.getLastName(), userEntity.getLastName());
        assertEquals(userDto.getCode(), userEntity.getCode());
        assertEquals(userDto.getMail(), userEntity.getMail());
        assertEquals(userDto.getRole(), userEntity.getRole().name());
    }

    @Test
    void testToEntity_fromUserDtoAuth(){
        // Arrange
        UserDTOAuth userDTOAuth = new UserDTOAuth();
        userDTOAuth.setMail("test@test.de");
        userDTOAuth.setPassword("password");

        // Act
        UserEntity userEntity = userConverter.toEntity(userDTOAuth);

        // Assert
        assertNotNull(userEntity);
        assertEquals(userDTOAuth.getMail(), userEntity.getMail());
        assertEquals(userDTOAuth.getPassword(), userEntity.getPassword());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        UserEntity userEntity = userConverter.toEntity((UserDTO) null);

        // Assert
        assertNull(userEntity);
    }

    @Test
    void testToEntity_NullDtoAuth() {
        // Act
        UserEntity userEntity = userConverter.toEntity((UserDTOAuth) null);

        // Assert
        assertNull(userEntity);
    }
}
