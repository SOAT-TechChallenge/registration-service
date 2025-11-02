package com.fiap.techChallenge._webApi.mappers;

import com.fiap.techChallenge._webApi.data.persistence.entity.user.AttendantEntity;
import com.fiap.techChallenge._webApi.data.persistence.entity.user.CPFEmbeddable;
import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AttendantMapper - Testes")
class AttendantMapperTest {

    @Test
    @DisplayName("Deve converter AttendantDTO para Attendant domain quando dto não é nulo")
    void shouldConvertDTOToDomainWhenDTOIsNotNull() {
        // Given
        UUID attendantId = UUID.randomUUID();
        AttendantDTO dto = AttendantDTO.builder()
                .id(attendantId)
                .name("Maria Santos")
                .email("maria@email.com")
                .cpf("98765432100")
                .build();

        // When
        Attendant attendant = AttendantMapper.DtoToDomain(dto);

        // Then
        assertNotNull(attendant);
        assertEquals(attendantId, attendant.getId());
        assertEquals("Maria Santos", attendant.getName());
        assertEquals("maria@email.com", attendant.getEmail());
        assertEquals("98765432100", attendant.getUnformattedCpf());
    }

    @Test
    @DisplayName("Deve retornar null quando dto é nulo ao converter para domain")
    void shouldReturnNullWhenDTOIsNull() {
        // When
        Attendant attendant = AttendantMapper.DtoToDomain(null);

        // Then
        assertNull(attendant);
    }

    @Test
    @DisplayName("Deve converter Attendant domain para AttendantDTO quando domain não é nulo")
    void shouldConvertDomainToDTOWhenDomainIsNotNull() {
        // Given
        UUID attendantId = UUID.randomUUID();
        Attendant attendant = Attendant.build(attendantId, "Maria Santos", "maria@email.com", "98765432100");

        // When
        AttendantDTO dto = AttendantMapper.domainToDto(attendant);

        // Then
        assertNotNull(dto);
        assertEquals(attendantId, dto.id());
        assertEquals("Maria Santos", dto.name());
        assertEquals("maria@email.com", dto.email());
        assertNotNull(dto.cpf());
    }

    @Test
    @DisplayName("Deve retornar null quando domain é nulo ao converter para dto")
    void shouldReturnNullWhenDomainIsNull() {
        // When
        AttendantDTO dto = AttendantMapper.domainToDto(null);

        // Then
        assertNull(dto);
    }

    @Test
    @DisplayName("Deve converter AttendantEntity para AttendantDTO quando entity não é nula")
    void shouldConvertEntityToDTOWhenEntityIsNotNull() {
        // Given
        UUID attendantId = UUID.randomUUID();
        AttendantEntity entity = new AttendantEntity();
        entity.setId(attendantId);
        entity.setName("Maria Santos");
        entity.setEmail("maria@email.com");
        entity.setCpf("98765432100");

        // When
        AttendantDTO dto = AttendantMapper.entityToDto(entity);

        // Then
        assertNotNull(dto);
        assertEquals(attendantId, dto.id());
        assertEquals("Maria Santos", dto.name());
        assertEquals("maria@email.com", dto.email());
        assertEquals("98765432100", dto.cpf());
    }

    @Test
    @DisplayName("Deve retornar null quando entity é nula ao converter para dto")
    void shouldReturnNullWhenEntityIsNull() {
        // When
        AttendantDTO dto = AttendantMapper.entityToDto(null);

        // Then
        assertNull(dto);
    }

    @Test
    @DisplayName("Deve converter AttendantDTO para AttendantEntity quando dto não é nulo")
    void shouldConvertDTOToEntityWhenDTOIsNotNull() {
        // Given
        UUID attendantId = UUID.randomUUID();
        AttendantDTO dto = AttendantDTO.builder()
                .id(attendantId)
                .name("Maria Santos")
                .email("maria@email.com")
                .cpf("98765432100")
                .build();

        // When
        AttendantEntity entity = AttendantMapper.dtoToEntity(dto);

        // Then
        assertNotNull(entity);
        assertEquals(attendantId, entity.getId());
        assertEquals("Maria Santos", entity.getName());
        assertEquals("maria@email.com", entity.getEmail());
        assertNotNull(entity.getCpf());
        assertEquals("98765432100", entity.getCpf().getNumber());
    }

    @Test
    @DisplayName("Deve retornar null quando dto é nulo ao converter para entity")
    void shouldReturnNullWhenDTOIsNullForEntityConversion() {
        // When
        AttendantEntity entity = AttendantMapper.dtoToEntity(null);

        // Then
        assertNull(entity);
    }
}

