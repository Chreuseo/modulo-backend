package de.modulo.backend.converters;

import de.modulo.backend.dtos.MaternityProtectionDTO;
import de.modulo.backend.entities.MaternityProtectionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MaternityProtectionConverterTest {

    @InjectMocks
    private MaternityProtectionConverter maternityProtectionConverter;

    @Test
    void testToDto() {
        // Arrange
        MaternityProtectionEntity maternityProtectionEntity = new MaternityProtectionEntity();
        maternityProtectionEntity.setId(1L);
        maternityProtectionEntity.setName("Mutterschutz");

        // Act
        MaternityProtectionDTO maternityProtectionDto = maternityProtectionConverter.toDto(maternityProtectionEntity);

        // Assert
        assertNotNull(maternityProtectionDto);
        assertEquals(maternityProtectionEntity.getId(), maternityProtectionDto.getId());
        assertEquals(maternityProtectionEntity.getName(), maternityProtectionDto.getName());
    }

    @Test
    void testToDto_NullEntity() {
        // Act
        MaternityProtectionDTO maternityProtectionDto = maternityProtectionConverter.toDto(null);

        // Assert
        assertNull(maternityProtectionDto);
    }

    @Test
    void testToEntity() {
        // Arrange
        MaternityProtectionDTO maternityProtectionDto = new MaternityProtectionDTO();
        maternityProtectionDto.setId(1L);
        maternityProtectionDto.setName("Mutterschutz");

        // Act
        MaternityProtectionEntity maternityProtectionEntity = maternityProtectionConverter.toEntity(maternityProtectionDto);

        // Assert
        assertNotNull(maternityProtectionEntity);
        assertEquals(maternityProtectionDto.getId(), maternityProtectionEntity.getId());
        assertEquals(maternityProtectionDto.getName(), maternityProtectionEntity.getName());
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        MaternityProtectionEntity maternityProtectionEntity = maternityProtectionConverter.toEntity(null);

        // Assert
        assertNull(maternityProtectionEntity);
    }
}
