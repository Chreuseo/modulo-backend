package de.modulo.backend.services;

import de.modulo.backend.converters.ModuleImplementationConverter;
import de.modulo.backend.dtos.ModuleImplementationDTOFlat;
import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.repositories.ModuleImplementationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleImplementationService {
    private final ModuleImplementationRepository moduleImplementationRepository;
    private final ModuleImplementationConverter moduleImplementationConverter;

    @Autowired
    public ModuleImplementationService(ModuleImplementationRepository moduleImplementationRepository, ModuleImplementationConverter moduleImplementationConverter) {
        this.moduleImplementationRepository = moduleImplementationRepository;
        this.moduleImplementationConverter = moduleImplementationConverter;
    }

    @GetMapping("/api/module-implementation/all")
    public List<ModuleImplementationDTOFlat> getAllModuleImplementations() {
        List<ModuleImplementationEntity> moduleImplementations = moduleImplementationRepository.findAll();
        return moduleImplementations.stream()
                .map(moduleImplementationConverter::toDtoFlat)
                .collect(Collectors.toList());
    }
}
