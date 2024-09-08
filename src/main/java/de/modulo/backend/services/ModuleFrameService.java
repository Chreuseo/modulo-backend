package de.modulo.backend.services;
import de.modulo.backend.converters.CourseTypeConverter;
import de.modulo.backend.converters.ModuleFrameConverter;

import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.dtos.ModuleFrameSetDTO;
import de.modulo.backend.entities.*;
import de.modulo.backend.repositories.CourseTypeModuleFrameRepository;
import de.modulo.backend.repositories.ModuleFrameRepository;
import de.modulo.backend.repositories.ModuleTypeRepository;
import de.modulo.backend.repositories.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleFrameService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ModuleTypeRepository moduleTypeRepository;

    @Autowired
    private ModuleFrameRepository moduleFrameRepository; // Assuming you have a repository for ModuleFrame

    @Autowired
    CourseTypeConverter courseTypeConverter;

    @Autowired
    private ModuleFrameConverter moduleFrameConverter;

    @Autowired
    private CourseTypeModuleFrameRepository courseTypeModuleFrameRepository;


    public ModuleFrameSetDTO getModuleFrameSetDTOBySpoId(Long spoId) {
        // Create the outer DTO
        ModuleFrameSetDTO moduleFrameSetDto = new ModuleFrameSetDTO();

        // Fetch sections based on the provided spoId
        List<SectionEntity> sectionEntities = sectionRepository.findBySpoId(spoId);
        sectionEntities.sort(Comparator.comparingInt(SectionEntity::getOrderNumber));
        List<ModuleFrameSetDTO.Section> sections = new ArrayList<>();

        for (SectionEntity sectionEntity : sectionEntities) {
            // Convert SectionEntity to SectionDTO
            ModuleFrameSetDTO.Section sectionDto = new ModuleFrameSetDTO.Section();
            sectionDto.setId(sectionEntity.getId());
            sectionDto.setName(sectionEntity.getName());
            sectionDto.setIndex(sectionEntity.getOrderNumber());

            // Fetch ModuleTypes for the current Section
            List<ModuleTypeEntity> moduleTypeEntities = moduleTypeRepository.findBySpoId(spoId);
            moduleTypeEntities.sort(Comparator.comparingInt(ModuleTypeEntity::getOrderNumber));
            List<ModuleFrameSetDTO.Section.ModuleType> moduleTypes = new ArrayList<>();

            for (ModuleTypeEntity moduleTypeEntity : moduleTypeEntities) {
                // Convert ModuleTypeEntity to ModuleTypeDTO
                ModuleFrameSetDTO.Section.ModuleType moduleTypeDto = new ModuleFrameSetDTO.Section.ModuleType();
                moduleTypeDto.setId(moduleTypeEntity.getId());
                moduleTypeDto.setName(moduleTypeEntity.getName());
                moduleTypeDto.setIndex(moduleTypeEntity.getOrderNumber());

                // Fetch ModuleFrames that match the current Section & ModuleType
                List<ModuleFrameDTO> moduleFrames = moduleFrameRepository.findBySectionIdAndModuleTypeId(sectionEntity.getId(), moduleTypeEntity.getId()).stream()
                        .map(moduleFrameConverter::toDto)
                        .collect(Collectors.toList());
                moduleTypeDto.setModuleFrames(moduleFrames);

                // Add the ModuleType to the list of ModuleTypes for this Section
                moduleTypes.add(moduleTypeDto);
            }

            // Set the list of ModuleTypes in the Section DTO
            sectionDto.setModuleTypes(moduleTypes);
            // Add the Section DTO to the list of Sections
            sections.add(sectionDto);
        }

        // Set the sections in the outer DTO
        moduleFrameSetDto.setSections(sections);

        return moduleFrameSetDto;
    }

    public ModuleFrameDTO addModuleFrame(ModuleFrameDTO moduleFrameDTO) {
        // Convert the DTO to Entity
        ModuleFrameEntity moduleFrameEntity = moduleFrameConverter.toEntity(moduleFrameDTO);
        // Save the Entity
        ModuleFrameEntity savedEntity = moduleFrameRepository.save(moduleFrameEntity);
        ModuleFrameDTO savedModuleFrameDTO = moduleFrameConverter.toDto(savedEntity);
        savedModuleFrameDTO.setCourseTypes(moduleFrameDTO.getCourseTypes());

        processCourseTypes(savedModuleFrameDTO);

        // Convert the saved Entity back to DTO
        return moduleFrameDTO;
    }

    public void processCourseTypes(ModuleFrameDTO moduleFrameDTO){
        if(moduleFrameDTO.getCourseTypes() != null){
            moduleFrameDTO.getCourseTypes().forEach(courseTypeDTO -> {
                if(courseTypeDTO.isEnabled()){
                    CourseTypeModuleFrameEntity courseTypeModuleFrameEntity = new CourseTypeModuleFrameEntity();
                    courseTypeModuleFrameEntity.setCourseType(courseTypeConverter.toEntity(courseTypeDTO));
                    courseTypeModuleFrameEntity.setModuleFrame(moduleFrameConverter.toEntity(moduleFrameDTO));
                    courseTypeModuleFrameEntity.setId(new CourseTypeModuleFrameEntity.CourseTypeModuleFrameId(courseTypeModuleFrameEntity.getCourseType().getId(), courseTypeModuleFrameEntity.getModuleFrame().getId()));

                    courseTypeModuleFrameRepository.save(courseTypeModuleFrameEntity);
                }else{

                    CourseTypeModuleFrameEntity.CourseTypeModuleFrameId courseTypeModuleFrameId = new CourseTypeModuleFrameEntity.CourseTypeModuleFrameId(courseTypeDTO.getId(), moduleFrameDTO.getId());

                    if (courseTypeModuleFrameRepository.existsById(courseTypeModuleFrameId)) {
                        courseTypeModuleFrameRepository.deleteById(courseTypeModuleFrameId);
                    }
                }
            });
        }
    }

    public void deleteModuleFrame(Long id) {
        if (!moduleFrameRepository.existsById(id)) {
            throw new IllegalArgumentException("ModuleFrame not found with id: " + id);
        }
        moduleFrameRepository.deleteById(id);
    }

    public ModuleFrameDTO updateModuleFrame(ModuleFrameDTO moduleFrameDTO) {
        // Check if the ModuleFrame already exists
        if (!moduleFrameRepository.existsById(moduleFrameDTO.getId())) {
            throw new IllegalArgumentException("ModuleFrame not found with id: " + moduleFrameDTO.getId());
        }

        processCourseTypes(moduleFrameDTO);

        // Convert the DTO to Entity
        ModuleFrameEntity savedEntity = moduleFrameRepository.save(moduleFrameConverter.toEntity(moduleFrameDTO));
        return moduleFrameConverter.toDto(savedEntity);
    }
}

