package de.modulo.backend.services.data;

import de.modulo.backend.converters.MaternityProtectionConverter;
import de.modulo.backend.dtos.MaternityProtectionDTO;
import de.modulo.backend.entities.MaternityProtectionEntity;
import de.modulo.backend.repositories.MaternityProtectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaternityProtectionService {

    private final MaternityProtectionRepository maternityProtectionRepository;
    private final MaternityProtectionConverter maternityProtectionConverter; // Optional

    @Autowired
    public MaternityProtectionService(MaternityProtectionRepository maternityProtectionRepository,
                                      MaternityProtectionConverter maternityProtectionConverter) {
        this.maternityProtectionRepository = maternityProtectionRepository;
        this.maternityProtectionConverter = maternityProtectionConverter;
    }

    // Get all maternity protections
    public List<MaternityProtectionDTO> getAll() {
        return maternityProtectionRepository.findAll().stream()
                .map(maternityProtectionConverter::toDto) // Convert to DTO
                .collect(Collectors.toList());
    }

    // Add a new maternity protection
    public MaternityProtectionDTO add(MaternityProtectionDTO maternityProtectionDto) {
        // Check if the maternity protection already exists
        if (maternityProtectionRepository.findByName(maternityProtectionDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Maternity Protection already exists with name: " + maternityProtectionDto.getName());
        }

        MaternityProtectionEntity maternityProtectionEntity = maternityProtectionConverter.toEntity(maternityProtectionDto);
        MaternityProtectionEntity savedEntity = maternityProtectionRepository.save(maternityProtectionEntity);
        return maternityProtectionConverter.toDto(savedEntity); // Convert back to DTO
    }

    // Delete a maternity protection by ID
    public void delete(Long id) {
        if (!maternityProtectionRepository.existsById(id)) {
            throw new IllegalArgumentException("Maternity Protection entry not found with id: " + id);
        }
        maternityProtectionRepository.deleteById(id);
    }
}
