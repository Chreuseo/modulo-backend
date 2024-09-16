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

    private final ModuleFrameModuleImplementationRepository repository;
    private final ModuleFrameModuleImplementationConverter converter;

    private final ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository;
    private final ExamTypeRepository examTypeRepository;
    @Autowired
    public ModuleFrameModuleImplementationService(ModuleFrameModuleImplementationRepository repository,
                                                  ModuleFrameModuleImplementationConverter converter,
                                                  ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository,
                                                  ExamTypeRepository examTypeRepository) {
        this.repository = repository;
        this.converter = converter;
        this.examTypeModuleImplementationRepository = examTypeModuleImplementationRepository;
        this.examTypeRepository = examTypeRepository;
    }

    public List<ModuleFrameModuleImplementationDTO> findAll() {
        List<ModuleFrameModuleImplementationEntity> entities = repository.findAll();
        return entities.stream()
                .map(converter::toDto)
                .toList();
    }

    public ModuleFrameModuleImplementationDTO findById(Long id) {
        return repository.findById(id)
                .map(converter::toDto)
                .orElse(null);
    }

    public List<ModuleFrameModuleImplementationDTO> findByModuleFrameId(Long moduleFrameId) {
        return repository.findModuleFrameModuleImplementationEntitiesByModuleFrameId(moduleFrameId).stream()
                .map(converter::toDto)
                .toList();
    }

    public List<ModuleFrameModuleImplementationDTO> findByModuleImplementationId(Long moduleImplementationId) {
        return repository.findModuleFrameModuleImplementationEntitiesByModuleImplementationId(moduleImplementationId).stream()
                .map(converter::toDto)
                .peek(dto -> dto.setExamTypeDTOs(examTypeModuleImplementationRepository.findExamTypeModuleImplementationEntitiesByModuleImplementationId(moduleImplementationId).stream()
                        .map(examTypeModuleImplementationEntity -> {
                            ExamTypeDTO examTypeDTO = new ExamTypeDTO();
                            examTypeDTO.setId(examTypeModuleImplementationEntity.getExamType().getId());
                            examTypeDTO.setName(examTypeModuleImplementationEntity.getExamType().getName());
                            examTypeDTO.setAbbreviation(examTypeModuleImplementationEntity.getExamType().getAbbreviation());
                            examTypeDTO.setDescription(examTypeModuleImplementationEntity.getDescription());
                            examTypeDTO.setLength(examTypeModuleImplementationEntity.getLength());
                            examTypeDTO.setEnabled(true);
                            return examTypeDTO;
                        })
                        .toList()))
                .toList();
    }

    public ModuleFrameModuleImplementationDTO save(ModuleFrameModuleImplementationDTO dto) {
        ModuleFrameModuleImplementationEntity entity = converter.toEntity(dto);
        entity = repository.save(entity);

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

        ModuleFrameModuleImplementationDTO savedDto = converter.toDto(entity);
        savedDto.setExamTypeDTOs(dto.getExamTypeDTOs());
        return savedDto;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
