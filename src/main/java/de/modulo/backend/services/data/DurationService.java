package de.modulo.backend.services.data;
import de.modulo.backend.converters.DurationConverter;
import de.modulo.backend.dtos.DurationDTO;
import de.modulo.backend.entities.DurationEntity;
import de.modulo.backend.repositories.DurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DurationService {

    private final DurationRepository durationRepository;
    private final DurationConverter durationConverter; // Optional, if using converter

    @Autowired
    public DurationService(DurationRepository durationRepository, DurationConverter durationConverter) {
        this.durationRepository = durationRepository;
        this.durationConverter = durationConverter;
    }

    // Get all durations
    public List<DurationDTO> getAll() {
        return durationRepository.findAll().stream()
                .map(durationConverter::toDto) // Convert to DTO
                .collect(Collectors.toList());
    }

    // Add a new duration
    public DurationDTO add(DurationDTO durationDto) {
        // Chreck if the duration already exists
        if (durationRepository.findByName(durationDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Duration already exists with name: " + durationDto.getName());
        }

        DurationEntity durationEntity = durationConverter.toEntity(durationDto); // Convert DTO to Entity
        DurationEntity savedEntity = durationRepository.save(durationEntity);
        return durationConverter.toDto(savedEntity); // Convert back to DTO on return
    }

    // Delete a duration by ID
    public void delete(Long id) {
        if (!durationRepository.existsById(id)) {
            throw new IllegalArgumentException("Duration not found with id: " + id);
        }
        durationRepository.deleteById(id);
    }
}
