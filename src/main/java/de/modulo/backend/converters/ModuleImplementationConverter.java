package de.modulo.backend.converters;

import de.modulo.backend.dtos.ModuleImplementationDTO;
import de.modulo.backend.dtos.ModuleImplementationDTOFlat;
import de.modulo.backend.dtos.SpoDTOFlat;
import de.modulo.backend.entities.ModuleImplementationEntity;
import de.modulo.backend.repositories.ModuleFrameModuleImplementationRepository;
import de.modulo.backend.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModuleImplementationConverter {

    private final UserConverter userConverter; // Assuming you have a UserConverter
    private final CycleConverter cycleConverter; // Assuming you have a CycleConverter
    private final DurationConverter durationConverter; // Assuming you have a DurationConverter
    private final LanguageConverter languageConverter; // Assuming you have a LanguageConverter
    private final MaternityProtectionConverter maternityProtectionConverter; // Assuming you have a MaternityProtectionConverter

    private final ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository; // Assuming you have a ModuleFrameModuleImplementationRepository
    private final SpoConverter spoConverter;
    private final UserRepository userRepository;

    // Constructor injection for converters
    public ModuleImplementationConverter(UserConverter userConverter,
                                         CycleConverter cycleConverter,
                                         DurationConverter durationConverter,
                                         LanguageConverter languageConverter,
                                         MaternityProtectionConverter maternityProtectionConverter,
                                         ModuleFrameModuleImplementationRepository moduleFrameModuleImplementationRepository,
                                         SpoConverter spoConverter,
                                         UserRepository userRepository) {
        this.userConverter = userConverter;
        this.cycleConverter = cycleConverter;
        this.durationConverter = durationConverter;
        this.languageConverter = languageConverter;
        this.maternityProtectionConverter = maternityProtectionConverter;

        this.moduleFrameModuleImplementationRepository = moduleFrameModuleImplementationRepository;
        this.spoConverter = spoConverter;
        this.userRepository = userRepository;
    }

    public ModuleImplementationDTO toDto(ModuleImplementationEntity moduleImplementationEntity) {
        if (moduleImplementationEntity == null) {
            return null;
        }

        ModuleImplementationDTO moduleImplementationDto = new ModuleImplementationDTO();
        moduleImplementationDto.setId(moduleImplementationEntity.getId());
        moduleImplementationDto.setName(moduleImplementationEntity.getName());
        moduleImplementationDto.setAbbreviation(moduleImplementationEntity.getAbbreviation());
        moduleImplementationDto.setAllowedResources(moduleImplementationEntity.getAllowedResources());
        moduleImplementationDto.setFirstExaminant(userConverter.toDtoFlat(moduleImplementationEntity.getFirstExaminant()));
        moduleImplementationDto.setFirstExaminant(userConverter.toDtoFlat(moduleImplementationEntity.getFirstExaminant()));
        moduleImplementationDto.setSecondExaminant(userConverter.toDtoFlat(moduleImplementationEntity.getSecondExaminant()));
        moduleImplementationDto.setResponsible(userConverter.toDtoFlat(moduleImplementationEntity.getResponsible()));
        moduleImplementationDto.setCycle(cycleConverter.toDto(moduleImplementationEntity.getCycle()));
        moduleImplementationDto.setDuration(durationConverter.toDto(moduleImplementationEntity.getDuration()));
        moduleImplementationDto.setLanguage(languageConverter.toDto(moduleImplementationEntity.getLanguage()));
        moduleImplementationDto.setWorkload(moduleImplementationEntity.getWorkload());
        moduleImplementationDto.setRequiredCompetences(moduleImplementationEntity.getRequiredCompetences());
        moduleImplementationDto.setQualificationTargets(moduleImplementationEntity.getQualificationTargets());
        moduleImplementationDto.setContent(moduleImplementationEntity.getContent());
        moduleImplementationDto.setAdditionalExams(moduleImplementationEntity.getAdditionalExams());
        moduleImplementationDto.setMediaTypes(moduleImplementationEntity.getMediaTypes());
        moduleImplementationDto.setLiterature(moduleImplementationEntity.getLiterature());
        moduleImplementationDto.setMaternityProtection(maternityProtectionConverter.toDto(moduleImplementationEntity.getMaternityProtection()));

        return moduleImplementationDto;
    }

    public ModuleImplementationEntity toEntity(ModuleImplementationDTO moduleImplementationDto) {
        if (moduleImplementationDto == null) {
            return null;
        }

        ModuleImplementationEntity moduleImplementationEntity = new ModuleImplementationEntity();
        moduleImplementationEntity.setId(moduleImplementationDto.getId());
        moduleImplementationEntity.setName(moduleImplementationDto.getName());
        moduleImplementationEntity.setAbbreviation(moduleImplementationDto.getAbbreviation());
        moduleImplementationEntity.setAllowedResources(moduleImplementationDto.getAllowedResources());
        moduleImplementationEntity.setFirstExaminant(userRepository.findById(moduleImplementationDto.getFirstExaminant().getId()).orElse(null));
        moduleImplementationEntity.setSecondExaminant(userRepository.findById(moduleImplementationDto.getSecondExaminant().getId()).orElse(null));
        moduleImplementationEntity.setResponsible(userRepository.findById(moduleImplementationDto.getResponsible().getId()).orElse(null));
        moduleImplementationEntity.setCycle(cycleConverter.toEntity(moduleImplementationDto.getCycle()));
        moduleImplementationEntity.setDuration(durationConverter.toEntity(moduleImplementationDto.getDuration()));
        moduleImplementationEntity.setLanguage(languageConverter.toEntity(moduleImplementationDto.getLanguage()));
        moduleImplementationEntity.setWorkload(moduleImplementationDto.getWorkload());
        moduleImplementationEntity.setRequiredCompetences(moduleImplementationDto.getRequiredCompetences());
        moduleImplementationEntity.setQualificationTargets(moduleImplementationDto.getQualificationTargets());
        moduleImplementationEntity.setContent(moduleImplementationDto.getContent());
        moduleImplementationEntity.setAdditionalExams(moduleImplementationDto.getAdditionalExams());
        moduleImplementationEntity.setMediaTypes(moduleImplementationDto.getMediaTypes());
        moduleImplementationEntity.setLiterature(moduleImplementationDto.getLiterature());
        moduleImplementationEntity.setMaternityProtection(maternityProtectionConverter.toEntity(moduleImplementationDto.getMaternityProtection()));

        return moduleImplementationEntity;
    }

    public ModuleImplementationDTOFlat toDtoFlat(ModuleImplementationEntity moduleImplementationEntity) {
        if (moduleImplementationEntity == null) {
            return null;
        }

        ModuleImplementationDTOFlat moduleImplementationDtoFlat = new ModuleImplementationDTOFlat();
        moduleImplementationDtoFlat.setId(moduleImplementationEntity.getId());
        moduleImplementationDtoFlat.setName(moduleImplementationEntity.getName());
        moduleImplementationDtoFlat.setAbbreviation(moduleImplementationEntity.getAbbreviation());

        List<SpoDTOFlat> spos = new ArrayList<>();
        moduleFrameModuleImplementationRepository.getModuleFrameModuleImplementationEntitiesByModuleImplementation(moduleImplementationEntity).forEach(moduleFrameModuleImplementationEntity -> spos.add(spoConverter.toDtoFlat(moduleFrameModuleImplementationEntity.getModuleFrame().getSpo())));
        moduleImplementationDtoFlat.setSpos(spos);

        return moduleImplementationDtoFlat;
    }

    public ModuleImplementationEntity toEntity(ModuleImplementationDTOFlat moduleImplementationDtoFlat) {
        if (moduleImplementationDtoFlat == null) {
            return null;
        }

        ModuleImplementationEntity moduleImplementationEntity = new ModuleImplementationEntity();
        moduleImplementationEntity.setName(moduleImplementationDtoFlat.getName());
        moduleImplementationEntity.setAbbreviation(moduleImplementationDtoFlat.getAbbreviation());

        return moduleImplementationEntity;
    }
}
