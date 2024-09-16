package de.modulo.backend.services;

import de.modulo.backend.converters.ModuleImplementationConverter;
import de.modulo.backend.dtos.ModuleImplementationDTO;
import de.modulo.backend.dtos.ModuleImplementationDTOFlat;
import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.repositories.ModuleImplementationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleImplementationService {
    private final ModuleImplementationRepository moduleImplementationRepository;
    private final ModuleImplementationConverter moduleImplementationConverter;

    @Autowired
    public ModuleImplementationService(ModuleImplementationRepository moduleImplementationRepository,
                                       ModuleImplementationConverter moduleImplementationConverter) {
        this.moduleImplementationRepository = moduleImplementationRepository;
        this.moduleImplementationConverter = moduleImplementationConverter;
    }

    public List<ModuleImplementationDTOFlat> getAllModuleImplementations() {
        List<ModuleImplementationEntity> moduleImplementations = moduleImplementationRepository.findAll();
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

    public ModuleImplementationDTO updateModuleImplementation(ModuleImplementationDTO moduleImplementationDTO) {
        ModuleImplementationEntity moduleImplementationEntity = moduleImplementationConverter.toEntity(moduleImplementationDTO);
        ModuleImplementationEntity savedEntity = moduleImplementationRepository.save(moduleImplementationEntity);
        return moduleImplementationConverter.toDto(savedEntity);
    }

    public ModuleImplementationDTO getModuleImplementationById(Long id) {
        ModuleImplementationEntity moduleImplementationEntity = moduleImplementationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Module Implementation not found with id: " + id));
        return moduleImplementationConverter.toDto(moduleImplementationEntity);
    }
}
