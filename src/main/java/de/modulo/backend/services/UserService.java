package de.modulo.backend.services;

import de.modulo.backend.converters.UserConverter;
import de.modulo.backend.dtos.UserDTO;
import de.modulo.backend.dtos.UserDTOFlat;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserService(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    public List<UserDTOFlat> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(userConverter::toDtoFlat)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        return userConverter.toDto(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        UserEntity user = userConverter.toEntity(userDTO);
        UserEntity savedUser = userRepository.save(user);
        return userConverter.toDto(savedUser);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        if(!userRepository.existsById(userDTO.getId())) {
            throw new RuntimeException("User not found");
        }
        UserEntity oldUser = userRepository.findById(userDTO.getId()).orElseThrow();
        UserEntity user = userConverter.toEntity(userDTO);
        user.setPassword(oldUser.getPassword());
        UserEntity savedUser = userRepository.save(user);
        return userConverter.toDto(savedUser);
    }

    public UserDTO changePassword(UserEntity userEntity, String password) {
        userEntity.setPassword(password);
        UserEntity savedUser = userRepository.save(userEntity);
        return userConverter.toDto(savedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
