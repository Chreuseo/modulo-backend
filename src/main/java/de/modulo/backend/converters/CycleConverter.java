package de.modulo.backend.converters;

import de.modulo.backend.dtos.CycleDTO;
import de.modulo.backend.entities.CycleEntity;
import org.springframework.stereotype.Component;

@Component
public class CycleConverter {

    public CycleDTO toDto(CycleEntity cycleEntity) {
        if (cycleEntity == null) {
            return null;
        }

        CycleDTO cycleDto = new CycleDTO();
        cycleDto.setId(cycleEntity.getId());
        cycleDto.setName(cycleEntity.getName());

        return cycleDto;
    }

    public CycleEntity toEntity(CycleDTO cycleDto) {
        if (cycleDto == null) {
            return null;
        }

        CycleEntity cycleEntity = new CycleEntity();
        cycleEntity.setId(cycleDto.getId());
        cycleEntity.setName(cycleDto.getName());

        return cycleEntity;
    }
}
