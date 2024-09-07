package de.modulo.backend.converters;

import de.modulo.backend.dtos.MaternityProtectionDTO;
import de.modulo.backend.entities.MaternityProtectionEntity;
import org.springframework.stereotype.Component;

@Component
public class MaternityProtectionConverter {

    public MaternityProtectionDTO toDto(MaternityProtectionEntity maternityProtectionEntity) {
        if (maternityProtectionEntity == null) {
            return null;
        }

        MaternityProtectionDTO maternityProtectionDto = new MaternityProtectionDTO();
        maternityProtectionDto.setId(maternityProtectionEntity.getId());
        maternityProtectionDto.setName(maternityProtectionEntity.getName());

        return maternityProtectionDto;
    }

    public MaternityProtectionEntity toEntity(MaternityProtectionDTO maternityProtectionDto) {
        if (maternityProtectionDto == null) {
            return null;
        }

        MaternityProtectionEntity maternityProtectionEntity = new MaternityProtectionEntity();
        maternityProtectionEntity.setId(maternityProtectionDto.getId());
        maternityProtectionEntity.setName(maternityProtectionDto.getName());

        return maternityProtectionEntity;
    }
}
