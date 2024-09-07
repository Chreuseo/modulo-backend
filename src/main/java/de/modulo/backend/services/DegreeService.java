package de.modulo.backend.services;

import de.modulo.backend.converters.DegreeConverter;
import de.modulo.backend.dtos.DegreeDTO;
import de.modulo.backend.entities.DegreeEntity;
import de.modulo.backend.repositories.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DegreeService {

    private final DegreeRepository degreeRepository;
    private final DegreeConverter degreeConverter;

    @Autowired
    public DegreeService(DegreeRepository degreeRepository, DegreeConverter degreeConverter) {
        this.degreeRepository = degreeRepository;
        this.degreeConverter = degreeConverter;
    }

    // Get all degrees
    public List<DegreeDTO> getAll() {
        return degreeRepository.findAll().stream()
                .map(degreeConverter::toDto)
                .collect(Collectors.toList());
    }

    // Add new degree
    public DegreeDTO add(DegreeDTO dto) {
        // Check if the degree already exists
        if (degreeRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException("Degree already exists with name: " + dto.getName());
        }

        // Convert DTO to entity and save
        DegreeEntity degreeEntity = degreeConverter.toEntity(dto);
        DegreeEntity savedEntity = degreeRepository.save(degreeEntity);
        return degreeConverter.toDto(savedEntity);
    }

    // Delete a degree by ID
    public void delete(Long id) {
        if (!degreeRepository.existsById(id)) {
            throw new IllegalArgumentException("Degree not found with id: " + id);
        }
        degreeRepository.deleteById(id);
    }
}
