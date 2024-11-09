package de.modulo.backend.services.data;

import de.modulo.backend.converters.ModuleRequirementConverter;
import de.modulo.backend.dtos.ModuleRequirementDTO;
import de.modulo.backend.entities.ModuleRequirementEntity;
import de.modulo.backend.repositories.ModuleRequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleRequirementService {

    @Autowired
    private ModuleRequirementRepository moduleRequirementRepository; // Assuming you have this repository
    @Autowired
    private ModuleRequirementConverter converter;

    public ModuleRequirementDTO addModuleRequirement(ModuleRequirementDTO dto) {
        ModuleRequirementEntity entity = converter.toEntity(dto);
        ModuleRequirementEntity savedEntity = moduleRequirementRepository.save(entity);
        return converter.toDto(savedEntity);
    }

    public void delete(long id) {
        moduleRequirementRepository.deleteById(id);
    }

    public ModuleRequirementDTO update(ModuleRequirementDTO dto) {
        ModuleRequirementEntity existingEntity = moduleRequirementRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("ModuleRequirement not found"));
        existingEntity.setName(dto.getName());
        existingEntity.setSpo(converter.toEntity(dto).getSpo()); // Assuming SPOs are managed appropriately
        ModuleRequirementEntity updatedEntity = moduleRequirementRepository.save(existingEntity);
        return converter.toDto(updatedEntity);
    }

    public List<ModuleRequirementDTO> getBySpoId(long spoId) {
        List<ModuleRequirementEntity> entities = moduleRequirementRepository.findBySpoId(spoId); // Assuming you have this in your repository
        return entities.stream()
                .map(converter::toDto)
                .toList();
    }
}
