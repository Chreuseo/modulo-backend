package de.modulo.backend.services.data;

import de.modulo.backend.converters.ModuleFrameModuleImplementationConverter;
import de.modulo.backend.dtos.*;
import de.modulo.backend.entities.*;
import de.modulo.backend.repositories.ExamTypeModuleImplementationRepository;
import de.modulo.backend.repositories.ModuleFrameModuleImplementationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleFrameModuleImplementationServiceTest {

    @InjectMocks
    private ModuleFrameModuleImplementationService moduleFrameModuleImplementationService;

    @Mock
    private ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;

    @Mock
    private ModuleFrameModuleImplementationConverter moduleFrameModuleImplementationConverter;

    @Mock
    private ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository;


    @BeforeEach
    public void setUp() {
        // Any common setup code can go here
    }

    @Test
    public void findAll_ShouldReturnListOfDtos() {
        // Arrange
        List<ModuleFrameModuleImplementationEntity> entities = List.of(new ModuleFrameModuleImplementationEntity());
        List<ModuleFrameModuleImplementationDTO> dtos = List.of(new ModuleFrameModuleImplementationDTO());

        when(moduleFrameModuleImplementationRepository.findAll()).thenReturn(entities);
        when(moduleFrameModuleImplementationConverter.toDto(any())).thenReturn(new ModuleFrameModuleImplementationDTO());

        // Act
        List<ModuleFrameModuleImplementationDTO> result = moduleFrameModuleImplementationService.findAll();

        // Assert
        assertThat(result).isNotEmpty();
        verify(moduleFrameModuleImplementationRepository).findAll();
        verify(moduleFrameModuleImplementationConverter, times(entities.size())).toDto(any());
    }

    @Test
    public void findById_ShouldReturnDto_WhenEntityExists() {
        // Arrange
        Long id = 1L;
        ModuleFrameModuleImplementationEntity entity = new ModuleFrameModuleImplementationEntity();
        ModuleFrameModuleImplementationDTO dto = new ModuleFrameModuleImplementationDTO();

        when(moduleFrameModuleImplementationRepository.findById(id)).thenReturn(Optional.of(entity));
        when(moduleFrameModuleImplementationConverter.toDto(entity)).thenReturn(dto);

        // Act
        ModuleFrameModuleImplementationDTO result = moduleFrameModuleImplementationService.findById(id);

        // Assert
        assertThat(result).isEqualTo(dto);
        verify(moduleFrameModuleImplementationRepository).findById(id);
    }

    @Test
    public void findById_ShouldReturnNull_WhenEntityDoesNotExist() {
        // Arrange
        Long id = 1L;

        when(moduleFrameModuleImplementationRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ModuleFrameModuleImplementationDTO result = moduleFrameModuleImplementationService.findById(id);

        // Assert
        assertThat(result).isNull();
        verify(moduleFrameModuleImplementationRepository).findById(id);
    }

    @Test
    public void save_ShouldReturnSavedDto1() {
        // Arrange
        ModuleFrameModuleImplementationDTO dtoToSave = new ModuleFrameModuleImplementationDTO();
        ModuleFrameModuleImplementationEntity savedEntity = new ModuleFrameModuleImplementationEntity();
        ModuleFrameModuleImplementationDTO savedDto = new ModuleFrameModuleImplementationDTO();

        when(moduleFrameModuleImplementationConverter.toEntity(dtoToSave)).thenReturn(savedEntity);
        when(moduleFrameModuleImplementationRepository.save(savedEntity)).thenReturn(savedEntity);
        when(moduleFrameModuleImplementationConverter.toDto(savedEntity)).thenReturn(savedDto);

        // Act
        ModuleFrameModuleImplementationDTO result = moduleFrameModuleImplementationService.save(dtoToSave);

        // Assert
        assertThat(result).isEqualTo(savedDto);
        verify(moduleFrameModuleImplementationRepository).save(savedEntity);
    }

    @Test
    public void save_ShouldReturnSavedDto2() {
        // Arrange
        ModuleFrameModuleImplementationDTO dtoToSave = new ModuleFrameModuleImplementationDTO();
        List<ExamTypeDTO> examTypeDTOs = new ArrayList<>();
        ExamTypeDTO examTypeDTO = new ExamTypeDTO();
        examTypeDTO.setMandatory(true);
        examTypeDTO.setId(1L);
        examTypeDTO.setSpoId(1L);
        examTypeDTO.setName("ExamType");
        examTypeDTO.setAbbreviation("ET");
        examTypeDTO.setDescription("Description");
        examTypeDTO.setLength("90 minutes");
        examTypeDTO.setEnabled(true);
        examTypeDTOs.add(examTypeDTO);
        ModuleFrameModuleImplementationEntity savedEntity = new ModuleFrameModuleImplementationEntity();
        ModuleFrameModuleImplementationDTO savedDto = new ModuleFrameModuleImplementationDTO();

        when(moduleFrameModuleImplementationConverter.toEntity(dtoToSave)).thenReturn(savedEntity);
        when(moduleFrameModuleImplementationRepository.save(savedEntity)).thenReturn(savedEntity);
        when(moduleFrameModuleImplementationConverter.toDto(savedEntity)).thenReturn(savedDto);

        // Act
        ModuleFrameModuleImplementationDTO result = moduleFrameModuleImplementationService.save(dtoToSave);

        // Assert
        assertThat(result).isEqualTo(savedDto);
        verify(moduleFrameModuleImplementationRepository).save(savedEntity);
    }


    @Test
    public void delete_ShouldInvokeDeleteOnRepository() {
        // Arrange
        Long id = 1L;

        // Act
        moduleFrameModuleImplementationService.delete(id);

        // Assert
        verify(moduleFrameModuleImplementationRepository).deleteById(id);
    }

    @Test
    public void findByModuleImplementationId_ShouldReturnListOfDtos_WithExamTypeDTOs() {
        // Arrange
        Long moduleImplementationId = 1L;

        // Mocked entities
        ModuleFrameModuleImplementationEntity entity = new ModuleFrameModuleImplementationEntity();
        ModuleFrameModuleImplementationDTO dto = new ModuleFrameModuleImplementationDTO();

        // Mock List of ModuleFrameModuleImplementationEntity
        List<ModuleFrameModuleImplementationEntity> entities = List.of(entity);
        List<ModuleFrameModuleImplementationDTO> dtos = List.of(dto);

        // Mocked exam type implementation entities and DTOs
        ExamTypeModuleImplementationEntity examTypeEntity = new ExamTypeModuleImplementationEntity();
        examTypeEntity.setExamType(new ExamTypeEntity()); // Setup nested properties as needed
        examTypeEntity.getExamType().setSpo(new SpoEntity()); // Assume SpoDTOFlat class designed
        examTypeEntity.getExamType().getSpo().setId(1L); // Example value

        // Set up the expected ExamTypeDTO
        ExamTypeDTO examTypeDTO = new ExamTypeDTO();
        // Populate examTypeDTO properties, such as spoId, id, name, etc.
        examTypeDTO.setSpoId(1L); // Example value
        examTypeDTO.setId(1L); // Example value

        // Setting up relations for the entities
        entity.setModuleFrame(new ModuleFrameEntity()); // Set up relationships as needed
        entity.getModuleFrame().setSpo(new SpoEntity()); // Assume SpoDTOFlat class designed
        entity.getModuleFrame().getSpo().setId(1L); // Example value
        // Mock examTypeDTO within the ModuleFrameModuleImplementationDTO
        dto.setModuleFrameDTO(new ModuleFrameDTO());
        dto.getModuleFrameDTO().setSpoDTOFlat(new SpoDTOFlat());
        dto.getModuleFrameDTO().getSpoDTOFlat().setId(1L); // Example value

        when(moduleFrameModuleImplementationRepository.findModuleFrameModuleImplementationEntitiesByModuleImplementationId(moduleImplementationId))
                .thenReturn(entities);

        when(moduleFrameModuleImplementationConverter.toDto(entity)).thenReturn(dto);
        when(examTypeModuleImplementationRepository.findExamTypeModuleImplementationEntitiesByModuleImplementationId(moduleImplementationId))
                .thenReturn(List.of(examTypeEntity));

        // Act
        List<ModuleFrameModuleImplementationDTO> result = moduleFrameModuleImplementationService.findByModuleImplementationId(moduleImplementationId);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getExamTypeDTOs()).isNotEmpty(); // Check if ExamTypeDTOs are populated
        assertThat(result.get(0).getExamTypeDTOs().get(0).getSpoId()).isEqualTo(1L); // Validate ExamTypeDTO
        verify(moduleFrameModuleImplementationRepository).findModuleFrameModuleImplementationEntitiesByModuleImplementationId(moduleImplementationId);
        verify(moduleFrameModuleImplementationConverter).toDto(entity);
        verify(examTypeModuleImplementationRepository).findExamTypeModuleImplementationEntitiesByModuleImplementationId(moduleImplementationId);
    }

}
