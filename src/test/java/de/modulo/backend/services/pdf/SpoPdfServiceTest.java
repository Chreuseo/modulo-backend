package de.modulo.backend.services.pdf;

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
public class SpoPdfServiceTest {

    @InjectMocks
    private SpoPdfService spoPdfService;

    @Mock
    private SpoRepository spoRepository;

    @Mock
    private ParagraphRepository paragraphRepository;

    @Mock
    private ModuleFrameService moduleFrameService;

    @Mock
    private CourseTypeModuleFrameRepository courseTypeModuleFrameRepository;

    @Mock
    private ExamTypeRepository examTypeRepository;

    @Mock
    private ExamTypeModuleFrameRepository examTypeModuleFrameRepository;

    private SpoEntity spoEntity;

    @BeforeEach
    public void setUp() {
        // Create test entity
        spoEntity = new SpoEntity();
        spoEntity.setId(1L);
        spoEntity.setName("Test SPO");
        spoEntity.setDegree(new DegreeEntity());
        spoEntity.getDegree().setId(1L);
        spoEntity.getDegree().setName("Test Degree");
        spoEntity.setPublication(new Date());
        spoEntity.setHeader("<h1>Header</h1>");
        spoEntity.setFooter("<h1>Footer</h1>");
    }

    @Test
    public void testGenerateSpo() {
        // Prepare mocked inputs
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));

        // Prepare mocked paragraphs
        List<ParagraphEntity> paragraphEntities = new ArrayList<>();
        ParagraphEntity paragraphEntity = new ParagraphEntity();
        paragraphEntity.setTitle("Paragraph Title");
        paragraphEntity.setText("<p>Paragraph text content.</p>");
        paragraphEntities.add(paragraphEntity);

        when(paragraphRepository.findBySpoId(1L)).thenReturn(paragraphEntities);

        // Mock ModuleFrameSetDTO response for module frame service
        ModuleFrameSetDTO moduleFrameSetDTO = new ModuleFrameSetDTO();
        ModuleFrameSetDTO.Section section = new ModuleFrameSetDTO.Section();
        section.setName("Module Section");
        section.setModuleTypes(new ArrayList<>());
        moduleFrameSetDTO.setSections(List.of(section));

        when(moduleFrameService.getModuleFrameSetDTOBySpoId(1L)).thenReturn(moduleFrameSetDTO);

        // Call the method
        ByteArrayOutputStream outputStream = spoPdfService.generateSpo(1L);

        // Validate result
        assertNotNull(outputStream);
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testGenerateSpo_SpoNotFound() {
        // Mock the behavior when the SPO ID does not exist
        when(spoRepository.findById(1L)).thenReturn(Optional.empty());

        // Validate that the exception is thrown
        assertThrows(NoSuchElementException.class, () -> {
            spoPdfService.generateSpo(1L);
        });
    }

    @Test
    public void testGenerateSpo_NoParagraphs() {
        // Prepare mocked inputs
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));
        when(paragraphRepository.findBySpoId(1L)).thenReturn(new ArrayList<>()); // No paragraphs

        // Mock ModuleFrameSetDTO response for module frame service
        ModuleFrameSetDTO moduleFrameSetDTO = new ModuleFrameSetDTO();
        moduleFrameSetDTO.setSections(new ArrayList<>());
        when(moduleFrameService.getModuleFrameSetDTOBySpoId(1L)).thenReturn(moduleFrameSetDTO);

        // Call the method
        ByteArrayOutputStream outputStream = spoPdfService.generateSpo(1L);

        // Validate result
        assertNotNull(outputStream);
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testGenerateSpo_WithFooter() {
        // Prepare mocked inputs
        when(spoRepository.findById(1L)).thenReturn(Optional.of(spoEntity));

        // Prepare mocked paragraphs
        List<ParagraphEntity> paragraphEntities = new ArrayList<>();
        ParagraphEntity paragraphEntity = new ParagraphEntity();
        paragraphEntity.setTitle("Paragraph Title");
        paragraphEntity.setText("<p>Paragraph text content.</p>");
        paragraphEntities.add(paragraphEntity);

        when(paragraphRepository.findBySpoId(1L)).thenReturn(paragraphEntities);

        // Mock ModuleFrameSetDTO response for module frame service
        ModuleFrameSetDTO moduleFrameSetDTO = new ModuleFrameSetDTO();
        ModuleFrameSetDTO.Section section = new ModuleFrameSetDTO.Section();
        section.setName("Module Section");
        section.setModuleTypes(new ArrayList<>());
        moduleFrameSetDTO.setSections(List.of(section));

        when(moduleFrameService.getModuleFrameSetDTOBySpoId(1L)).thenReturn(moduleFrameSetDTO);

        // Call the method
        ByteArrayOutputStream outputStream = spoPdfService.generateSpo(1L);

        // Validate result
        assertNotNull(outputStream);
        assertTrue(outputStream.size() > 0);
    }
}
