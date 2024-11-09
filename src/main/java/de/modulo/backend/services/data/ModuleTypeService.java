package de.modulo.backend.services.data;

import de.modulo.backend.converters.ModuleTypeConverter;
import de.modulo.backend.dtos.ModuleTypeDTO;
import de.modulo.backend.entities.ModuleTypeEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.ModuleTypeRepository;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleTypeService {

    private final ModuleTypeRepository moduleTypeRepository;
    private final ModuleTypeConverter moduleTypeConverter;

    private final SpoRepository spoRepository;

    @Autowired
    public ModuleTypeService(ModuleTypeRepository moduleTypeRepository, ModuleTypeConverter moduleTypeConverter, SpoRepository spoRepository) {
        this.moduleTypeRepository = moduleTypeRepository;
        this.moduleTypeConverter = moduleTypeConverter;

        this.spoRepository = spoRepository;
    }

    public ModuleTypeDTO add(ModuleTypeDTO moduleTypeDTO) {
        ModuleTypeEntity moduleTypeEntity = moduleTypeConverter.toEntity(moduleTypeDTO);
        ModuleTypeEntity savedEntity = moduleTypeRepository.save(moduleTypeEntity);
        return moduleTypeConverter.toDto(savedEntity);
    }

    public void delete(Long id) {
        if (!moduleTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("ModuleType not found with id: " + id);
        }
        moduleTypeRepository.deleteById(id);
    }

    public List<ModuleTypeDTO> getBySpo(Long spoId){
        SpoEntity spoEntity = spoRepository.findById(spoId).orElseThrow(() -> new IllegalArgumentException("Spo not found with id: " + spoId));
        List<ModuleTypeEntity> moduleTypeEntities = moduleTypeRepository.findBySpoId(spoId);

        return moduleTypeEntities.stream()
                .map(moduleTypeConverter::toDto)
                .toList();
    }

}
