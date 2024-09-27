package de.modulo.backend.services;

import de.modulo.backend.converters.*;
import de.modulo.backend.dtos.SpoDTO;
import de.modulo.backend.dtos.SpoDTOFlat;
import de.modulo.backend.entities.*;
import de.modulo.backend.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpoService {

    private final SpoRepository spoRepository;
    private final SpoConverter spoConverter;
    private final SectionConverter sectionConverter;
    private final ModuleRequirementConverter moduleRequirementConverter;

    private final SectionRepository sectionRepository;
    private final ModuleTypeRepository moduleTypeRepository;
    private final ModuleTypeConverter moduleTypeConverter;
    private final ExamTypeService examTypeService;
    private final ModuleRequirementRepository moduleRequirementRepository;
    private final SpoResponsibleUserRepository spoResponsibleUserRepository;
    private final UserConverter userConverter;
    private final UserRepository userRepository;

    @Autowired
    public SpoService(SpoRepository spoRepository,
                      SpoConverter spoConverter,
                      ModuleRequirementConverter moduleRequirementConverter,
                      SectionRepository sectionRepository,
                      ModuleTypeRepository moduleTypeRepository,
                      SectionConverter sectionConverter,
                      ModuleTypeConverter moduleTypeConverter,
                      ExamTypeService examTypeService,
                      ModuleRequirementRepository moduleRequirementRepository,
                      SpoResponsibleUserRepository spoResponsibleUserRepository,
                      UserConverter userConverter,
                      UserRepository userRepository) {
        this.spoRepository = spoRepository;
        this.spoConverter = spoConverter;
        this.sectionRepository = sectionRepository;
        this.moduleRequirementRepository = moduleRequirementRepository;
        this.moduleRequirementConverter = moduleRequirementConverter;

        this.moduleTypeRepository = moduleTypeRepository;
        this.sectionConverter = sectionConverter;
        this.moduleTypeConverter = moduleTypeConverter;
        this.examTypeService = examTypeService;
        this.spoResponsibleUserRepository = spoResponsibleUserRepository;
        this.userConverter = userConverter;
        this.userRepository = userRepository;
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

        spoDto.getModuleRequirementDTOs().forEach(moduleRequirementDTO -> {
            ModuleRequirementEntity moduleRequirementEntity = moduleRequirementConverter.toEntity(moduleRequirementDTO);
            moduleRequirementEntity.setSpo(savedEntity);
            moduleRequirementRepository.save(moduleRequirementEntity);
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
        spoDTO.setModuleRequirementDTOs(moduleRequirementRepository.findBySpoId(id).stream()
                .map(moduleRequirementConverter::toDto)
                .collect(Collectors.toList()));
        spoDTO.setResponsibleUsers(spoResponsibleUserRepository.findAllBySpoId(id).stream()
                .map(SpoResponsibleUserEntity::getUser)
                .map(userConverter::toDtoFlat)
                .collect(Collectors.toList()));

        spoDTO.setExamTypeDTOs(examTypeService.getBySpo(id));

        return spoDTO;
    }

    public void addResponsible(Long id, Long userId) {
        SpoEntity spoEntity = spoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Spo not found with id: " + id));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        SpoResponsibleUserEntity spoResponsibleUserEntity = new SpoResponsibleUserEntity();
        spoResponsibleUserEntity.setSpo(spoEntity);
        spoResponsibleUserEntity.setUser(userEntity);
        SpoResponsibleUserEntity.SpoResponsibleUserId spoResponsibleUserId = new SpoResponsibleUserEntity.SpoResponsibleUserId();
        spoResponsibleUserId.setSpoId(id);
        spoResponsibleUserId.setUserId(userId);
        spoResponsibleUserEntity.setId(spoResponsibleUserId);
        spoResponsibleUserRepository.save(spoResponsibleUserEntity);
    }

    public void removeResponsible(Long id, Long userId) {
        SpoResponsibleUserEntity.SpoResponsibleUserId spoResponsibleUserId = new SpoResponsibleUserEntity.SpoResponsibleUserId();
        spoResponsibleUserId.setSpoId(id);
        spoResponsibleUserId.setUserId(userId);
        if(spoResponsibleUserRepository.existsById(spoResponsibleUserId)) {
            spoResponsibleUserRepository.deleteById(spoResponsibleUserId);
        }else{
            throw new IllegalArgumentException("Responsible user not found with spoId: " + id + " and userId: " + userId);
        }

    }
}
