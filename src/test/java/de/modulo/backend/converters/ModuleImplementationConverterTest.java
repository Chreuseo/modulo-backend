package de.modulo.backend.converters;

import de.modulo.backend.dtos.*;
import de.modulo.backend.entities.*;
import de.modulo.backend.repositories.ModuleFrameModuleImplementationRepository;
import de.modulo.backend.repositories.ModuleImplementationLecturerRepository;
import de.modulo.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleImplementationConverterTest {

    @Mock
    private UserConverter userConverter;

    @Mock
    private CycleConverter cycleConverter;

    @Mock
    private DurationConverter durationConverter;

    @Mock
    private LanguageConverter languageConverter;

    @Mock
    private MaternityProtectionConverter maternityProtectionConverter;

    @Mock
    private ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;

    @Mock
    private SpoConverter spoConverter;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;

    @InjectMocks
    private ModuleImplementationConverter moduleImplementationConverter;

    private ModuleImplementationEntity moduleImplementationEntity;
    private ModuleImplementationDTO moduleImplementationDTO;

    @BeforeEach
    void setUp() {
        // Set up a sample ModuleImplementationEntity for testing
        moduleImplementationEntity = new ModuleImplementationEntity();
        moduleImplementationEntity.setId(1L);
        moduleImplementationEntity.setName("Test Module");
        moduleImplementationEntity.setAbbreviation("TM");

        // Set up a sample ModuleImplementationDTO for testing
        moduleImplementationDTO = new ModuleImplementationDTO();
        moduleImplementationDTO.setId(1L);
        moduleImplementationDTO.setName("Test Module");
        moduleImplementationDTO.setAbbreviation("TM");
    }

    @Test
    void testToDto() {
        // Stubbing the userConverter for conversion results
        UserEntity userEntity = new UserEntity(); // You would have to set required fields
        UserDTOFlat userDTOFlat = new UserDTOFlat(); // Same here
        when(userConverter.toDtoFlat(any())).thenReturn(userDTOFlat);

        moduleImplementationEntity.setFirstExaminant(userEntity);
        moduleImplementationEntity.setSecondExaminant(userEntity);
        moduleImplementationEntity.setResponsible(userEntity);

        when(moduleImplementationLecturerRepository.getModuleImplementationLecturerEntitiesByModuleImplementationId(1L))
                .thenReturn(Collections.singletonList(new ModuleImplementationLecturerEntity())); // Mocking lecturers

        ModuleImplementationDTO dto = moduleImplementationConverter.toDto(moduleImplementationEntity);

        assertNotNull(dto);
        assertEquals(moduleImplementationEntity.getId(), dto.getId());
        assertEquals(moduleImplementationEntity.getName(), dto.getName());
        assertEquals(moduleImplementationEntity.getAbbreviation(), dto.getAbbreviation());

        // You can add more assertions to check all properties being mapped
    }

    @Test
    void testToDto_nullEntity(){
        assertNull(moduleImplementationConverter.toDto(null));
    }

    @Test
    void testToEntity_nullDTO(){
        assertNull(moduleImplementationConverter.toEntity((ModuleImplementationDTO) null));
    }

    @Test
    void testToEntity_nullDTOFlat(){
        assertNull(moduleImplementationConverter.toEntity((ModuleImplementationDTOFlat) null));
    }

    @Test
    void testToDtoFlat_nullEntity(){
        assertNull(moduleImplementationConverter.toDtoFlat(null));
    }

    @Test
    void testToEntity() {
        // Arrange
        UserEntity userEntity = new UserEntity(); // Prepare a mock user entity as return value
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity)); // Mocking user lookup

        moduleImplementationDTO.setFirstExaminant(new UserDTOFlat());
        moduleImplementationDTO.getFirstExaminant().setId(1L);

        moduleImplementationDTO.setSecondExaminant(new UserDTOFlat());
        moduleImplementationDTO.getSecondExaminant().setId(2L);

        moduleImplementationDTO.setResponsible(new UserDTOFlat());
        moduleImplementationDTO.getResponsible().setId(3L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userEntity));
        when(userRepository.findById(3L)).thenReturn(Optional.of(userEntity));

        // Act
        ModuleImplementationEntity entity = moduleImplementationConverter.toEntity(moduleImplementationDTO);

        // Assert
        assertNotNull(entity);
        assertEquals(moduleImplementationDTO.getId(), entity.getId());
        assertEquals(moduleImplementationDTO.getName(), entity.getName());
        assertEquals(moduleImplementationDTO.getAbbreviation(), entity.getAbbreviation());
        assertNotNull(entity.getFirstExaminant());
        assertNotNull(entity.getSecondExaminant());
        assertNotNull(entity.getResponsible());

        // You can add more property assertions here
    }

    @Test
    void testToEntity_fromDTOFlat() {
        // Act
        ModuleImplementationEntity entity = moduleImplementationConverter.toEntity(moduleImplementationDTO);

        // Assert
        assertNotNull(entity);
        assertEquals(moduleImplementationDTO.getId(), entity.getId());
        assertEquals(moduleImplementationDTO.getName(), entity.getName());
        assertEquals(moduleImplementationDTO.getAbbreviation(), entity.getAbbreviation());
        assertNull(entity.getFirstExaminant());
        assertNull(entity.getSecondExaminant());
        assertNull(entity.getResponsible());

        // You can add more property assertions here
    }

    @Test
    void testToEntity_nullValues() {
        // Act
        ModuleImplementationEntity entity = moduleImplementationConverter.toEntity(moduleImplementationDTO);

        // Assert
        assertNotNull(entity);
        assertEquals(moduleImplementationDTO.getId(), entity.getId());
        assertEquals(moduleImplementationDTO.getName(), entity.getName());
        assertEquals(moduleImplementationDTO.getAbbreviation(), entity.getAbbreviation());
        assertNull(entity.getFirstExaminant());
        assertNull(entity.getSecondExaminant());
        assertNull(entity.getResponsible());

        // You can add more property assertions here
    }

    @Test
    void testToDtoFlat() {
        ModuleImplementationDTOFlat flatDto = moduleImplementationConverter.toDtoFlat(moduleImplementationEntity);

        assertNotNull(flatDto);
        assertEquals(moduleImplementationEntity.getId(), flatDto.getId());
        assertEquals(moduleImplementationEntity.getName(), flatDto.getName());
        assertEquals(moduleImplementationEntity.getAbbreviation(), flatDto.getAbbreviation());

        // You can add more assertions to check properties, especially the spos list
    }
}
