package de.modulo.backend.services;

import de.modulo.backend.converters.UserConverter;
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
}
