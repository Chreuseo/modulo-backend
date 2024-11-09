package de.modulo.backend.services.data;

import de.modulo.backend.converters.SemesterConverter;
import de.modulo.backend.dtos.SemesterDTO;
import de.modulo.backend.entities.SemesterEntity;
import de.modulo.backend.repositories.SemesterRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemesterService {

    private final SemesterRepository semesterRepository;
    private final SemesterConverter semesterConverter;

    public SemesterService(SemesterRepository semesterRepository, SemesterConverter semesterConverter) {
        this.semesterRepository = semesterRepository;
        this.semesterConverter = semesterConverter;
    }

    public List<SemesterDTO> getAllSemesters() {
        return semesterRepository.findAll().stream()
                .sorted(Comparator.comparing(SemesterEntity::getYear))
                .map(semesterConverter::toDTO)
                .collect(Collectors.toList());
    }

    public SemesterDTO addSemester(SemesterDTO semesterDTO) {
        SemesterEntity entity = semesterConverter.toEntity(semesterDTO);
        SemesterEntity savedEntity = semesterRepository.save(entity);
        return semesterConverter.toDTO(savedEntity);
    }

    public SemesterDTO updateSemester(SemesterDTO semesterDTO) {
        SemesterEntity entity = semesterConverter.toEntity(semesterDTO);
        SemesterEntity updatedEntity = semesterRepository.save(entity);
        return semesterConverter.toDTO(updatedEntity);
    }

    public void removeSemester(long id) {
        semesterRepository.deleteById(id);
    }
}
