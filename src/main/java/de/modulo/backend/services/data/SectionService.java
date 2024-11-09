package de.modulo.backend.services.data;

import de.modulo.backend.converters.SectionConverter;
import de.modulo.backend.dtos.SectionDTO;
import de.modulo.backend.entities.SectionEntity;
import de.modulo.backend.repositories.SectionRepository;
import de.modulo.backend.repositories.SpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final SectionConverter sectionConverter;

    private final SpoRepository spoRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository, SectionConverter sectionConverter, SpoRepository spoRepository) {
        this.sectionRepository = sectionRepository;
        this.sectionConverter = sectionConverter;

        this.spoRepository = spoRepository;
    }

    public SectionDTO add(SectionDTO sectionDTO) {
        SectionEntity sectionEntity = sectionConverter.toEntity(sectionDTO);
        SectionEntity savedEntity = sectionRepository.save(sectionEntity);
        return sectionConverter.toDto(savedEntity);
    }

    public void delete(Long id) {
        if (!sectionRepository.existsById(id)) {
            throw new IllegalArgumentException("Section not found with id: " + id);
        }
        sectionRepository.deleteById(id);
    }

    public List<SectionDTO> getBySpo(Long spoId){
        List<SectionEntity> sectionEntities = sectionRepository.findBySpoId(spoId);

        return sectionEntities.stream()
                .map(sectionConverter::toDto)
                .toList();
    }
}