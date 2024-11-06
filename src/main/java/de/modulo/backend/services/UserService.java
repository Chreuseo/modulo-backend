package de.modulo.backend.services;

import de.modulo.backend.converters.NotificationConverter;
import de.modulo.backend.converters.UserConverter;
import de.modulo.backend.dtos.NotificationDTO;
import de.modulo.backend.dtos.UserDTO;
import de.modulo.backend.dtos.UserDTOFlat;
import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.repositories.NotificationRepository;
import de.modulo.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final NotificationRepository notificationRepository;
    private final NotificationConverter notificationConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserConverter userConverter,
                       NotificationRepository notificationRepository,
                       NotificationConverter notificationConverter,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.notificationRepository = notificationRepository;
        this.notificationConverter = notificationConverter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<UserDTOFlat> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(userConverter::toDtoFlat)
                .collect(Collectors.toList());
    }

    public List<UserDTOFlat> getUsersByRole(ROLE role){
        return userRepository.findAllByRole(role).stream()
                .map(userConverter::toDtoFlat)
                .toList();
    }

    public UserDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        return userConverter.toDto(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        UserEntity user = userConverter.toEntity(userDTO);
        user.setPassword(bCryptPasswordEncoder.encode("password"));
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

    public List<NotificationDTO> getNotifications(UserEntity user, boolean setRead) {
        List<NotificationEntity> unreadNotifications = new ArrayList<>();
        List<NotificationDTO> result = notificationRepository.findByUser(user).stream()
                .sorted((o1, o2) -> {
                    return o1.getCreatedAt() != null && o2.getCreatedAt() != null ? o2.getCreatedAt().compareTo(o1.getCreatedAt()) : 0;
                })
                .peek(notification -> {
                    if(setRead && notification.isUnread()){
                        unreadNotifications.add(notification);
                    }})
                .map(notificationConverter::toDto).toList();
        unreadNotifications.forEach(notification -> {
            notification.setUnread(false);
        });
        notificationRepository.saveAll(unreadNotifications);
        return result;
    }
}
