package de.modulo.backend.services.data;

import de.modulo.backend.converters.ExamTypeConverter;
import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.entities.ExamTypeEntity;
import de.modulo.backend.entities.ExamTypeModuleFrameEntity;
import de.modulo.backend.repositories.ExamTypeModuleFrameRepository;
import de.modulo.backend.repositories.ExamTypeRepository; // Assuming you have this repository created
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamTypeService {

    @Autowired
    private ExamTypeRepository examTypeRepository;

    @Autowired
    private ExamTypeModuleFrameRepository examTypeModuleFrameRepository;

    @Autowired
    private ExamTypeConverter examTypeConverter;

    public List<ExamTypeDTO> getBySpo(long spoId) {
        List<ExamTypeEntity> examTypes = examTypeRepository.findBySpoId(spoId);
        return examTypes.stream()
                .map(examTypeConverter::toDto)
                .collect(Collectors.toList());
    }

    public List<ExamTypeDTO> getByModuleFrame(long moduleFrameId) {
        List<ExamTypeModuleFrameEntity> examTypeModuleFrameEntities = examTypeModuleFrameRepository.getExamTypeModuleFrameEntitiesByModuleFrameId(moduleFrameId);
        List<ExamTypeDTO> examTypeDTOs = new ArrayList<>();

        for (ExamTypeModuleFrameEntity examTypeModuleFrameEntity : examTypeModuleFrameEntities) {
            ExamTypeDTO examTypeDTO = examTypeConverter.toDto(examTypeModuleFrameEntity.getExamType());
            examTypeDTO.setMandatory(examTypeModuleFrameEntity.isMandatory());
            examTypeDTO.setEnabled(examTypeModuleFrameEntity.isMandatory());
            examTypeDTOs.add(examTypeDTO);
        }

        return examTypeDTOs;
    }

    public ExamTypeDTO add(ExamTypeDTO dto) {
        ExamTypeEntity entity = examTypeConverter.toEntity(dto);
        ExamTypeEntity savedEntity = examTypeRepository.save(entity);
        return examTypeConverter.toDto(savedEntity);
    }

    public void remove(long id) {
        examTypeRepository.deleteById(id);
    }

    public ExamTypeDTO update(ExamTypeDTO dto) {
        ExamTypeEntity entity = examTypeConverter.toEntity(dto);
        ExamTypeEntity updatedEntity = examTypeRepository.save(entity);
        return examTypeConverter.toDto(updatedEntity);
    }
}
