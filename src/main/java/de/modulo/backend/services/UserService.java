package de.modulo.backend.services;

import de.modulo.backend.converters.NotificationConverter;
import de.modulo.backend.converters.UserConverter;
import de.modulo.backend.dtos.NotificationDTO;
import de.modulo.backend.dtos.UserDTO;
import de.modulo.backend.dtos.UserDTOFlat;
import de.modulo.backend.entities.NotificationEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.repositories.ModuleImplementationLecturerRepository;
import de.modulo.backend.repositories.ModuleImplementationRepository;
import de.modulo.backend.repositories.NotificationRepository;
import de.modulo.backend.repositories.UserRepository;
import de.modulo.backend.services.mail.MailSenderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.text.RandomStringGenerator;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final NotificationRepository notificationRepository;
    private final NotificationConverter notificationConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MailSenderService mailSenderService;
    private final ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;
    private final ModuleImplementationRepository moduleImplementationRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserConverter userConverter,
                       NotificationRepository notificationRepository,
                       NotificationConverter notificationConverter,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       MailSenderService mailSenderService, ModuleImplementationLecturerRepository moduleImplementationLecturerRepository, ModuleImplementationRepository moduleImplementationRepository) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.notificationRepository = notificationRepository;
        this.notificationConverter = notificationConverter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailSenderService = mailSenderService;
        this.moduleImplementationLecturerRepository = moduleImplementationLecturerRepository;
        this.moduleImplementationRepository = moduleImplementationRepository;
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
        String password = getRandomPassword();
        UserEntity user = userConverter.toEntity(userDTO);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        UserEntity savedUser = userRepository.save(user);
        sendWelcomeMail(savedUser, password);
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

    @Transactional
    public void deleteUser(Long id) {
        moduleImplementationLecturerRepository.deleteModuleImplementationLecturerEntitiesByLecturerId(id);
        moduleImplementationRepository.getModuleImplementationEntitiesByResponsibleId(id).forEach(moduleImplementationEntity -> {
            moduleImplementationEntity.setResponsible(null);
            moduleImplementationRepository.save(moduleImplementationEntity);
        });
        userRepository.deleteById(id);
    }

    public List<NotificationDTO> getNotifications(UserEntity user, boolean setRead) {
        List<NotificationEntity> unreadNotifications = new ArrayList<>();
        List<NotificationDTO> result = notificationRepository.findByUser(user).stream()
                .sorted((o1, o2) -> o1.getCreatedAt() != null && o2.getCreatedAt() != null ? o2.getCreatedAt().compareTo(o1.getCreatedAt()) : 0)
                .peek(notification -> {
                    if(setRead && notification.isUnread()){
                        unreadNotifications.add(notification);
                    }})
                .map(notificationConverter::toDto).toList();
        unreadNotifications.forEach(notification -> notification.setUnread(false));
        notificationRepository.saveAll(unreadNotifications);
        return result;
    }

    private String getRandomPassword() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(33, 126) // ASCII printable characters
                .build();

        return generator.generate(12);
    }

    private void sendWelcomeMail(UserEntity user, String password) {
        String subject = "Welcome to Modulo!";

        String htmlText = "<p>Sehr geehrte/-r " + user.getTitle() + " " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                "<p>Willkommen bei der Modulverwaltungssoftware Modulo. Für sie wurde soeben erfolgreich ein Account erstellt.</p>" +
                "<p>Sie können sich mir ihrer E-Mail Adresse und folgendem Initialpasswort einloggen: <strong>" + password + "</strong></p>" +
                "<p><a href=\"http://modulo.christopheuskirchen.de/login\">Hier einloggen</a></p>" +
                "<p>Bitte ändern sie nach dem ersten Login ihr Passwort.</p>" +
                "<p>Viele Grüße,<br>Ihr Modulo-Team</p>";
        mailSenderService.sendHtmlMail(user.getMail(), subject, htmlText);
    }

    public void resetPassword(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        String password = getRandomPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        sendPasswordResetMail(user, password);
    }

    private void sendPasswordResetMail(UserEntity user, String password){
        String subject = "Password zurückgesetzt!";
        String htmlText = "<p>Sehr geehrte/-r " + user.getTitle() + " " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                "<p>Ihr Passwort wurde zurückgesetzt. Ihr neues Passwort lautet: <strong>" + password + "</strong></p>" +
                "<p><a href=\"http://modulo.christopheuskirchen.de/login\">Hier einloggen</a></p>" +
                "<p>Bitte ändern sie nach dem ersten Login ihr Passwort.</p>" +
                "<p>Viele Grüße,<br>Ihr Modulo-Team</p>";
        mailSenderService.sendHtmlMail(user.getMail(), subject, htmlText);
    }
}
