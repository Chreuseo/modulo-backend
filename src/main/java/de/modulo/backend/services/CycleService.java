package de.modulo.backend.services;

import de.modulo.backend.converters.CycleConverter;
import de.modulo.backend.dtos.CycleDTO;
import de.modulo.backend.entities.CycleEntity;
import de.modulo.backend.repositories.CycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CycleService {

    @Autowired
    private CycleRepository cycleRepository; // Assume you have this repository interface

    @Autowired
    private CycleConverter cycleConverter;

    public CycleDTO addCycle(CycleDTO cycleDto) {
        CycleEntity cycleEntity = cycleConverter.toEntity(cycleDto);
        CycleEntity savedEntity = cycleRepository.save(cycleEntity);
        return cycleConverter.toDto(savedEntity);
    }

    public CycleDTO updateCycle(CycleDTO cycleDto) {
        if (!cycleRepository.existsById(cycleDto.getId())) {
            throw new IllegalArgumentException("Cycle not found with id " + cycleDto.getId());
        }
        CycleEntity cycleEntity = cycleConverter.toEntity(cycleDto);
        CycleEntity updatedEntity = cycleRepository.save(cycleEntity);
        return cycleConverter.toDto(updatedEntity);
    }

    public void removeCycle(Long id) {
        if (!cycleRepository.existsById(id)) {
            throw new IllegalArgumentException("Cycle not found with id " + id);
        }
        cycleRepository.deleteById(id);
    }

    public List<CycleDTO> getAllCycles() {
        List<CycleEntity> cycleEntities = cycleRepository.findAll();
        return cycleEntities.stream()
                .map(cycleConverter::toDto)
                .collect(Collectors.toList());
    }
}
