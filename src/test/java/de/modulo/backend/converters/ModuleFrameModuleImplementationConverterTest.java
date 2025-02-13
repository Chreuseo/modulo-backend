package de.modulo.backend.converters;

import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.dtos.ModuleFrameModuleImplementationDTO;
import de.modulo.backend.dtos.ModuleImplementationDTOFlat;
import de.modulo.backend.dtos.ModuleRequirementDTO;
import de.modulo.backend.entities.ModuleFrameEntity;
import de.modulo.backend.entities.ModuleFrameModuleImplementationEntity;
import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.entities.ModuleRequirementEntity;
import de.modulo.backend.repositories.ModuleFrameRepository;
import de.modulo.backend.repositories.ModuleImplementationRepository;
import de.modulo.backend.repositories.ModuleRequirementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ModuleFrameModuleImplementationConverterTest {

    @Mock
    ModuleFrameRepository moduleFrameRepository;

    @Mock
    ModuleImplementationRepository moduleImplementationRepository;

    @Mock
    ModuleRequirementRepository moduleRequirementRepository;

    @Mock
    ModuleImplementationConverter moduleImplementationConverter;

    @Mock
    ModuleRequirementConverter moduleRequirementConverter;

    @Mock
    ModuleFrameConverter moduleFrameConverter;

    @InjectMocks
    private ModuleFrameModuleImplementationConverter moduleFrameModuleImplementationConverter;

    @Test
    void testToDto() {
        // Arrange
        ModuleFrameModuleImplementationEntity moduleFrameModuleImplementationEntity = new ModuleFrameModuleImplementationEntity();
        moduleFrameModuleImplementationEntity.setId(1L);

        ModuleFrameEntity moduleFrameEntity = new ModuleFrameEntity();
        moduleFrameEntity.setId(1L);
        moduleFrameEntity.setName("Module Frame");
        moduleFrameModuleImplementationEntity.setModuleFrame(moduleFrameEntity);

        ModuleImplementationEntity moduleImplementationEntity = new ModuleImplementationEntity();
        moduleImplementationEntity.setId(1L);
        moduleImplementationEntity.setName("Module Implementation");
        moduleFrameModuleImplementationEntity.setModuleImplementation(moduleImplementationEntity);

        ModuleRequirementEntity moduleRequirementEntity = new ModuleRequirementEntity();
        moduleRequirementEntity.setId(1L);
        moduleRequirementEntity.setName("Module Requirement");
        moduleFrameModuleImplementationEntity.setModuleRequirement(moduleRequirementEntity);

        // Arrange mocks
        ModuleFrameDTO mockModuleFrameDTO = new ModuleFrameDTO();
        mockModuleFrameDTO.setId(1L);
        mockModuleFrameDTO.setName("Module Frame");
        when(moduleFrameConverter.toDto(moduleFrameEntity)).thenReturn(mockModuleFrameDTO);

        ModuleImplementationDTOFlat mockModuleImplementationDTOFlat = new ModuleImplementationDTOFlat();
        mockModuleImplementationDTOFlat.setId(1L);
        mockModuleImplementationDTOFlat.setName("Module Implementation");
        when(moduleImplementationConverter.toDtoFlat(moduleImplementationEntity)).thenReturn(mockModuleImplementationDTOFlat);

        ModuleRequirementDTO mockModuleRequirementDTO = new ModuleRequirementDTO();
        mockModuleRequirementDTO.setId(1L);
        mockModuleRequirementDTO.setName("Module Requirement");
        when(moduleRequirementConverter.toDto(moduleRequirementEntity)).thenReturn(mockModuleRequirementDTO);

        // Act
        ModuleFrameModuleImplementationDTO moduleFrameModuleImplementationDto = moduleFrameModuleImplementationConverter.toDto(moduleFrameModuleImplementationEntity);

        // Assert
        assertNotNull(moduleFrameModuleImplementationDto);
        assertEquals(moduleFrameModuleImplementationEntity.getId(), moduleFrameModuleImplementationDto.getId());
        assertEquals(moduleFrameModuleImplementationEntity.getModuleFrame().getId(), moduleFrameModuleImplementationDto.getModuleFrameDTO().getId());
        assertEquals(moduleFrameModuleImplementationEntity.getModuleFrame().getName(), moduleFrameModuleImplementationDto.getModuleFrameDTO().getName());
        assertEquals(moduleFrameModuleImplementationEntity.getModuleImplementation().getId(), moduleFrameModuleImplementationDto.getModuleImplementationDTOFlat().getId());
        assertEquals(moduleFrameModuleImplementationEntity.getModuleImplementation().getName(), moduleFrameModuleImplementationDto.getModuleImplementationDTOFlat().getName());
        assertEquals(moduleFrameModuleImplementationEntity.getModuleRequirement().getId(), moduleFrameModuleImplementationDto.getModuleRequirementDTO().getId());
        assertEquals(moduleFrameModuleImplementationEntity.getModuleRequirement().getName(), moduleFrameModuleImplementationDto.getModuleRequirementDTO().getName());
    }

    @Test
    void testToDto_nullModuleRequirement(){
        // Arrange
        ModuleFrameModuleImplementationEntity moduleFrameModuleImplementationEntity = new ModuleFrameModuleImplementationEntity();
        moduleFrameModuleImplementationEntity.setId(1L);

        ModuleFrameEntity moduleFrameEntity = new ModuleFrameEntity();
        moduleFrameEntity.setId(1L);
        moduleFrameEntity.setName("Module Frame");
        moduleFrameModuleImplementationEntity.setModuleFrame(moduleFrameEntity);

        ModuleImplementationEntity moduleImplementationEntity = new ModuleImplementationEntity();
        moduleImplementationEntity.setId(1L);
        moduleImplementationEntity.setName("Module Implementation");
        moduleFrameModuleImplementationEntity.setModuleImplementation(moduleImplementationEntity);

        // Arrange mocks
        ModuleFrameDTO mockModuleFrameDTO = new ModuleFrameDTO();
        mockModuleFrameDTO.setId(1L);
        mockModuleFrameDTO.setName("Module Frame");
        when(moduleFrameConverter.toDto(moduleFrameEntity)).thenReturn(mockModuleFrameDTO);

        ModuleImplementationDTOFlat mockModuleImplementationDTOFlat = new ModuleImplementationDTOFlat();
        mockModuleImplementationDTOFlat.setId(1L);
        mockModuleImplementationDTOFlat.setName("Module Implementation");
        when(moduleImplementationConverter.toDtoFlat(moduleImplementationEntity)).thenReturn(mockModuleImplementationDTOFlat);

        ModuleRequirementDTO mockModuleRequirementDTO = new ModuleRequirementDTO();

        // Act
        ModuleFrameModuleImplementationDTO moduleFrameModuleImplementationDto = moduleFrameModuleImplementationConverter.toDto(moduleFrameModuleImplementationEntity);

        // Assert
        assertNotNull(moduleFrameModuleImplementationDto);
        assertEquals(moduleFrameModuleImplementationEntity.getId(), moduleFrameModuleImplementationDto.getId());
        assertEquals(moduleFrameModuleImplementationEntity.getModuleFrame().getId(), moduleFrameModuleImplementationDto.getModuleFrameDTO().getId());
        assertEquals(moduleFrameModuleImplementationEntity.getModuleFrame().getName(), moduleFrameModuleImplementationDto.getModuleFrameDTO().getName());
        assertEquals(moduleFrameModuleImplementationEntity.getModuleImplementation().getId(), moduleFrameModuleImplementationDto.getModuleImplementationDTOFlat().getId());
        assertEquals(moduleFrameModuleImplementationEntity.getModuleImplementation().getName(), moduleFrameModuleImplementationDto.getModuleImplementationDTOFlat().getName());
        assertNull(moduleFrameModuleImplementationDto.getModuleRequirementDTO());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        ModuleFrameModuleImplementationDTO moduleFrameModuleImplementationDto = moduleFrameModuleImplementationConverter.toDto(null);

        // Assert
        assertNull(moduleFrameModuleImplementationDto);
    }

    @Test
    void testToEntity(){
        // Arrange
        ModuleFrameModuleImplementationDTO moduleFrameModuleImplementationDTO = new ModuleFrameModuleImplementationDTO();
        moduleFrameModuleImplementationDTO.setId(1L);
        moduleFrameModuleImplementationDTO.setSemester("1");

        ModuleFrameDTO moduleFrameDTO = new ModuleFrameDTO();
        moduleFrameDTO.setId(1L);
        moduleFrameDTO.setName("Module Frame");
        moduleFrameModuleImplementationDTO.setModuleFrameDTO(moduleFrameDTO);

        ModuleImplementationDTOFlat moduleImplementationDTOFlat = new ModuleImplementationDTOFlat();
        moduleImplementationDTOFlat.setId(1L);
        moduleImplementationDTOFlat.setName("Module Implementation");
        moduleFrameModuleImplementationDTO.setModuleImplementationDTOFlat(moduleImplementationDTOFlat);

        ModuleRequirementDTO moduleRequirementDTO = new ModuleRequirementDTO();
        moduleRequirementDTO.setId(1L);
        moduleRequirementDTO.setName("Module Requirement");
        moduleFrameModuleImplementationDTO.setModuleRequirementDTO(moduleRequirementDTO);

        // Arrange Mocks
        ModuleFrameEntity mockModuleFrameEntity = new ModuleFrameEntity();
        mockModuleFrameEntity.setId(1L);
        mockModuleFrameEntity.setName("Module Frame");

        ModuleImplementationEntity mockModuleImplementationEntity = new ModuleImplementationEntity();
        mockModuleImplementationEntity.setId(1L);
        mockModuleImplementationEntity.setName("Module Implementation");

        ModuleRequirementEntity mockModuleRequirementEntity = new ModuleRequirementEntity();
        mockModuleRequirementEntity.setId(1L);
        mockModuleRequirementEntity.setName("Module Requirement");

        when(moduleFrameRepository.findById(1L)).thenReturn(java.util.Optional.of(mockModuleFrameEntity));
        when(moduleImplementationRepository.findById(1L)).thenReturn(java.util.Optional.of(mockModuleImplementationEntity));
        when(moduleRequirementRepository.findById(1L)).thenReturn(java.util.Optional.of(mockModuleRequirementEntity));

        // Act
        ModuleFrameModuleImplementationEntity moduleFrameModuleImplementationEntity = moduleFrameModuleImplementationConverter.toEntity(moduleFrameModuleImplementationDTO);

        // Assert
        assertNotNull(moduleFrameModuleImplementationEntity);
        assertEquals(moduleFrameModuleImplementationDTO.getId(), moduleFrameModuleImplementationEntity.getId());
        assertEquals(moduleFrameModuleImplementationDTO.getSemester(), moduleFrameModuleImplementationEntity.getSemester());
        assertEquals(moduleFrameModuleImplementationDTO.getModuleFrameDTO().getId(), moduleFrameModuleImplementationEntity.getModuleFrame().getId());
        assertEquals(moduleFrameModuleImplementationDTO.getModuleFrameDTO().getName(), moduleFrameModuleImplementationEntity.getModuleFrame().getName());
        assertEquals(moduleFrameModuleImplementationDTO.getModuleImplementationDTOFlat().getId(), moduleFrameModuleImplementationEntity.getModuleImplementation().getId());
        assertEquals(moduleFrameModuleImplementationDTO.getModuleImplementationDTOFlat().getName(), moduleFrameModuleImplementationEntity.getModuleImplementation().getName());
        assertEquals(moduleFrameModuleImplementationDTO.getModuleRequirementDTO().getId(), moduleFrameModuleImplementationEntity.getModuleRequirement().getId());
        assertEquals(moduleFrameModuleImplementationDTO.getModuleRequirementDTO().getName(), moduleFrameModuleImplementationEntity.getModuleRequirement().getName());
    }

    @Test
    void testToEntity_nullModuleRequirement(){
        // Arrange
        ModuleFrameModuleImplementationDTO moduleFrameModuleImplementationDTO = new ModuleFrameModuleImplementationDTO();
        moduleFrameModuleImplementationDTO.setId(1L);
        moduleFrameModuleImplementationDTO.setSemester("1");

        ModuleFrameDTO moduleFrameDTO = new ModuleFrameDTO();
        moduleFrameDTO.setId(1L);
        moduleFrameDTO.setName("Module Frame");
        moduleFrameModuleImplementationDTO.setModuleFrameDTO(moduleFrameDTO);

        ModuleImplementationDTOFlat moduleImplementationDTOFlat = new ModuleImplementationDTOFlat();
        moduleImplementationDTOFlat.setId(1L);
        moduleImplementationDTOFlat.setName("Module Implementation");
        moduleFrameModuleImplementationDTO.setModuleImplementationDTOFlat(moduleImplementationDTOFlat);

        // Arrange Mocks
        ModuleFrameEntity mockModuleFrameEntity = new ModuleFrameEntity();
        mockModuleFrameEntity.setId(1L);
        mockModuleFrameEntity.setName("Module Frame");

        ModuleImplementationEntity mockModuleImplementationEntity = new ModuleImplementationEntity();
        mockModuleImplementationEntity.setId(1L);
        mockModuleImplementationEntity.setName("Module Implementation");

        when(moduleFrameRepository.findById(1L)).thenReturn(java.util.Optional.of(mockModuleFrameEntity));
        when(moduleImplementationRepository.findById(1L)).thenReturn(java.util.Optional.of(mockModuleImplementationEntity));

        // Act
        ModuleFrameModuleImplementationEntity moduleFrameModuleImplementationEntity = moduleFrameModuleImplementationConverter.toEntity(moduleFrameModuleImplementationDTO);

        // Assert
        assertNotNull(moduleFrameModuleImplementationEntity);
        assertEquals(moduleFrameModuleImplementationDTO.getId(), moduleFrameModuleImplementationEntity.getId());
        assertEquals(moduleFrameModuleImplementationDTO.getSemester(), moduleFrameModuleImplementationEntity.getSemester());
        assertEquals(moduleFrameModuleImplementationDTO.getModuleFrameDTO().getId(), moduleFrameModuleImplementationEntity.getModuleFrame().getId());
        assertEquals(moduleFrameModuleImplementationDTO.getModuleFrameDTO().getName(), moduleFrameModuleImplementationEntity.getModuleFrame().getName());
        assertEquals(moduleFrameModuleImplementationDTO.getModuleImplementationDTOFlat().getId(), moduleFrameModuleImplementationEntity.getModuleImplementation().getId());
        assertEquals(moduleFrameModuleImplementationDTO.getModuleImplementationDTOFlat().getName(), moduleFrameModuleImplementationEntity.getModuleImplementation().getName());
        assertNull(moduleFrameModuleImplementationEntity.getModuleRequirement());
    }
}


