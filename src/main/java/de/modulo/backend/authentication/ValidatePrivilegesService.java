package de.modulo.backend.authentication;

import de.modulo.backend.entities.*;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.NOTIFICATION;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.excpetions.NotifyException;
import de.modulo.backend.repositories.*;
import de.modulo.backend.services.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ValidatePrivilegesService {

    private final SessionService sessionService;
    private final ModuleImplementationRepository moduleImplementationRepository;
    private final ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;
    private final SpoResponsibleUserRepository spoResponsibleUserRepository;
    private final SpoRepository spoRepository;

    private final NotifyService notifyService;
    private final UserRepository userRepository;

    @Autowired
    public ValidatePrivilegesService(SessionService sessionService,
                                     ModuleImplementationRepository moduleImplementationRepository,
                                     ModuleImplementationLecturerRepository moduleImplementationLecturerRepository,
                                     SpoResponsibleUserRepository spoResponsibleUserRepository,
                                     SpoRepository spoRepository,
                                     NotifyService notifyService,
                                     UserRepository userRepository) {
        this.sessionService = sessionService;
        this.moduleImplementationRepository = moduleImplementationRepository;
        this.moduleImplementationLecturerRepository = moduleImplementationLecturerRepository;
        this.spoResponsibleUserRepository = spoResponsibleUserRepository;
        this.spoRepository = spoRepository;

        this.notifyService = notifyService;
        this.userRepository = userRepository;
    }

    public void validateGeneralPrivileges(ENTITY_TYPE entityType, PRIVILEGES privileges, String sessionToken) throws InsufficientPermissionsException, NotifyException{
        ROLE role = sessionService.getRoleBySessionId(UUID.fromString(sessionToken));

        switch(role){
            case ADMIN:
                return;
            case SPO_ADMIN:
                switch(entityType){
                    case GENERAL_SETTINGS -> {
                        switch(privileges){
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access general settings");
                        }
                    }
                    case SPO_SETTINGS -> {
                        switch(privileges){
                            case ADD -> {
                                List<UserEntity> users = userRepository.getUserEntitiesByRole(ROLE.ADMIN);
                                throw new NotifyException(notifyService,
                                        sessionService.getUserBySessionId(UUID.fromString(sessionToken)),
                                        users,
                                        NOTIFICATION.SPO_CREATED);
                            }
                            case UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access SPO settings");
                        }
                    }
                    case USER -> {
                        switch(privileges){
                            case ADD, UPDATE, DELETE, READ_DETAILS -> throw new InsufficientPermissionsException("You do not have the required permissions to access users");
                        }
                    }
                    case SPO -> {
                        switch(privileges){
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access SPOs");
                        }
                    }
                    case MODULE -> {
                        switch(privileges){
                            case UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
                        }
                    }
                    case DOCUMENT -> {
                        switch(privileges){
                            case READ, ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access documents");
                        }
                    }
                    case MODULE_FRAME_MODULE_IMPLEMENTATION -> {
                        switch(privileges){
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access module frame module implementations");
                        }
                    }
                }
                break;
            case USER:
                switch (entityType) {
                    case GENERAL_SETTINGS -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access general settings");
                        }
                    }
                    case SPO_SETTINGS -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access SPO settings");
                        }
                    }
                    case SPO -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access SPOs");
                        }
                    }
                    case MODULE -> {
                        switch (privileges) {
                            case ADD -> {
                                List<UserEntity> users = userRepository.getUserEntitiesByRole(ROLE.ADMIN);
                                throw new NotifyException(notifyService,
                                        sessionService.getUserBySessionId(UUID.fromString(sessionToken)),
                                        users,
                                        NOTIFICATION.MODULE_CREATED);
                            }
                            case READ, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
                        }
                    }
                    case MODULE_FRAME_MODULE_IMPLEMENTATION -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access module frame module implementations");
                        }
                    }
                    case USER -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE, READ_DETAILS -> throw new InsufficientPermissionsException("You do not have the required permissions to access users");
                        }
                    }
                    case DOCUMENT -> {
                        switch (privileges) {
                            case READ, ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access documents");
                        }
                    }
                }
        }
    }

    public void validateSpoSpecificPrivileges(ENTITY_TYPE entityType, PRIVILEGES privileges, String sessionToken, Long spoId) throws InsufficientPermissionsException{
        ROLE role = sessionService.getRoleBySessionId(UUID.fromString(sessionToken));
        SpoEntity spoEntity = spoRepository.findById(spoId).orElseThrow();
        boolean isResponsible = spoResponsibleUserRepository.existsBySpoIdAndUserId(spoId, sessionService.getUserIdBySessionId(UUID.fromString(sessionToken)));

        switch (role){
            case ADMIN -> {
                return;
            }
            case SPO_ADMIN -> {
                switch (entityType){
                    case SPO -> {
                        switch (privileges){
                            case DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access SPOs");
                            case UPDATE -> {
                                if (!isResponsible) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access SPOs");
                                }
                            }
                        }
                    }
                    case MODULE -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
                        }
                    }
                    case DOCUMENT -> {
                        switch (privileges){
                            case READ, ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access documents");
                        }
                    }
                    case MODULE_FRAME_MODULE_IMPLEMENTATION -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE -> {
                                if (!isResponsible) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access module frame module implementations");
                                }
                            }
                        }
                    }
                    case USER -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE, READ_DETAILS -> throw new InsufficientPermissionsException("You do not have the required permissions to access users");
                        }
                    }
                    case SPO_SETTINGS -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE -> {
                                if (!isResponsible) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access SPO settings");
                                }
                            }
                        }
                    }
                    case GENERAL_SETTINGS -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access general settings");
                        }
                    }
                }
            }
        }
    }

    public void validateModuleSpecificPrivileges(ENTITY_TYPE entityType, PRIVILEGES privileges, String sessionToken, Long moduleImplementationId) throws InsufficientPermissionsException, NotifyException{
        ROLE role = sessionService.getRoleBySessionId(UUID.fromString(sessionToken));
        if(role == ROLE.ADMIN){
            return;
        }

        ModuleImplementationEntity moduleImplementationEntity = moduleImplementationRepository.findById(moduleImplementationId).orElseThrow();
        boolean isResponsible = false;
        if(moduleImplementationEntity.getResponsible() != null){
            isResponsible = moduleImplementationEntity.getResponsible().getId().equals(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken)));
        }
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId moduleImplementationLecturerEntityId = new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId();
        moduleImplementationLecturerEntityId.setModuleImplementation(moduleImplementationId);
        moduleImplementationLecturerEntityId.setLecturer(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken)));
        boolean isLecturer = moduleImplementationLecturerRepository.existsById(moduleImplementationLecturerEntityId);

        switch (role){
            case SPO_ADMIN -> {
                switch (entityType){
                    case MODULE -> {
                        switch (privileges){
                            case DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
                            case UPDATE -> {
                                if (!isResponsible) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
                                } else if (isLecturer) {
                                    List<UserEntity> notifyUsers = new ArrayList<>();
                                    notifyUsers.add(moduleImplementationEntity.getResponsible());
                                    throw new NotifyException(notifyService,
                                            sessionService.getUserBySessionId(UUID.fromString(sessionToken)),
                                            notifyUsers,
                                            NOTIFICATION.LECTURER_EDITED_MODULE,
                                            moduleImplementationEntity);
                                }
                            }
                        }
                    }
                    case DOCUMENT -> {
                        switch (privileges){
                            case READ, ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access documents");
                        }
                    }
                    case MODULE_FRAME_MODULE_IMPLEMENTATION -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE -> {
                                if (!isResponsible) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access module frame module implementations");
                                }
                            }
                        }
                    }
                    case USER -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE, READ_DETAILS -> throw new InsufficientPermissionsException("You do not have the required permissions to access users");
                        }
                    }
                    case SPO_SETTINGS -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access SPO settings");
                        }
                    }
                    case GENERAL_SETTINGS -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access general settings");
                        }
                    }
                }
            }
            case USER -> {
                switch (entityType) {
                    case SPO -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE -> {
                                throw new InsufficientPermissionsException("You do not have the required permissions to access SPOs");
                            }
                        }
                    }
                    case DOCUMENT -> {
                        switch (privileges) {
                            case READ, ADD, UPDATE, DELETE -> {
                                throw new InsufficientPermissionsException("You do not have the required permissions to access documents");
                            }
                        }
                    }
                    case GENERAL_SETTINGS -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE -> {
                                throw new InsufficientPermissionsException("You do not have the required permissions to access general settings");
                            }
                        }
                    }
                    case MODULE -> {
                        switch (privileges){
                            case READ, ADD, DELETE -> {
                                if(!isResponsible && !isLecturer){
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
                                }
                            }
                            case UPDATE -> {
                                if(!isResponsible && !isLecturer){
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
                                }else if(!isResponsible){
                                    List<UserEntity> notifyUsers = new ArrayList<>();
                                    notifyUsers.add(moduleImplementationEntity.getResponsible());
                                    throw new NotifyException(notifyService,
                                            sessionService.getUserBySessionId(UUID.fromString(sessionToken)),
                                            notifyUsers,
                                            NOTIFICATION.LECTURER_EDITED_MODULE,
                                            moduleImplementationEntity);
                                }
                            }
                        }
                    }
                    case SPO_SETTINGS -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE -> {
                                throw new InsufficientPermissionsException("You do not have the required permissions to access SPO settings");
                            }
                        }
                    }
                    case USER -> {
                        switch (privileges){
                            case ADD, UPDATE, DELETE, READ_DETAILS -> {
                                throw new InsufficientPermissionsException("You do not have the required permissions to access users");
                            }
                        }
                    }
                    case MODULE_FRAME_MODULE_IMPLEMENTATION -> {
                        switch (privileges){
                            case READ -> {
                                if(!isResponsible && !isLecturer){
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access module frame module implementations");
                                }
                            }
                            case ADD, UPDATE, DELETE -> {
                                throw new InsufficientPermissionsException("You do not have the required permissions to access module frame module implementations");
                            }
                        }
                    }
                }
            }
        }
    }

    public void validateSpoOrModuleSpecificPrivileges(ENTITY_TYPE entityType, PRIVILEGES privileges, String sessionToken, Long spoId, Long moduleImplementationId) throws InsufficientPermissionsException, NotifyException{
        ROLE role = sessionService.getRoleBySessionId(UUID.fromString(sessionToken));
        boolean isSpoResponsible = spoResponsibleUserRepository.existsBySpoIdAndUserId(spoId, sessionService.getUserIdBySessionId(UUID.fromString(sessionToken)));
        ModuleImplementationEntity moduleImplementationEntity = moduleImplementationRepository.findById(moduleImplementationId).orElseThrow();
        boolean isModuleResponsible = false;
        if(moduleImplementationEntity.getResponsible() != null) {
            isModuleResponsible = moduleImplementationEntity.getResponsible().getId().equals(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken)));
        }
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId moduleImplementationLecturerEntityId = new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId();
        moduleImplementationLecturerEntityId.setModuleImplementation(moduleImplementationId);
        moduleImplementationLecturerEntityId.setLecturer(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken)));
        boolean isLecturer = moduleImplementationLecturerRepository.existsById(moduleImplementationLecturerEntityId);

        switch (role) {
            case ADMIN -> {
                return;
            }
            case SPO_ADMIN -> {
                switch (entityType) {
                    case SPO -> {
                        switch (privileges) {
                            case ADD, DELETE ->
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access SPOs");
                            case UPDATE -> {
                                if (!isSpoResponsible) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access SPOs");
                                }
                            }
                        }
                    }
                    case MODULE -> {
                        switch (privileges) {
                            case UPDATE, DELETE -> {
                                if (!isModuleResponsible && !isLecturer) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
                                } else if (!isModuleResponsible) {
                                    List<UserEntity> notifyUsers = new ArrayList<>();
                                    notifyUsers.add(moduleImplementationEntity.getResponsible());
                                    throw new NotifyException(notifyService,
                                            sessionService.getUserBySessionId(UUID.fromString(sessionToken)),
                                            notifyUsers,
                                            NOTIFICATION.LECTURER_EDITED_MODULE,
                                            moduleImplementationEntity);
                                }
                            }
                        }
                    }
                    case DOCUMENT -> {
                        switch (privileges) {
                            case READ, ADD, UPDATE, DELETE ->
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access documents");
                        }
                    }
                    case MODULE_FRAME_MODULE_IMPLEMENTATION -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE -> {
                                if (!isModuleResponsible) {
                                    if (!isSpoResponsible) {
                                        throw new InsufficientPermissionsException("You do not have the required permissions to access module frame module implementations");
                                    } else {
                                        List<UserEntity> notifyUsers = new ArrayList<>();
                                        notifyUsers.add(moduleImplementationEntity.getResponsible());
                                        throw new NotifyException(notifyService,
                                                sessionService.getUserBySessionId(UUID.fromString(sessionToken)),
                                                notifyUsers,
                                                NOTIFICATION.MODULE_ADDED_TO_SPO,
                                                moduleImplementationEntity,
                                                spoRepository.findById(spoId).orElseThrow());
                                    }

                                }
                            }
                        }
                    }
                    case USER -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE, READ_DETAILS ->
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access users");
                        }
                    }
                    case SPO_SETTINGS -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE -> {
                                if (!isSpoResponsible) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access SPO settings");
                                }
                            }
                        }
                    }
                    case GENERAL_SETTINGS -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE ->
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access general settings");
                        }
                    }
                }
            }
            case USER -> {
                switch (entityType) {
                    case SPO, SPO_SETTINGS -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE ->
                                    throw new InsufficientPermissionsException("You do not have the required permissions to edit SPO settings");
                        }
                    }
                    case MODULE -> {
                        switch (privileges) {
                            case READ -> {
                                if (!isModuleResponsible && !isLecturer) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions.");
                                }
                            }
                            case UPDATE -> {
                                if (!isModuleResponsible && !isLecturer) {
                                    throw new InsufficientPermissionsException("You do not have permission to update this resource.");
                                } else if (!isModuleResponsible) {
                                        List<UserEntity> notifyUsers = new ArrayList<>();
                                        notifyUsers.add(moduleImplementationEntity.getResponsible());
                                        throw new NotifyException(notifyService,
                                                sessionService.getUserBySessionId(UUID.fromString(sessionToken)),
                                                notifyUsers,
                                                NOTIFICATION.LECTURER_EDITED_MODULE,
                                                moduleImplementationEntity);
                                }
                            }
                            case DELETE -> {
                                if (!isModuleResponsible) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to remove this module.");
                                }
                            }
                            default ->
                                    throw new InsufficientPermissionsException("You do not have the required perissions");
                        }
                    }
                    case MODULE_FRAME_MODULE_IMPLEMENTATION -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE -> {
                                if (!isModuleResponsible) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to edit this resource.");
                                }else {
                                    List<UserEntity> notifyUsers = spoResponsibleUserRepository.findAllBySpoId(spoId).stream().map(SpoResponsibleUserEntity::getUser).toList();
                                    throw new NotifyException(notifyService,
                                            sessionService.getUserBySessionId(UUID.fromString(sessionToken)),
                                            notifyUsers,
                                            NOTIFICATION.MODULE_ADDED_TO_SPO,
                                            moduleImplementationEntity,
                                            spoRepository.findById(spoId).orElseThrow());
                                }
                            }
                        }
                    }
                    case USER -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE, READ_DETAILS ->
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access users");
                        }
                    }
                    case GENERAL_SETTINGS -> {
                        switch (privileges) {
                            case ADD, UPDATE, DELETE ->
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access general settings");
                        }
                    }
                    case DOCUMENT -> {
                        switch (privileges) {
                            case READ, ADD, UPDATE, DELETE ->
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access documents");
                        }
                    }
                }
            }
        }
    }
}
