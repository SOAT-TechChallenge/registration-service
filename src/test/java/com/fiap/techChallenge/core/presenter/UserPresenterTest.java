package com.fiap.techChallenge.core.presenter;

import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerAnonymDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserPresenter - Testes")
class UserPresenterTest {

    @Test
    @DisplayName("Deve converter Customer para CustomerFullDTO quando customer não é nulo")
    void shouldConvertCustomerToCustomerFullDTOWhenCustomerIsNotNull() {
        // Given
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.build(customerId, "João Silva", "joao@email.com", "12345678901", false);

        // When
        CustomerFullDTO dto = UserPresenter.toCustomerDTO(customer);

        // Then
        assertNotNull(dto);
        assertEquals(customerId, dto.id());
        assertEquals("João Silva", dto.name());
        assertEquals("joao@email.com", dto.email());
        assertNotNull(dto.cpf());
        assertFalse(dto.anonymous());
    }

    @Test
    @DisplayName("Deve converter Customer anônimo para CustomerAnonymDTO quando customer não é nulo")
    void shouldConvertAnonymousCustomerToCustomerAnonymDTOWhenCustomerIsNotNull() {
        // Given
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.build(customerId, null, null, null, true);

        // When
        CustomerAnonymDTO dto = UserPresenter.toAnonymCustomerDTO(customer);

        // Then
        assertNotNull(dto);
        assertEquals(customerId, dto.id());
        assertTrue(dto.anonymous());
    }

    @Test
    @DisplayName("Deve converter Attendant para AttendantDTO quando attendant não é nulo")
    void shouldConvertAttendantToAttendantDTOWhenAttendantIsNotNull() {
        // Given
        UUID attendantId = UUID.randomUUID();
        Attendant attendant = Attendant.build(attendantId, "Maria Santos", "maria@email.com", "98765432100");

        // When
        AttendantDTO dto = UserPresenter.toAttendantDTO(attendant);

        // Then
        assertNotNull(dto);
        assertEquals(attendantId, dto.id());
        assertEquals("Maria Santos", dto.name());
        assertEquals("maria@email.com", dto.email());
        assertNotNull(dto.cpf());
    }
}

