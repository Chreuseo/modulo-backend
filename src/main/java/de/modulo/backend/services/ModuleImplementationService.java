package de.modulo.backend.services;

import de.modulo.backend.converters.ModuleImplementationConverter;
import de.modulo.backend.dtos.ModuleImplementationDTO;
import de.modulo.backend.dtos.ModuleImplementationDTOFlat;
import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.ModuleImplementationLecturerEntity;
import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.excpetions.InsufficientPermissionsException;
import de.modulo.backend.repositories.ModuleImplementationLecturerRepository;
import de.modulo.backend.repositories.ModuleImplementationRepository;
import de.modulo.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleImplementationService {
    private final ModuleImplementationRepository moduleImplementationRepository;
    private final ModuleImplementationConverter moduleImplementationConverter;
    private final ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;
    private final UserRepository userRepository;

    @Autowired
    public ModuleImplementationService(ModuleImplementationRepository moduleImplementationRepository,
                                       ModuleImplementationConverter moduleImplementationConverter,
                                       ModuleImplementationLecturerRepository moduleImplementationLecturerRepository, UserRepository userRepository) {
        this.moduleImplementationRepository = moduleImplementationRepository;
        this.moduleImplementationConverter = moduleImplementationConverter;
        this.moduleImplementationLecturerRepository = moduleImplementationLecturerRepository;
        this.userRepository = userRepository;
    }

    public List<ModuleImplementationDTOFlat> getAllModuleImplementations() {
        List<ModuleImplementationEntity> moduleImplementations = moduleImplementationRepository.findAll();
        return moduleImplementations.stream()
                .map(moduleImplementationConverter::toDtoFlat)
                .collect(Collectors.toList());
    }

    public List<ModuleImplementationDTOFlat> getAllAssignedModuleImplementations(Long userId) {
        List<ModuleImplementationEntity> moduleImplementations = new ArrayList<>(moduleImplementationRepository.getModuleImplementationEntitiesByResponsibleId(userId));
        moduleImplementations.addAll(moduleImplementationLecturerRepository
                .getModuleImplementationLecturerEntitiesByLecturerId(userId).stream()
                .map(ModuleImplementationLecturerEntity::getModuleImplementation).toList());
        return moduleImplementations.stream()
                .map(moduleImplementationConverter::toDtoFlat)
                .collect(Collectors.toList());
    }

    public ModuleImplementationDTOFlat addModuleImplementation(ModuleImplementationDTOFlat moduleImplementationDTOFlat) {
        ModuleImplementationEntity moduleImplementationEntity = moduleImplementationConverter.toEntity(moduleImplementationDTOFlat);
        ModuleImplementationEntity savedEntity = moduleImplementationRepository.save(moduleImplementationEntity);
        return moduleImplementationConverter.toDtoFlat(savedEntity);
    }

    public void deleteModuleImplementation(Long id) {
        moduleImplementationRepository.deleteById(id);
    }

    public ModuleImplementationDTO updateModuleImplementation(ModuleImplementationDTO moduleImplementationDTO, UserEntity user) throws InsufficientPermissionsException{
        ModuleImplementationEntity moduleImplementationEntity = moduleImplementationConverter.toEntity(moduleImplementationDTO);
        ModuleImplementationEntity oldEntity = moduleImplementationRepository.findById(moduleImplementationDTO.getId()).orElseThrow(() -> new IllegalArgumentException("Module Implementation not found with id: " + moduleImplementationDTO.getId()));
        if(moduleImplementationEntity.getResponsible() != null
                && !moduleImplementationEntity.getResponsible().equals(oldEntity.getResponsible())){
            if(oldEntity.getResponsible() != null
                    && !oldEntity.getResponsible().equals(user)
                    && !user.getRole().equals(ROLE.ADMIN)){
                throw new InsufficientPermissionsException("You are not allowed to change the responsible of this module implementation");
            }
        }
        ModuleImplementationEntity savedEntity = moduleImplementationRepository.save(moduleImplementationEntity);
        return moduleImplementationConverter.toDto(savedEntity);
    }

    public ModuleImplementationDTO getModuleImplementationById(Long id) {
        ModuleImplementationEntity moduleImplementationEntity = moduleImplementationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Module Implementation not found with id: " + id));
        return moduleImplementationConverter.toDto(moduleImplementationEntity);
    }

    public ModuleImplementationDTOFlat getModuleImplementationFlatById(Long id) {
        ModuleImplementationEntity moduleImplementationEntity = moduleImplementationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Module Implementation not found with id: " + id));
        return moduleImplementationConverter.toDtoFlat(moduleImplementationEntity);
    }

    public ModuleImplementationDTO addLecturerToModuleImplementation(Long moduleImplementationId, Long lecturerId) {
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId id = new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId();
        id.setModuleImplementation(moduleImplementationId);
        id.setLecturer(lecturerId);

        ModuleImplementationEntity moduleImplementationEntity = moduleImplementationRepository.findById(moduleImplementationId).orElseThrow(() -> new IllegalArgumentException("Module Implementation not found with id: " + moduleImplementationId));
        UserEntity lecturer = userRepository.findById(lecturerId).orElseThrow(() -> new IllegalArgumentException("Lecturer not found with id: " + lecturerId));

        ModuleImplementationLecturerEntity moduleImplementationLecturerEntity = new ModuleImplementationLecturerEntity();
        moduleImplementationLecturerEntity.setId(id);
        moduleImplementationLecturerEntity.setModuleImplementation(moduleImplementationEntity);
        moduleImplementationLecturerEntity.setLecturer(lecturer);

        moduleImplementationLecturerRepository.save(moduleImplementationLecturerEntity);
        return moduleImplementationConverter.toDto(moduleImplementationEntity);
    }

    public void removeLecturerFromModuleImplementation(Long moduleImplementationId, Long lecturerId) {
        ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId id = new ModuleImplementationLecturerEntity.ModuleImplementationLecturerEntityId();
        id.setModuleImplementation(moduleImplementationId);
        id.setLecturer(lecturerId);
        moduleImplementationLecturerRepository.deleteById(id);
    }
}
