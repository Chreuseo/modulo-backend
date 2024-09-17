package de.modulo.backend.services;

import de.modulo.backend.converters.ModuleFrameModuleImplementationConverter;
import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.dtos.ModuleFrameModuleImplementationDTO;
import de.modulo.backend.entities.ExamTypeModuleImplementationEntity;
import de.modulo.backend.entities.ModuleFrameModuleImplementationEntity;
import de.modulo.backend.repositories.ExamTypeModuleImplementationRepository;
import de.modulo.backend.repositories.ExamTypeRepository;
import de.modulo.backend.repositories.ModuleFrameModuleImplementationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleFrameModuleImplementationService {

    private final ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;
    private final ModuleFrameModuleImplementationConverter moduleFrameModuleImplementationConverter;

    private final ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository;
    private final ExamTypeRepository examTypeRepository;
    @Autowired
    public ModuleFrameModuleImplementationService(ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository,
                                                  ModuleFrameModuleImplementationConverter moduleFrameModuleImplementationConverter,
                                                  ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository,
                                                  ExamTypeRepository examTypeRepository) {
        this.moduleFrameModuleImplementationRepository = moduleFrameModuleImplementationRepository;
        this.moduleFrameModuleImplementationConverter = moduleFrameModuleImplementationConverter;
        this.examTypeModuleImplementationRepository = examTypeModuleImplementationRepository;
        this.examTypeRepository = examTypeRepository;
    }

    public List<ModuleFrameModuleImplementationDTO> findAll() {
        List<ModuleFrameModuleImplementationEntity> entities = moduleFrameModuleImplementationRepository.findAll();
        return entities.stream()
                .map(moduleFrameModuleImplementationConverter::toDto)
                .toList();
    }

    public ModuleFrameModuleImplementationDTO findById(Long id) {
        return moduleFrameModuleImplementationRepository.findById(id)
                .map(moduleFrameModuleImplementationConverter::toDto)
                .orElse(null);
    }

    public List<ModuleFrameModuleImplementationDTO> findByModuleFrameId(Long moduleFrameId) {
        return moduleFrameModuleImplementationRepository.findModuleFrameModuleImplementationEntitiesByModuleFrameId(moduleFrameId).stream()
                .map(moduleFrameModuleImplementationConverter::toDto)
                .toList();
    }

    public List<ModuleFrameModuleImplementationDTO> findByModuleImplementationId(Long moduleImplementationId) {
        return moduleFrameModuleImplementationRepository.findModuleFrameModuleImplementationEntitiesByModuleImplementationId(moduleImplementationId).stream()
                .map(moduleFrameModuleImplementationConverter::toDto)
                .peek(moduleFrameModuleImplementationDTO -> moduleFrameModuleImplementationDTO.setExamTypeDTOs(examTypeModuleImplementationRepository.findExamTypeModuleImplementationEntitiesByModuleImplementationId(moduleImplementationId).stream()
                        .map(examTypeModuleImplementationEntity -> {
                            ExamTypeDTO examTypeDTO = new ExamTypeDTO();
                            examTypeDTO.setSpoId(examTypeModuleImplementationEntity.getExamType().getSpo().getId());
                            examTypeDTO.setId(examTypeModuleImplementationEntity.getExamType().getId());
                            examTypeDTO.setName(examTypeModuleImplementationEntity.getExamType().getName());
                            examTypeDTO.setAbbreviation(examTypeModuleImplementationEntity.getExamType().getAbbreviation());
                            examTypeDTO.setDescription(examTypeModuleImplementationEntity.getDescription());
                            examTypeDTO.setLength(examTypeModuleImplementationEntity.getLength());
                            examTypeDTO.setEnabled(true);
                            return examTypeDTO;
                        }).filter(examTypeDTO -> examTypeDTO.getSpoId() == moduleFrameModuleImplementationDTO.getModuleFrameDTO().getSpoDTOFlat().getId())
                        .toList()))
                .toList();
    }

    public ModuleFrameModuleImplementationDTO save(ModuleFrameModuleImplementationDTO dto) {
        ModuleFrameModuleImplementationEntity entity = moduleFrameModuleImplementationConverter.toEntity(dto);
        entity = moduleFrameModuleImplementationRepository.save(entity);

        if(dto.getExamTypeDTOs() != null){
            for(ExamTypeDTO examTypeDTO : dto.getExamTypeDTOs()) {
                ExamTypeModuleImplementationEntity.ExamTypeModuleImplementationId examTypeModuleImplementationId = new ExamTypeModuleImplementationEntity.ExamTypeModuleImplementationId();
                examTypeModuleImplementationId.setExamType(examTypeDTO.getId());
                examTypeModuleImplementationId.setModuleImplementation(entity.getModuleImplementation().getId());

                if (examTypeDTO.isEnabled()) {
                    ExamTypeModuleImplementationEntity examTypeModuleImplementationEntity = new ExamTypeModuleImplementationEntity();
                    examTypeModuleImplementationEntity.setId(examTypeModuleImplementationId);
                    examTypeModuleImplementationEntity.setExamType(examTypeRepository.findById(examTypeDTO.getId()).orElse(null));
                    examTypeModuleImplementationEntity.setModuleImplementation(entity.getModuleImplementation());
                    examTypeModuleImplementationEntity.setDescription(examTypeDTO.getDescription());
                    examTypeModuleImplementationEntity.setLength(examTypeDTO.getLength());
                    examTypeModuleImplementationEntity.setLength(examTypeDTO.getLength());
                    examTypeModuleImplementationRepository.save(examTypeModuleImplementationEntity);
                } else {
                    if (examTypeModuleImplementationRepository.existsById(examTypeModuleImplementationId)) {
                        examTypeModuleImplementationRepository.deleteById(examTypeModuleImplementationId);
                    }
                }
            }
        }

        ModuleFrameModuleImplementationDTO savedDto = moduleFrameModuleImplementationConverter.toDto(entity);
        savedDto.setExamTypeDTOs(dto.getExamTypeDTOs());
        return savedDto;
    }

    public void delete(Long id) {
        moduleFrameModuleImplementationRepository.deleteById(id);
    }
}
