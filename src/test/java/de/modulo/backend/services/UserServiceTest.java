package de.modulo.backend.services;

import de.modulo.backend.converters.NotificationConverter;
import de.modulo.backend.converters.UserConverter;
import de.modulo.backend.dtos.UserDTO;
import de.modulo.backend.dtos.UserDTOFlat;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.repositories.*;
import de.modulo.backend.services.mail.MailSenderService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService; // Class under test

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConverter userConverter;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationConverter notificationConverter;

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;

    @Mock
    private ModuleImplementationRepository moduleImplementationRepository;

    @Mock
    private SpoResponsibleUserRepository spoResponsibleUserRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetAllUsers_thenReturnUserList() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        UserDTOFlat userDTOFlat = new UserDTOFlat();
        userDTOFlat.setId(1L);

        when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity));
        when(userConverter.toDtoFlat(userEntity)).thenReturn(userDTOFlat);

        List<UserDTOFlat> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(userDTOFlat.getId(), users.get(0).getId());
    }

    @Test
    void whenGetUsersByRole_thenReturnUserList() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        UserDTOFlat userDTOFlat = new UserDTOFlat();
        userDTOFlat.setId(1L);

        when(userRepository.findAllByRole(ROLE.USER)).thenReturn(Collections.singletonList(userEntity));
        when(userConverter.toDtoFlat(userEntity)).thenReturn(userDTOFlat);

        List<UserDTOFlat> users = userService.getUsersByRole(ROLE.USER);

        assertEquals(1, users.size());
        assertEquals(userDTOFlat.getId(), users.get(0).getId());
    }

    @Test
    void whenGetUserById_thenReturnUserDTO() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userConverter.toDto(userEntity)).thenReturn(userDTO);

        UserDTO foundUser = userService.getUserById(1L);

        assertEquals(userDTO.getId(), foundUser.getId());
    }

    @Test
    void whenCreateUser_thenReturnUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(userConverter.toEntity(userDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.createUser(userDTO);

        verify(mailSenderService, times(1)).sendHtmlMail(any(), any(), any());
    }

    @Test
    void whenUpdateUser_thenReturnUpdatedUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John Updated");

        UserEntity existingUserEntity = new UserEntity();
        existingUserEntity.setId(1L);
        existingUserEntity.setPassword("oldPassword");

        UserEntity updatedUserEntity = new UserEntity();
        updatedUserEntity.setId(1L);

        when(userRepository.existsById(userDTO.getId())).thenReturn(true);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(existingUserEntity));
        when(userConverter.toEntity(userDTO)).thenReturn(updatedUserEntity);
        when(userRepository.save(updatedUserEntity)).thenReturn(updatedUserEntity);
        when(userConverter.toDto(updatedUserEntity)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(userDTO);

        assertEquals("John Updated", result.getFirstName());
    }

    @Test
    void whenDeleteUser_thenUserIsDeleted() {
        Long userId = 1L;

        // Assuming moduleImplementationLecturerRepository, moduleImplementationRepository,
        // and spoResponsibleUserRepository methods are called and do not throw exceptions.
        doNothing().when(moduleImplementationLecturerRepository).deleteModuleImplementationLecturerEntitiesByLecturerId(userId);
        doNothing().when(spoResponsibleUserRepository).deleteSpoResponsibleUserEntitiesByUserId(userId);
        doNothing().when(notificationRepository).deleteNotificationEntitiesByUserId(userId);
        // Mocking the behavior for module implementations (to avoid throwing null pointer exceptions)
        when(moduleImplementationRepository.getModuleImplementationEntitiesByResponsibleId(userId)).thenReturn(Collections.emptyList());
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId); // Verify the userRepository method
    }

    @Test
    void whenResetPassword_thenPasswordIsReset() {
        Long userId = 1L;
        String randomPassword = "newRandomPassword";
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setMail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(bCryptPasswordEncoder.encode(randomPassword)).thenReturn("encodedPassword");
        doNothing().when(mailSenderService).sendHtmlMail(any(), any(), any());

        userService.resetPassword(userId);

        verify(userRepository, times(1)).save(userEntity);
        verify(mailSenderService, times(1)).sendHtmlMail(any(), any(), any());
    }

    @AfterEach
    public void tearDown() {
        // Any necessary clean-up
    }
}
