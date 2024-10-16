package de.modulo.backend.converters;

import de.modulo.backend.dtos.CourseTypeDTO;
import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.entities.*;
import de.modulo.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    private final ExamTypeRepository examTypeRepository;
    private final CourseTypeModuleFrameRepository courseTypeModuleFrameRepository;
    private final ExamTypeModuleFrameRepository examTypeModuleFrameRepository;
    private final ExamTypeConverter examTypeConverter;
    private final SpoConverter spoConverter;

    @Autowired
    public ModuleFrameConverter(SectionConverter sectionConverter,
                                ModuleTypeConverter moduleTypeConverter,
                                SpoRepository spoRepository,
                                SectionRepository sectionRepository,
                                ModuleTypeRepository moduleTypeRepository,
                                CourseTypeRepository courseTypeRepository,
                                CourseTypeConverter courseTypeConverter,
                                ExamTypeRepository examTypeRepository,
                                CourseTypeModuleFrameRepository courseTypeModuleFrameRepository,
                                ExamTypeModuleFrameRepository examTypeModuleFrameRepository, ExamTypeConverter examTypeConverter, SpoConverter spoConverter) {
        this.sectionConverter = sectionConverter;
        this.moduleTypeConverter = moduleTypeConverter;
        this.courseTypeConverter = courseTypeConverter;

        this.spoRepository = spoRepository;
        this.sectionRepository = sectionRepository;
        this.moduleTypeRepository = moduleTypeRepository;
        this.courseTypeRepository = courseTypeRepository;
        this.examTypeRepository = examTypeRepository;
        this.courseTypeModuleFrameRepository = courseTypeModuleFrameRepository;
        this.examTypeModuleFrameRepository = examTypeModuleFrameRepository;
        this.examTypeConverter = examTypeConverter;
        this.spoConverter = spoConverter;
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
        dto.setWeight(entity.getWeight());
        dto.setCredits(entity.getCredits());

        dto.setSpoDTOFlat(spoConverter.toDtoFlat(entity.getSpo()));

        if (entity.getSection() != null) {
            dto.setSection(sectionConverter.toDto(entity.getSection()));
        }

        if (entity.getModuleType() != null) {
            dto.setModuleType(moduleTypeConverter.toDto(entity.getModuleType()));
        }

        List<CourseTypeDTO> courseTypeDTOs = new ArrayList<>();
        List<CourseTypeEntity> usedCourseTypes = courseTypeModuleFrameRepository
                .findCourseTypeModuleFrameEntitiesByModuleFrameId(entity.getId()).stream()
                .map(CourseTypeModuleFrameEntity::getCourseType)
                .toList();

        courseTypeRepository.findAll().forEach(courseTypeEntity -> {
            CourseTypeDTO courseTypeDTO = courseTypeConverter.toDto(courseTypeEntity);
            courseTypeDTO.setEnabled(usedCourseTypes.contains(courseTypeEntity));
            courseTypeDTOs.add(courseTypeDTO);
        });

        dto.setCourseTypes(courseTypeDTOs);

        List<ExamTypeDTO> examTypeDTOs = new ArrayList<>();
        List<ExamTypeModuleFrameEntity> usedExamTypes = examTypeModuleFrameRepository
                .getExamTypeModuleFrameEntitiesByModuleFrameId(entity.getId()).stream()
                .toList();

        Map<Long, ExamTypeModuleFrameEntity> usedExamTypeMap = usedExamTypes.stream()
                .collect(Collectors.toMap(examTypeModuleFrameEntity -> examTypeModuleFrameEntity.getExamType().getId(), Function.identity()));

        examTypeRepository.findBySpoId(entity.getSpo().getId()).forEach(examTypeEntity -> {
            ExamTypeDTO examTypeDTO = examTypeConverter.toDto(examTypeEntity);
            examTypeDTO.setEnabled(usedExamTypeMap.containsKey(examTypeEntity.getId()));
            examTypeDTO.setMandatory(usedExamTypeMap.containsKey(examTypeEntity.getId()) && usedExamTypeMap.get(examTypeEntity.getId()).isMandatory());
            examTypeDTOs.add(examTypeDTO);
        });

        dto.setExamTypes(examTypeDTOs);


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
        entity.setWeight(dto.getWeight());
        entity.setCredits(dto.getCredits());

        entity.setSpo(spoRepository.findById(dto.getSpoDTOFlat().getId()).orElse(null));

        if (dto.getSection() != null) {
            entity.setSection(sectionRepository.findById(dto.getSection().getId()).orElse(null));
        }

        if (dto.getModuleType() != null) {
            entity.setModuleType(moduleTypeRepository.findById(dto.getModuleType().getId()).orElse(null));
        }

        return entity;
    }
}
