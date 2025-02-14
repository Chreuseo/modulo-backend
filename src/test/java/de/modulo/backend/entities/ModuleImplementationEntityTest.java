package de.modulo.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ModuleImplementationEntityTest {

    @InjectMocks
    private ModuleImplementationEntity moduleImplementationEntity;

    private UserEntity mockFirstExaminant = new UserEntity();
    private UserEntity mockSecondExaminant = new UserEntity();
    private UserEntity mockResponsible = new UserEntity();

    @Mock
    private CycleEntity mockCycle;

    @Mock
    private DurationEntity mockDuration;

    @Mock
    private LanguageEntity mockLanguage;

    @Mock
    private MaternityProtectionEntity mockMaternityProtection;

    private Long id;
    private String name;
    private String abbreviation;
    private String allowedResources;
    private String workload;
    private String requiredCompetences;
    private String qualificationTargets;
    private String content;
    private String additionalExams;
    private String mediaTypes;
    private String literature;

    @BeforeEach
    public void setUp() {
        id = 1L;
        name = "Module Name";
        abbreviation = "MN";
        allowedResources = "Resources";
        workload = "40 hours";
        requiredCompetences = "Competence 1, Competence 2";
        qualificationTargets = "Target 1, Target 2";
        content = "Module content";
        additionalExams = "Exam 1";
        mediaTypes = "Media 1, Media 2";
        literature = "Some literature";

        mockFirstExaminant.setId(1L);
        mockSecondExaminant.setId(2L);
        mockResponsible.setId(3L);

        moduleImplementationEntity.setId(id);
        moduleImplementationEntity.setName(name);
        moduleImplementationEntity.setAbbreviation(abbreviation);
        moduleImplementationEntity.setAllowedResources(allowedResources);
        moduleImplementationEntity.setWorkload(workload);
        moduleImplementationEntity.setRequiredCompetences(requiredCompetences);
        moduleImplementationEntity.setQualificationTargets(qualificationTargets);
        moduleImplementationEntity.setContent(content);
        moduleImplementationEntity.setAdditionalExams(additionalExams);
        moduleImplementationEntity.setMediaTypes(mediaTypes);
        moduleImplementationEntity.setLiterature(literature);
        moduleImplementationEntity.setFirstExaminant(mockFirstExaminant);
        moduleImplementationEntity.setSecondExaminant(mockSecondExaminant);
        moduleImplementationEntity.setResponsible(mockResponsible);
        moduleImplementationEntity.setCycle(mockCycle);
        moduleImplementationEntity.setDuration(mockDuration);
        moduleImplementationEntity.setLanguage(mockLanguage);
        moduleImplementationEntity.setMaternityProtection(mockMaternityProtection);
    }

    @Test
    public void testSettersAndGetters() {
        assertEquals(id, moduleImplementationEntity.getId());
        assertEquals(name, moduleImplementationEntity.getName());
        assertEquals(abbreviation, moduleImplementationEntity.getAbbreviation());
        assertEquals(allowedResources, moduleImplementationEntity.getAllowedResources());
        assertEquals(workload, moduleImplementationEntity.getWorkload());
        assertEquals(requiredCompetences, moduleImplementationEntity.getRequiredCompetences());
        assertEquals(qualificationTargets, moduleImplementationEntity.getQualificationTargets());
        assertEquals(content, moduleImplementationEntity.getContent());
        assertEquals(additionalExams, moduleImplementationEntity.getAdditionalExams());
        assertEquals(mediaTypes, moduleImplementationEntity.getMediaTypes());
        assertEquals(literature, moduleImplementationEntity.getLiterature());
        assertEquals(mockFirstExaminant, moduleImplementationEntity.getFirstExaminant());
        assertEquals(mockSecondExaminant, moduleImplementationEntity.getSecondExaminant());
        assertEquals(mockResponsible, moduleImplementationEntity.getResponsible());
        assertEquals(mockCycle, moduleImplementationEntity.getCycle());
        assertEquals(mockDuration, moduleImplementationEntity.getDuration());
        assertEquals(mockLanguage, moduleImplementationEntity.getLanguage());
        assertEquals(mockMaternityProtection, moduleImplementationEntity.getMaternityProtection());
    }

    @Test
    public void testIdInitialization() {
        // Test that ID is initialized to null
        ModuleImplementationEntity entity = new ModuleImplementationEntity();
        assertNull(entity.getId());
    }

    @Test
    public void testEqualsAndHashCode() {
        ModuleImplementationEntity otherEntity = new ModuleImplementationEntity();
        otherEntity.setId(id);

        moduleImplementationEntity.setId(id);

        // Both entities should be equal since IDs match
        assertEquals(moduleImplementationEntity, otherEntity);

        // Now they should not be equal with different IDs
        otherEntity.setId(2L);
        assertNotEquals(moduleImplementationEntity, otherEntity);
    }

    @Test
    public void testEqualsDifferentObjects() {
        // Different object types should not be equal
        assertNotEquals(moduleImplementationEntity, new Object());
    }

    @Test
    public void testEqualsNull() {
        // Comparing with null should return false
        assertNotEquals(moduleImplementationEntity, null);
    }
}
