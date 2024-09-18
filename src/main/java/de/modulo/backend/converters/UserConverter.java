package de.modulo.backend.converters;

import de.modulo.backend.dtos.UserDTO;
import de.modulo.backend.dtos.UserDTOAuth;
import de.modulo.backend.dtos.UserDTOFlat;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    // Convert UserEntity to UserDTO
    public UserDTO toDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setMail(userEntity.getMail());
        userDTO.setTitle(userEntity.getTitle());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setCode(userEntity.getCode());
        userDTO.setRole(userEntity.getRole().toString()); // Assuming ROLE is an Enum
        return userDTO;
    }

    // Convert UserDTO to UserEntity
    public UserEntity toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setMail(userDTO.getMail());
        userEntity.setTitle(userDTO.getTitle());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setCode(userDTO.getCode());
        userEntity.setRole(ROLE.valueOf(userDTO.getRole())); // Assuming ROLE is an Enum
        // Set default values for boolean fields if needed
        return userEntity;
    }

    // Convert UserEntity to UserDTOAuth
    public UserDTOAuth toDtoAuth(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new UserDTOAuth(
                userEntity.getMail(),
                userEntity.getPassword(),
                userEntity.isExpired(), // Maps `expired`
                userEntity.isLocked(), // Maps `locked`
                userEntity.isCredentialsExpired(), // Maps `credentialsExpired`
                userEntity.isEnabled() // Maps `enabled`
        );
    }

    public UserDTOFlat toDtoFlat(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        UserDTOFlat userDTOFlat = new UserDTOFlat();
        userDTOFlat.setId(userEntity.getId());
        userDTOFlat.setName(userEntity.getTitle() + " " + userEntity.getFirstName() + " " + userEntity.getLastName());
        userDTOFlat.setMail(userEntity.getMail());
        userDTOFlat.setCode(userEntity.getCode());
        return userDTOFlat;
    }

    // Convert UserDTOAuth to UserEntity
    public UserEntity toEntity(UserDTOAuth userDTOAuth) {
        if (userDTOAuth == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setMail(userDTOAuth.getMail());
        userEntity.setPassword(userDTOAuth.getPassword());
        // Set the default values for booleans if needed
        return userEntity;
    }
}

