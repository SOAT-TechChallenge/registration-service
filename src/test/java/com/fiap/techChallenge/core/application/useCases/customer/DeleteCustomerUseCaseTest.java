package com.fiap.techChallenge.core.application.useCases.customer;

import com.fiap.techChallenge.core.application.useCases.user.customer.DeleteCustomerUseCase;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.gateways.user.CustomerGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteCustomerUseCase Tests")
class DeleteCustomerUseCaseTest {

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private DeleteCustomerUseCase deleteCustomerUseCase;

    private UUID validCustomerId;
    private UUID invalidCustomerId;
    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        validCustomerId = UUID.randomUUID();
        invalidCustomerId = UUID.randomUUID();

        mockCustomer = Customer.build(
                validCustomerId,
                "João Silva",
                "joao.silva@email.com",
                "12345678901",
                false
        );
    }

    @Test
    @DisplayName("Deve deletar cliente com sucesso quando cliente existe")
    void shouldDeleteCustomerSuccessfully_WhenCustomerExists() {
        // Arrange
        when(customerGateway.findFirstById(validCustomerId)).thenReturn(mockCustomer);
        doNothing().when(customerGateway).delete(validCustomerId);

        // Act
        assertDoesNotThrow(() -> deleteCustomerUseCase.execute(validCustomerId));

        // Assert
        verify(customerGateway).findFirstById(validCustomerId);
        verify(customerGateway).delete(validCustomerId);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando cliente não existe")
    void shouldThrowEntityNotFoundException_WhenCustomerDoesNotExist() {
        // Arrange
        when(customerGateway.findFirstById(invalidCustomerId)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> deleteCustomerUseCase.execute(invalidCustomerId)
        );

        assertEquals("Registro não encontrado: Customer", exception.getMessage());
        verify(customerGateway).findFirstById(invalidCustomerId);
        verify(customerGateway, never()).delete(any(UUID.class));
    }

    @Test
    @DisplayName("Deve deletar cliente anônimo com sucesso")
    void shouldDeleteAnonymousCustomerSuccessfully() {
        // Arrange
        Customer anonymousCustomer = Customer.build(
                validCustomerId,
                "Cliente Anônimo",
                null,
                null,
                true
        );

        when(customerGateway.findFirstById(validCustomerId)).thenReturn(anonymousCustomer);
        doNothing().when(customerGateway).delete(validCustomerId);

        // Act
        assertDoesNotThrow(() -> deleteCustomerUseCase.execute(validCustomerId));

        // Assert
        verify(customerGateway).findFirstById(validCustomerId);
        verify(customerGateway).delete(validCustomerId);
    }

    @Test
    @DisplayName("Deve verificar que delete não é chamado quando findFirstById retorna null")
    void shouldNotCallDelete_WhenFindFirstByIdReturnsNull() {
        // Arrange
        when(customerGateway.findFirstById(invalidCustomerId)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            deleteCustomerUseCase.execute(invalidCustomerId);
        });

        verify(customerGateway).findFirstById(invalidCustomerId);
        verify(customerGateway, never()).delete(any(UUID.class));
        verifyNoMoreInteractions(customerGateway);
    }
}
