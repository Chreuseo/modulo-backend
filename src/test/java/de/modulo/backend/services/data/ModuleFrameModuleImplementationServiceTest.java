package de.modulo.backend.services.data;

import de.modulo.backend.converters.ModuleFrameModuleImplementationConverter;
import de.modulo.backend.dtos.ExamTypeDTO;
import de.modulo.backend.dtos.ModuleFrameModuleImplementationDTO;
import de.modulo.backend.entities.ModuleFrameModuleImplementationEntity;
import de.modulo.backend.repositories.ExamTypeModuleImplementationRepository;
import de.modulo.backend.repositories.ModuleFrameModuleImplementationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleFrameModuleImplementationServiceTest {

    @InjectMocks
    private ModuleFrameModuleImplementationService service;

    @Mock
    private ModuleFrameModuleImplementationRepository repository;

    @Mock
    private ModuleFrameModuleImplementationConverter converter;

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

        when(repository.findAll()).thenReturn(entities);
        when(converter.toDto(any())).thenReturn(new ModuleFrameModuleImplementationDTO());

        // Act
        List<ModuleFrameModuleImplementationDTO> result = service.findAll();

        // Assert
        assertThat(result).isNotEmpty();
        verify(repository).findAll();
        verify(converter, times(entities.size())).toDto(any());
    }

    @Test
    public void findById_ShouldReturnDto_WhenEntityExists() {
        // Arrange
        Long id = 1L;
        ModuleFrameModuleImplementationEntity entity = new ModuleFrameModuleImplementationEntity();
        ModuleFrameModuleImplementationDTO dto = new ModuleFrameModuleImplementationDTO();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(converter.toDto(entity)).thenReturn(dto);

        // Act
        ModuleFrameModuleImplementationDTO result = service.findById(id);

        // Assert
        assertThat(result).isEqualTo(dto);
        verify(repository).findById(id);
    }

    @Test
    public void findById_ShouldReturnNull_WhenEntityDoesNotExist() {
        // Arrange
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act
        ModuleFrameModuleImplementationDTO result = service.findById(id);

        // Assert
        assertThat(result).isNull();
        verify(repository).findById(id);
    }

    @Test
    public void save_ShouldReturnSavedDto() {
        // Arrange
        ModuleFrameModuleImplementationDTO dtoToSave = new ModuleFrameModuleImplementationDTO();
        ModuleFrameModuleImplementationEntity savedEntity = new ModuleFrameModuleImplementationEntity();
        ModuleFrameModuleImplementationDTO savedDto = new ModuleFrameModuleImplementationDTO();

        when(converter.toEntity(dtoToSave)).thenReturn(savedEntity);
        when(repository.save(savedEntity)).thenReturn(savedEntity);
        when(converter.toDto(savedEntity)).thenReturn(savedDto);

        // Act
        ModuleFrameModuleImplementationDTO result = service.save(dtoToSave);

        // Assert
        assertThat(result).isEqualTo(savedDto);
        verify(repository).save(savedEntity);
    }

    @Test
    public void delete_ShouldInvokeDeleteOnRepository() {
        // Arrange
        Long id = 1L;

        // Act
        service.delete(id);

        // Assert
        verify(repository).deleteById(id);
    }
}
