package de.modulo.backend.services.pdf;

import de.modulo.backend.dtos.ModuleFrameDTO;
import de.modulo.backend.dtos.ModuleFrameSetDTO;
import de.modulo.backend.entities.*;
import de.modulo.backend.repositories.*;
import de.modulo.backend.services.data.ModuleFrameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleManualServiceTest {

    @InjectMocks
    private ModuleManualService moduleManualService;

    @Mock
    private ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;

    @Mock
    private ModuleFrameService moduleFrameService;

    @Mock
    private SpoRepository spoRepository;

    @Mock
    private ModuleImplementationLecturerRepository moduleImplementationLecturerRepository;

    @Mock
    private ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository;

    @Mock
    private SemesterRepository semesterRepository;

    private SpoEntity spoEntity;

    @BeforeEach
    public void setUp() {
        // Create test SPO entity
        spoEntity = new SpoEntity();
        spoEntity.setId(1L);
        spoEntity.setName("Test SPO");
        spoEntity.setDegree(new DegreeEntity());
        spoEntity.getDegree().setId(1L);
        spoEntity.getDegree().setName("Test Degree");
        spoEntity.setModuleManualIntroduction("<h1>Module Manual Introduction</h1>");
        spoEntity.setPublication(new Date());
        spoEntity.setValidFrom(new Date());
        spoEntity.setValidUntil(new Date(System.currentTimeMillis() + 100000000L)); // Valid for some time
        spoEntity.setStudyPlanAppendix("<p>Study Plan Appendix</p>");
    }

    @Test
    public void testGenerateModuleManual() {
        // Mock repository responses
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));
        ModuleFrameModuleImplementationEntity moduleFrameModuleImplementationEntity = new ModuleFrameModuleImplementationEntity();
        moduleFrameModuleImplementationEntity.setId(1L);
        moduleFrameModuleImplementationEntity.setModuleFrame(new ModuleFrameEntity());
        moduleFrameModuleImplementationEntity.getModuleFrame().setId(1L);
        moduleFrameModuleImplementationEntity.getModuleFrame().setName("Module Frame 1");
        moduleFrameModuleImplementationEntity.setModuleImplementation(new ModuleImplementationEntity());
        moduleFrameModuleImplementationEntity.getModuleImplementation().setId(1L);
        moduleFrameModuleImplementationEntity.getModuleImplementation().setName("Module Implementation 1");
        when(moduleFrameModuleImplementationRepository.findModuleFrameModuleImplementationEntitiesByModuleFrameId(1L))
                .thenReturn(List.of(moduleFrameModuleImplementationEntity));

        SemesterEntity semesterEntity = new SemesterEntity();
        semesterEntity.setId(1L);
        semesterEntity.setName("Test Semester");
        semesterEntity.setAbbreviation("TS");
        semesterEntity.setYear("2021");


        when(semesterRepository.findById(any())).thenReturn(Optional.of(semesterEntity));

        // Prepare a mocked ModuleFrameSetDTO
        ModuleFrameSetDTO moduleFrameSetDTO = new ModuleFrameSetDTO();
        ModuleFrameSetDTO.Section section = new ModuleFrameSetDTO.Section();
        section.setName("Section 1");
        List<ModuleFrameSetDTO.Section.ModuleType> moduleTypes = new ArrayList<>();

        ModuleFrameSetDTO.Section.ModuleType moduleType = new ModuleFrameSetDTO.Section.ModuleType();
        moduleType.setName("Module Type 1");
        List<ModuleFrameDTO> moduleFrames = new ArrayList<>();

        ModuleFrameDTO moduleFrameDTO = new ModuleFrameDTO();
        moduleFrameDTO.setId(1L);
        moduleFrameDTO.setName("Module Frame 1");
        moduleFrames.add(moduleFrameDTO);
        moduleType.setModuleFrames(moduleFrames);
        moduleTypes.add(moduleType);
        section.setModuleTypes(moduleTypes);
        moduleFrameSetDTO.setSections(List.of(section));

        when(moduleFrameService.getModuleFrameSetDTOBySpoId(1L)).thenReturn(moduleFrameSetDTO);
        when(moduleFrameModuleImplementationRepository.findModuleFrameModuleImplementationEntitiesByModuleFrameId(1L))
                .thenReturn(new ArrayList<>());

        // Call the method
        ByteArrayOutputStream outputStream = moduleManualService.generateModuleManual(1L, 1L);

        // Validate result
        assertNotNull(outputStream);
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testGenerateModuleManual_SpoNotFound() {
        // Mock the behavior when the SPO ID does not exist
        when(spoRepository.findById(1L)).thenReturn(Optional.empty());

        // Validate that the exception is thrown
        assertThrows(NoSuchElementException.class, () -> {
            moduleManualService.generateModuleManual(1L, 1L);
        });
    }

    @Test
    public void testGenerateModuleManual_NoSections() {
        // Mock repository responses
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));

        SemesterEntity semesterEntity = new SemesterEntity();
        semesterEntity.setId(1L);
        semesterEntity.setName("Test Semester");
        semesterEntity.setAbbreviation("TS");
        semesterEntity.setYear("2021");


        when(semesterRepository.findById(any())).thenReturn(Optional.of(semesterEntity));

        // Prepare a mocked ModuleFrameSetDTO with no sections
        ModuleFrameSetDTO moduleFrameSetDTO = new ModuleFrameSetDTO();
        moduleFrameSetDTO.setSections(new ArrayList<>()); // No sections
        when(moduleFrameService.getModuleFrameSetDTOBySpoId(1L)).thenReturn(moduleFrameSetDTO);

        // Call the method
        ByteArrayOutputStream outputStream = moduleManualService.generateModuleManual(1L, 1L);

        // Validate result
        assertNotNull(outputStream);
        assertTrue(outputStream.size() > 0);
    }


}
