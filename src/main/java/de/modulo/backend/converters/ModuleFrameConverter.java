package de.modulo.backend.converters;

import de.modulo.backend.dtos.CourseTypeDTO;
import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.entities.CourseTypeEntity;
import de.modulo.backend.entities.CourseTypeModuleFrameEntity;
import de.modulo.backend.entities.ModuleFrameEntity;
import de.modulo.backend.repositories.*;
import de.modulo.backend.services.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModuleFrameConverter {

    private final SectionConverter sectionConverter;
    private final ModuleTypeConverter moduleTypeConverter;
    private final CourseTypeConverter courseTypeConverter;

    private final SpoRepository spoRepository;
    private final SectionRepository sectionRepository;
    private final ModuleTypeRepository moduleTypeRepository;
    private final CourseTypeRepository courseTypeRepository;
    private final CourseTypeModuleFrameRepository courseTypeModuleFrameRepository;
    private final CourseTypeService courseTypeService;

    @Autowired
    public ModuleFrameConverter(SpoConverter spoConverter, SectionConverter sectionConverter, ModuleTypeConverter moduleTypeConverter, SpoRepository spoRepository, SectionRepository sectionRepository, ModuleTypeRepository moduleTypeRepository, CourseTypeRepository courseTypeRepository, CourseTypeConverter courseTypeConverter, CourseTypeModuleFrameRepository courseTypeModuleFrameRepository, CourseTypeService courseTypeService) {
        this.sectionConverter = sectionConverter;
        this.moduleTypeConverter = moduleTypeConverter;
        this.courseTypeConverter = courseTypeConverter;

        this.spoRepository = spoRepository;
        this.sectionRepository = sectionRepository;
        this.moduleTypeRepository = moduleTypeRepository;
        this.courseTypeRepository = courseTypeRepository;
        this.courseTypeModuleFrameRepository = courseTypeModuleFrameRepository;
        this.courseTypeService = courseTypeService;
    }

    public ModuleFrameDTO toDto(ModuleFrameEntity entity) {
        if (entity == null) {
            return null;
        }

        ModuleFrameDTO dto = new ModuleFrameDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setName(entity.getName());
        dto.setSws(entity.getSws());
        dto.setCourseType(entity.getCourseType());
        dto.setWeight(entity.getWeight());
        dto.setCredits(entity.getCredits());
        dto.setAllExamsMandatory(entity.isAllExamsMandatory());

        dto.setSpoId(entity.getSpo().getId());

        if (entity.getSection() != null) {
            dto.setSection(sectionConverter.toDto(entity.getSection()));
        }

        if (entity.getModuleType() != null) {
            dto.setModuleType(moduleTypeConverter.toDto(entity.getModuleType()));
        }

        List<CourseTypeEntity> courseTypes = courseTypeRepository.findAll();
        List<CourseTypeDTO> courseTypeDTOs = new ArrayList<>();
        List<CourseTypeEntity> usedCourseTypes = courseTypeModuleFrameRepository
                .getCourseTypeModuleFrameEntitiesByModuleFrame(entity).stream()
                .map(CourseTypeModuleFrameEntity::getCourseType)
                .toList();

        courseTypeRepository.findAll().forEach(courseTypeEntity -> {
            CourseTypeDTO courseTypeDTO = courseTypeConverter.toDto(courseTypeEntity);
            if(usedCourseTypes.contains(courseTypeEntity)) {
                courseTypeDTO.setEnabled(true);
            }else{
                courseTypeDTO.setEnabled(false);
            }
            courseTypeDTOs.add(courseTypeDTO);
        });

        dto.setCourseTypes(courseTypeDTOs);




        return dto;
    }

    public ModuleFrameEntity toEntity(ModuleFrameDTO dto) {
        if (dto == null) {
            return null;
        }

        ModuleFrameEntity entity = new ModuleFrameEntity();
        entity.setId(dto.getId());
        entity.setQuantity(dto.getQuantity());
        entity.setName(dto.getName());
        entity.setSws(dto.getSws());
        entity.setCourseType(dto.getCourseType());
        entity.setWeight(dto.getWeight());
        entity.setCredits(dto.getCredits());
        entity.setAllExamsMandatory(dto.isAllExamsMandatory());

        entity.setSpo(spoRepository.findById(dto.getSpoId()).orElse(null));

        if (dto.getSection() != null) {
            entity.setSection(sectionRepository.findById(dto.getSection().getId()).orElse(null));
        }

        if (dto.getModuleType() != null) {
            entity.setModuleType(moduleTypeRepository.findById(dto.getModuleType().getId()).orElse(null));
        }

        return entity;
    }
}
