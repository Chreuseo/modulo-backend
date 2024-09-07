package de.modulo.backend.converters;
import de.modulo.backend.dtos.DurationDTO;
import de.modulo.backend.entities.DurationEntity;
import org.springframework.stereotype.Component;

@Component
public class DurationConverter {

    public DurationDTO toDto(DurationEntity durationEntity) {
        if (durationEntity == null) {
            return null;
        }

        DurationDTO durationDto = new DurationDTO();
        durationDto.setId(durationEntity.getId());
        durationDto.setName(durationEntity.getName());

        return durationDto;
    }

    public DurationEntity toEntity(DurationDTO durationDto) {
        if (durationDto == null) {
            return null;
        }

        DurationEntity durationEntity = new DurationEntity();
        durationEntity.setId(durationDto.getId());
        durationEntity.setName(durationDto.getName());

        return durationEntity;
    }
}
