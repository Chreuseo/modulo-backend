package de.modulo.backend.services;

import de.modulo.backend.converters.ModuleTypeConverter;
import de.modulo.backend.converters.SectionConverter;
import de.modulo.backend.converters.SpoConverter;
import de.modulo.backend.dtos.SpoDTO;
import de.modulo.backend.dtos.SpoDTOFlat;
import de.modulo.backend.entities.ModuleTypeEntity;
import de.modulo.backend.entities.SectionEntity;
import de.modulo.backend.entities.SpoEntity;
import de.modulo.backend.repositories.ModuleTypeRepository;
import de.modulo.backend.repositories.SectionRepository;
import de.modulo.backend.repositories.SpoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpoService {

    private final SpoRepository spoRepository;
    private final SpoConverter spoConverter;
    private final SectionRepository sectionRepository;
    private final ModuleTypeRepository moduleTypeRepository;
    private final SectionConverter sectionConverter;
    private final ModuleTypeConverter moduleTypeConverter;
    private final ExamTypeService examTypeService;

    @Autowired
    public SpoService(SpoRepository spoRepository, SpoConverter spoConverter, SectionRepository sectionRepository, ModuleTypeRepository moduleTypeRepository, SectionConverter sectionConverter, ModuleTypeConverter moduleTypeConverter, ExamTypeService examTypeService) {
        this.spoRepository = spoRepository;
        this.spoConverter = spoConverter;
        this.sectionRepository = sectionRepository;

        this.moduleTypeRepository = moduleTypeRepository;
        this.sectionConverter = sectionConverter;
        this.moduleTypeConverter = moduleTypeConverter;
        this.examTypeService = examTypeService;
    }

    public List<SpoDTOFlat> getAllSpos() {
        List<SpoEntity> spos = spoRepository.findAll();
        return spos.stream()
                .map(spoConverter::toDtoFlat)
                .collect(Collectors.toList());
    }

    public SpoDTOFlat add(SpoDTOFlat spoDto) {
        SpoEntity spoEntity = spoConverter.toEntity(spoDto);
        SpoEntity savedEntity = spoRepository.save(spoEntity);
        return spoConverter.toDtoFlat(savedEntity);
    }

    public void delete(Long id) {
        if (!spoRepository.existsById(id)) {
            throw new IllegalArgumentException("Spo not found with id: " + id);
        }
        spoRepository.deleteById(id);
    }

    public SpoDTO update(SpoDTO spoDto) {
        // Check if the spo already exists
        if (!spoRepository.existsById(spoDto.getId())) {
            throw new IllegalArgumentException("Spo not found with id: " + spoDto.getId());
        }

        SpoEntity savedEntity = spoRepository.save(spoConverter.toEntity(spoDto));
        spoDto.getSectionDTOs().forEach(sectionDTO -> {
            SectionEntity sectionEntity = sectionConverter.toEntity(sectionDTO);
            sectionEntity.setSpo(savedEntity);
            sectionRepository.save(sectionEntity);
        });
        spoDto.getModuleTypeDTOs().forEach(moduleTypeDTO -> {
            ModuleTypeEntity moduleTypeEntity = moduleTypeConverter.toEntity(moduleTypeDTO);
            moduleTypeEntity.setSpo(savedEntity);
            moduleTypeRepository.save(moduleTypeEntity);
        });

        return spoConverter.toDto(savedEntity);
    }

    public SpoDTO getSpo(Long id) {
        SpoEntity spoEntity = spoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Spo not found with id: " + id));

        SpoDTO spoDTO = spoConverter.toDto(spoEntity);

        List<SectionEntity> sections = sectionRepository.findBySpoId(id);
        sections.sort(Comparator.comparing(SectionEntity::getOrderNumber));
        List<ModuleTypeEntity> moduleTypes = moduleTypeRepository.findBySpoId(id);
        moduleTypes.sort(Comparator.comparing(ModuleTypeEntity::getOrderNumber));

        spoDTO.setSectionDTOs(sections.stream()
                .map(sectionConverter::toDto)
                .collect(Collectors.toList()));
        spoDTO.setModuleTypeDTOs(moduleTypes.stream()
                .map(moduleTypeConverter::toDto)
                .collect(Collectors.toList()));

        spoDTO.setExamTypeDTOs(examTypeService.getBySpo(id));

        return spoDTO;
    }
}
