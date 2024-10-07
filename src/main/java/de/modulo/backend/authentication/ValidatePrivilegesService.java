package de.modulo.backend.authentication;

import de.modulo.backend.entities.ModuleImplementationLecturerEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.enums.ENTITY_TYPE;
import de.modulo.backend.enums.PRIVILEGES;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ValidatePrivilegesService {

    private final SessionService sessionService;
    private final NotifyService notifyService;
    private final ModuleImplementationRepository moduleImplementationRepository;
    private final ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;
    private final SpoResponsibleUserRepository spoResponsibleUserRepository;
    private final SpoRepository spoRepository;

    @Autowired
    public ValidatePrivilegesService(SessionService sessionService,
                                     NotifyService notifyService,
                                     ModuleImplementationRepository moduleImplementationRepository,
                                     ModuleImplementationLecturerRepository moduleImplementationLecturerRepository,
                                     SpoResponsibleUserRepository spoResponsibleUserRepository, SpoRepository spoRepository) {
        this.sessionService = sessionService;
        this.notifyService = notifyService;
        this.moduleImplementationRepository = moduleImplementationRepository;
        this.moduleImplementationLecturerRepository = moduleImplementationLecturerRepository;
        this.spoResponsibleUserRepository = spoResponsibleUserRepository;
        this.spoRepository = spoRepository;
    }

    public void validateGeneralPrivileges(ENTITY_TYPE entityType, PRIVILEGES privileges, String sessionToken) throws InsufficientPermissionsException{
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
                            case ADD, UPDATE, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access SPO settings");
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
            default:
                throw new InsufficientPermissionsException("You do not have the required permissions to access this resource");
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
                            case ADD, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access SPOs");
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

    public void validateModuleSpecificPrivileges(ENTITY_TYPE entityType, PRIVILEGES privileges, String sessionToken, Long moduleImplementationId){
        ROLE role = sessionService.getRoleBySessionId(UUID.fromString(sessionToken));
        boolean isResponsible = moduleImplementationRepository.findById(moduleImplementationId).orElseThrow().getResponsible().getId().equals(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken)));
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId moduleImplementationLecturerEntityId = new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId();
        moduleImplementationLecturerEntityId.setModuleImplementation(moduleImplementationId);
        moduleImplementationLecturerEntityId.setLecturer(sessionService.getUserIdBySessionId(UUID.fromString(sessionToken)));
        boolean isLecturer = moduleImplementationLecturerRepository.existsById(moduleImplementationLecturerEntityId);

        switch (role){
            case ADMIN -> {
                return;
            }
            case SPO_ADMIN -> {
                switch (entityType){
                    case MODULE -> {
                        switch (privileges){
                            case ADD, DELETE -> throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
                            case UPDATE -> {
                                if (!isLecturer) {
                                    throw new InsufficientPermissionsException("You do not have the required permissions to access modules");
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
                                if (!isLecturer) {
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
        }
    }
}
