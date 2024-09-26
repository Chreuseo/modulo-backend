package de.modulo.backend.authentication;

import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.ModuleImplementationLecturerEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.ModuleImplementationLecturerRepository;
import de.modulo.backend.repositories.ModuleImplementationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ValidatePrivilegesService {

    private final SessionService sessionService;
    private final NotifyService notifyService;
    private final ModuleImplementationRepository moduleImplementationRepository;
    private final ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;

    @Autowired
    public ValidatePrivilegesService(SessionService sessionService,
                                     NotifyService notifyService,
                                     ModuleImplementationRepository moduleImplementationRepository,
                                     ModuleImplementationLecturerRepository moduleImplementationLecturerRepository) {
        this.sessionService = sessionService;
        this.notifyService = notifyService;
        this.moduleImplementationRepository = moduleImplementationRepository;
        this.moduleImplementationLecturerRepository = moduleImplementationLecturerRepository;
    }

    public void validatePrivileges(ENTITY_TYPE entityType, PRIVILEGES privileges, String sessionToken) throws InsufficientPermissionsException {
        ROLE role = sessionService.getRoleBySessionId(UUID.fromString(sessionToken));

        switch (entityType) {
            case GENERAL_SETTINGS:
                if(role != ROLE.ADMIN) {
                    switch (privileges) {
                        case ADD, UPDATE, DELETE:
                            throw new InsufficientPermissionsException("You do not have the required permissions to access general settings");
                    }
                }
                break;
            case SPO_SETTINGS:
                if(role != ROLE.ADMIN) {
                    switch (privileges) {
                        case ADD, UPDATE, DELETE:
                            throw new InsufficientPermissionsException("You do not have the required permissions to access SPO settings");
                    }
                }
                break;
            case SPO:
                if(role != ROLE.ADMIN) {
                    switch (privileges) {
                        case ADD, UPDATE, DELETE:
                            throw new InsufficientPermissionsException("You do not have the required permissions to access SPOs");
                    }
                }
                break;
            case MODULE:
                if(role != ROLE.ADMIN) {
                    switch (privileges) {
                        case ADD, UPDATE, DELETE:
                            throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
                    }
                }
                break;
            case MODULE_FRAME_MODULE_IMPLEMENTATION:
                if(role != ROLE.ADMIN) {
                    switch (privileges) {
                        case ADD, UPDATE, DELETE:
                            throw new InsufficientPermissionsException("You do not have the required permissions to access module frame module implementations");
                    }
                }
                break;
            case USER:
                if(role != ROLE.ADMIN) {
                    throw new InsufficientPermissionsException("You do not have the required permissions to access users");
                }
                break;
            default:
                throw new InsufficientPermissionsException("You do not have the required permissions to access this entity type");
        }
    }

    public void validatePrivileges(ENTITY_TYPE entityType, PRIVILEGES privileges, String sessionToken, Long id) throws InsufficientPermissionsException {
        ROLE role = sessionService.getRoleBySessionId(UUID.fromString(sessionToken));
        UserEntity userEntity = sessionService.getUserBySessionId(UUID.fromString(sessionToken));

        ModuleImplementationEntity moduleImplementationEntity;
        List<ModuleImplementationLecturerEntity> moduleImplementationLecturerEntities;

        if(role == ROLE.ADMIN) {
            return;
        }

        switch (entityType) {
            case GENERAL_SETTINGS:
                switch (privileges) {
                    case ADD, UPDATE, DELETE:
                        throw new InsufficientPermissionsException("You do not have the required permissions to access general settings");
                }
            case SPO_SETTINGS:
                switch (privileges) {
                    case ADD, UPDATE, DELETE:
                        throw new InsufficientPermissionsException("You do not have the required permissions to write SPO settings");
                }
                break;
            case SPO:
                switch (privileges) {
                    case ADD, UPDATE, DELETE:
                        throw new InsufficientPermissionsException("You do not have the required permissions to write SPOs");
                }
                break;
            case MODULE:
                moduleImplementationEntity = moduleImplementationRepository.findById(id).orElseThrow(() -> new InsufficientPermissionsException("You do not have the required permissions to read modules"));
                moduleImplementationLecturerEntities = moduleImplementationLecturerRepository.getModuleImplementationLecturerEntitiesByModuleImplementationId(id);
                switch (privileges) {
                    case READ:
                        if(!moduleImplementationEntity.getResponsible().equals(userEntity) && moduleImplementationLecturerEntities.stream().noneMatch(moduleImplementationLecturerEntity -> moduleImplementationLecturerEntity.getLecturer().equals(userEntity))) {
                            throw new InsufficientPermissionsException("You do not have the required permissions to read modules");
                        }
                        break;
                    case ADD:
                        throw new InsufficientPermissionsException("You do not have the required permissions to write modules");
                    case UPDATE:
                        if(moduleImplementationEntity.getResponsible().equals(userEntity)) {
                            break;
                        }else if(moduleImplementationLecturerEntities.stream().anyMatch(moduleImplementationLecturerEntity -> moduleImplementationLecturerEntity.getLecturer().equals(userEntity))) {
                            notifyService.notifyUser("User " + userEntity.getLastName() + " has updated module " + moduleImplementationEntity.getName(), moduleImplementationEntity.getResponsible());
                        }else {
                            throw new InsufficientPermissionsException("You do not have the required permissions to update modules");
                        }
                        break;
                    case DELETE:
                        if(!moduleImplementationEntity.getResponsible().equals(userEntity)) {
                            throw new InsufficientPermissionsException("You do not have the required permissions to delete modules");
                        }
                        break;
                }
                break;
            case MODULE_FRAME_MODULE_IMPLEMENTATION:
                moduleImplementationEntity = moduleImplementationRepository.findById(id).orElseThrow(() -> new InsufficientPermissionsException("You do not have the required permissions to read modules"));
                moduleImplementationLecturerEntities = moduleImplementationLecturerRepository.getModuleImplementationLecturerEntitiesByModuleImplementationId(id);
                switch (privileges) {
                    case READ:
                        if(!moduleImplementationEntity.getResponsible().equals(userEntity) && moduleImplementationLecturerEntities.stream().noneMatch(moduleImplementationLecturerEntity -> moduleImplementationLecturerEntity.getLecturer().equals(userEntity))) {
                            throw new InsufficientPermissionsException("You do not have the required permissions to read modules");
                        }
                        break;
                    case ADD, DELETE:
                        if(!moduleImplementationEntity.getResponsible().equals(userEntity)) {
                            throw new InsufficientPermissionsException("You do not have the required permissions to delete modules");
                        }
                        break;
                    case UPDATE:
                        if(!moduleImplementationEntity.getResponsible().equals(userEntity)) {
                            throw new InsufficientPermissionsException("You do not have the required permissions to update module frame module implementations");
                        }
                        break;
                }
                break;
            case USER:
                switch (privileges) {
                    case READ, ADD, UPDATE, DELETE:
                        throw new InsufficientPermissionsException("You do not have the required permissions to access users");
                }
                break;
            default:
                throw new InsufficientPermissionsException("You do not have the required permissions to access this entity type");
        }
    }
}
