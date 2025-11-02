package com.fiap.techChallenge._webApi.mappers;

import com.fiap.techChallenge._webApi.data.persistence.entity.user.CPFEmbeddable;
import com.fiap.techChallenge._webApi.data.persistence.entity.user.CustomerEntity;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CustomerMapper - Testes")
class CustomerMapperTest {

    @Test
    @DisplayName("Deve converter CustomerFullDTO para Customer domain quando dto não é nulo")
    void shouldConvertDTOToDomainWhenDTOIsNotNull() {
        // Given
        UUID customerId = UUID.randomUUID();
        CustomerFullDTO dto = CustomerFullDTO.builder()
                .id(customerId)
                .name("João Silva")
                .email("joao@email.com")
                .cpf("12345678901")
                .anonymous(false)
                .build();

        // When
        Customer customer = CustomerMapper.customerDtoToDomain(dto);

        // Then
        assertNotNull(customer);
        assertEquals(customerId, customer.getId());
        assertEquals("João Silva", customer.getName());
        assertEquals("joao@email.com", customer.getEmail());
        assertEquals("12345678901", customer.getUnformattedCpf());
        assertFalse(customer.isAnonymous());
    }

    @Test
    @DisplayName("Deve converter Customer domain para CustomerFullDTO quando customer não é nulo")
    void shouldConvertDomainToDTOWhenCustomerIsNotNull() {
        // Given
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.build(customerId, "João Silva", "joao@email.com", "12345678901", false);

        // When
        CustomerFullDTO dto = CustomerMapper.customerDomainToDto(customer);

        // Then
        assertNotNull(dto);
        assertEquals(customerId, dto.id());
        assertEquals("João Silva", dto.name());
        assertEquals("joao@email.com", dto.email());
        assertNotNull(dto.cpf());
        assertFalse(dto.anonymous());
    }

    @Test
    @DisplayName("Deve converter CustomerEntity para CustomerFullDTO quando entity não é nula")
    void shouldConvertEntityToDTOWhenEntityIsNotNull() {
        // Given
        UUID customerId = UUID.randomUUID();
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customerId);
        entity.setName("João Silva");
        entity.setEmail("joao@email.com");
        entity.setCpf("12345678901");
        entity.setAnonymous(false);

        // When
        CustomerFullDTO dto = CustomerMapper.customerEntityToDto(entity);

        // Then
        assertNotNull(dto);
        assertEquals(customerId, dto.id());
        assertEquals("João Silva", dto.name());
        assertEquals("joao@email.com", dto.email());
        assertEquals("12345678901", dto.cpf());
        assertFalse(dto.anonymous());
    }

    @Test
    @DisplayName("Deve converter CustomerEntity para CustomerFullDTO quando CPF é nulo")
    void shouldConvertEntityToDTOWhenCPFIsNull() {
        // Given
        UUID customerId = UUID.randomUUID();
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customerId);
        entity.setName("João Silva");
        entity.setEmail("joao@email.com");
        entity.setCpf(null);
        entity.setAnonymous(true);

        // When
        CustomerFullDTO dto = CustomerMapper.customerEntityToDto(entity);

        // Then
        assertNotNull(dto);
        assertEquals(customerId, dto.id());
        assertEquals("João Silva", dto.name());
        assertEquals("joao@email.com", dto.email());
        assertNull(dto.cpf());
        assertTrue(dto.anonymous());
    }

    @Test
    @DisplayName("Deve converter CustomerFullDTO para CustomerEntity quando dto não é nulo")
    void shouldConvertDTOToEntityWhenDTOIsNotNull() {
        // Given
        UUID customerId = UUID.randomUUID();
        CustomerFullDTO dto = CustomerFullDTO.builder()
                .id(customerId)
                .name("João Silva")
                .email("joao@email.com")
                .cpf("12345678901")
                .anonymous(false)
                .build();

        // When
        CustomerEntity entity = CustomerMapper.customerDtoToEntity(dto);

        // Then
        assertNotNull(entity);
        assertEquals(customerId, entity.getId());
        assertEquals("João Silva", entity.getName());
        assertEquals("joao@email.com", entity.getEmail());
        assertNotNull(entity.getCpf());
        assertEquals("12345678901", entity.getCpf().getNumber());
        assertFalse(entity.isAnonymous());
    }
}

