package de.modulo.backend.converters;

import de.modulo.backend.dtos.MaternityProtectionDTO;
import de.modulo.backend.entities.MaternityProtectionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MaternityProtectionConverterTest {

    private MaternityProtectionConverter maternityProtectionConverter;

    @BeforeEach
    void setUp() {
        maternityProtectionConverter = new MaternityProtectionConverter();
    }

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
}
