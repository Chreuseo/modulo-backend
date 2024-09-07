package de.modulo.backend.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.modulo.backend.converters.UserConverter;
import de.modulo.backend.dtos.UserDTO;
import de.modulo.backend.dtos.UserDTOAuth;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class UserConverterTest {

    private UserConverter userConverter;

    @BeforeEach
    public void setUp() {
        userConverter = new UserConverter();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToDtoWithNullEntity() {
        UserDTO result = userConverter.toDto(null);
        assertNull(result, "convertToDTO should return null when input entity is null");
    }

    @Test
    public void testToDto() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setMail("user@example.com");
        userEntity.setTitle("Mr.");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setCode("USER123");
        userEntity.setRole(ROLE.USER); // Assuming ROLE is an Enum with an instance USER

        // When
        UserDTO result = userConverter.toDto(userEntity);

        // Then
        assertEquals(userEntity.getId(), result.getId());
        assertEquals(userEntity.getMail(), result.getMail());
        assertEquals(userEntity.getTitle(), result.getTitle());
        assertEquals(userEntity.getFirstName(), result.getFirstName());
        assertEquals(userEntity.getLastName(), result.getLastName());
        assertEquals(userEntity.getCode(), result.getCode());
        assertEquals(userEntity.getRole().toString(), result.getRole());
    }

    @Test
    public void testToEntityWithNullDto() {
        UserEntity result = userConverter.toEntity((UserDTO) null);
        assertNull(result, "convertToEntity should return null when input DTO is null");
    }

    @Test
    public void testToEntity() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setMail("user@example.com");
        userDTO.setTitle("Mr.");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setCode("USER123");

        // When
        UserEntity result = userConverter.toEntity(userDTO);

        // Then
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getMail(), result.getMail());
        assertEquals(userDTO.getTitle(), result.getTitle());
        assertEquals(userDTO.getFirstName(), result.getFirstName());
        assertEquals(userDTO.getLastName(), result.getLastName());
        assertEquals(userDTO.getCode(), result.getCode());
        // Role is not set in UserDTO, you may want to add it if needed.
    }

    @Test
    public void testToDtoAuthWithNullEntity() {
        UserDTOAuth result = userConverter.toDtoAuth(null);
        assertNull(result, "convertToDTOAuth should return null when input entity is null");
    }

    @Test
    public void testToDtoAuth() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setMail("user@example.com");
        userEntity.setPassword("password123");
        userEntity.setExpired(false);
        userEntity.setLocked(false);
        userEntity.setCredentialsExpired(false);
        userEntity.setEnabled(true);

        // When
        UserDTOAuth result = userConverter.toDtoAuth(userEntity);

        // Then
        assertEquals(userEntity.getMail(), result.getMail());
        assertEquals(userEntity.getPassword(), result.getPassword());
        assertEquals(userEntity.isExpired(), result.isExpired());
        assertEquals(userEntity.isLocked(), result.isLocked());
        assertEquals(userEntity.isCredentialsExpired(), result.isCredentialsExpired());
        assertEquals(userEntity.isEnabled(), result.isEnabled());
    }

    @Test
    public void testToEntityFromDtoAuthWithNull() {
        UserEntity result = userConverter.toEntity((UserDTOAuth) null);
        assertNull(result, "convertToEntity should return null when input DTO is null");
    }

    @Test
    public void testToEntityFromDtoAuth() {
        // Given
        UserDTOAuth userDTOAuth = new UserDTOAuth();
        userDTOAuth.setMail("user@example.com");
        userDTOAuth.setPassword("password123");
        userDTOAuth.setExpired(false);
        userDTOAuth.setLocked(false);
        userDTOAuth.setCredentialsExpired(false);
        userDTOAuth.setEnabled(true);

        // When
        UserEntity result = userConverter.toEntity(userDTOAuth);

        // Then
        assertEquals(userDTOAuth.getMail(), result.getMail());
        assertEquals(userDTOAuth.getPassword(), result.getPassword());
        // Role and other fields not set are not asserted here, can set defaults if necessary.
    }
}
