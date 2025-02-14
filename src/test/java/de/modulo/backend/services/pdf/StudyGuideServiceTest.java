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
public class StudyGuideServiceTest {

    @InjectMocks
    private StudyGuideService studyGuideService;

    @Mock
    private SpoRepository spoRepository;

    @Mock
    private SemesterRepository semesterRepository;

    @Mock
    private ModuleFrameService moduleFrameService;

    @Mock
    private ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository;

    @Mock
    private CourseTypeModuleFrameRepository courseTypeModuleFrameRepository;

    @Mock
    private ExamTypeModuleImplementationRepository examTypeModuleImplementationRepository;

    private SpoEntity spoEntity;
    private SemesterEntity semesterEntity;

    @BeforeEach
    public void setUp() {
        // Create test entities
        spoEntity = new SpoEntity();
        spoEntity.setId(1L);
        spoEntity.setName("Test SPO");
        spoEntity.setDegree(new DegreeEntity());
        spoEntity.getDegree().setId(1L);
        spoEntity.getDegree().setName("Bachelor of Science");
        spoEntity.setValidFrom(new Date());
        spoEntity.setValidUntil(new Date(System.currentTimeMillis() + 100000000L)); // Valid for some time
        spoEntity.setStudyPlanAppendix("This is the appendix.");

        semesterEntity = new SemesterEntity();
        semesterEntity.setId(1L);
        semesterEntity.setName("Winter Semester");
        semesterEntity.setYear("2023");
    }

    @Test
    public void testGenerateStudyGuide() {
        // Mock the repository responses
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));
        when(semesterRepository.findById(1L)).thenReturn(Optional.of(semesterEntity));

        // Prepare mock ModuleFrameSetDTO
        ModuleFrameSetDTO moduleFrameSetDTO = new ModuleFrameSetDTO();
        ModuleFrameSetDTO.Section section = new ModuleFrameSetDTO.Section();
        section.setName("Section 1");
        List<ModuleFrameSetDTO.Section.ModuleType> moduleTypes = new ArrayList<>();

        ModuleFrameSetDTO.Section.ModuleType moduleType = new ModuleFrameSetDTO.Section.ModuleType();
        moduleType.setName("Module Type 1");
        List<ModuleFrameDTO> moduleFrames = new ArrayList<>();

        ModuleFrameDTO moduleFrameDTO = new ModuleFrameDTO();
        moduleFrameDTO.setId(1L);
        moduleFrameDTO.setSws(4);
        moduleFrameDTO.setCredits(5);
        moduleFrames.add(moduleFrameDTO);
        moduleType.setModuleFrames(moduleFrames);
        moduleTypes.add(moduleType);
        section.setModuleTypes(moduleTypes);
        moduleFrameSetDTO.setSections(List.of(section));

        when(moduleFrameService.getModuleFrameSetDTOBySpoId(1L)).thenReturn(moduleFrameSetDTO);
        when(moduleFrameModuleImplementationRepository.findModuleFrameModuleImplementationEntitiesByModuleFrameId(1L))
                .thenReturn(new ArrayList<>());

        // Call the method
        ByteArrayOutputStream outputStream = studyGuideService.generateStudyGuide(1L, 1L);

        // Validate result
        assertNotNull(outputStream);
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testGenerateStudyGuide_SpoNotFound() {
        // Mock the behavior when the SPO ID does not exist
        when(spoRepository.findById(1L)).thenReturn(Optional.empty());

        // Validate that the exception is thrown
        assertThrows(NoSuchElementException.class, () -> {
            studyGuideService.generateStudyGuide(1L, 1L);
        });
    }

    @Test
    public void testGenerateStudyGuide_SemesterNotFound() {
        // Mock the behavior for SPO
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));
        when(semesterRepository.findById(1L)).thenReturn(Optional.empty());

        // Validate that the exception is thrown
        assertThrows(NoSuchElementException.class, () -> {
            studyGuideService.generateStudyGuide(1L, 1L);
        });
    }

    @Test
    public void testGenerateStudyGuide_EmptyModuleTypes() {
        // Mock repositories
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));
        when(semesterRepository.findById(1L)).thenReturn(Optional.of(semesterEntity));

        // Prepare mock ModuleFrameSetDTO with empty module types
        ModuleFrameSetDTO moduleFrameSetDTO = new ModuleFrameSetDTO();
        ModuleFrameSetDTO.Section section = new ModuleFrameSetDTO.Section();
        section.setName("Section 1");
        section.setModuleTypes(new ArrayList<>()); // Empty
        moduleFrameSetDTO.setSections(List.of(section));
        when(moduleFrameService.getModuleFrameSetDTOBySpoId(1L)).thenReturn(moduleFrameSetDTO);

        // Call the method
        ByteArrayOutputStream outputStream = studyGuideService.generateStudyGuide(1L, 1L);

        // Validate the result
        assertNotNull(outputStream);
        assertTrue(outputStream.size() > 0);
    }
}
