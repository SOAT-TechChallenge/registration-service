package com.fiap.techChallenge.core.application.useCases.customer;

import com.fiap.techChallenge.core.application.dto.user.CustomerInputDTO;
import com.fiap.techChallenge.core.application.useCases.user.customer.UpdateCustomerUseCase;
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
@DisplayName("UpdateCustomerUseCase Tests")
class UpdateCustomerUseCaseTest {

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private UpdateCustomerUseCase updateCustomerUseCase;

    private UUID validCustomerId;
    private UUID invalidCustomerId;
    private CustomerInputDTO updateCustomerInputDTO;
    private CustomerInputDTO anonymousCustomerInputDTO;
    private Customer existingCustomer;
    private Customer updatedCustomer;

    @BeforeEach
    void setUp() {
        validCustomerId = UUID.randomUUID();
        invalidCustomerId = UUID.randomUUID();

        updateCustomerInputDTO = new CustomerInputDTO(
                "João Silva Atualizado",
                "joao.silva.novo@email.com",
                "123.456.789-01",
                false
        );

        anonymousCustomerInputDTO = new CustomerInputDTO(
                "Cliente Anônimo",
                null,
                null,
                true
        );

        existingCustomer = Customer.build(
                validCustomerId,
                "João Silva",
                "joao.silva@email.com",
                "12345678901",
                false
        );

        updatedCustomer = Customer.build(
                validCustomerId,
                "João Silva Atualizado",
                "joao.silva.novo@email.com",
                "12345678901",
                false
        );
    }

    @Test
    @DisplayName("Deve atualizar cliente com sucesso quando cliente existe")
    void shouldUpdateCustomerSuccessfullyWhenCustomerExists() {
        // Arrange
        when(customerGateway.findFirstById(validCustomerId)).thenReturn(existingCustomer);
        when(customerGateway.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Act
        Customer result = updateCustomerUseCase.execute(validCustomerId, updateCustomerInputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(validCustomerId, result.getId());
        assertEquals("João Silva Atualizado", result.getName());
        assertEquals("joao.silva.novo@email.com", result.getEmail());
        assertEquals("12345678901", result.getUnformattedCpf());
        assertFalse(result.isAnonymous());

        verify(customerGateway).findFirstById(validCustomerId);
        verify(customerGateway).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando cliente não existe")
    void shouldThrowEntityNotFoundException_WhenCustomerDoesNotExist() {
        // Arrange
        when(customerGateway.findFirstById(invalidCustomerId)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> updateCustomerUseCase.execute(invalidCustomerId, updateCustomerInputDTO)
        );

        assertEquals("Registro não encontrado: Customer", exception.getMessage());
        verify(customerGateway).findFirstById(invalidCustomerId);
        verify(customerGateway, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve atualizar cliente para anônimo com sucesso")
    void shouldUpdateCustomerToAnonymousSuccessfully() {
        // Arrange
        Customer anonymousCustomer = Customer.build(
                validCustomerId,
                "Cliente Anônimo",
                null,
                null,
                true
        );

        when(customerGateway.findFirstById(validCustomerId)).thenReturn(existingCustomer);
        when(customerGateway.save(any(Customer.class))).thenReturn(anonymousCustomer);

        // Act
        Customer result = updateCustomerUseCase.execute(validCustomerId, anonymousCustomerInputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(validCustomerId, result.getId());
        assertEquals("Cliente Anônimo", result.getName());
        assertNull(result.getEmail());
        assertNull(result.getUnformattedCpf());
        assertTrue(result.isAnonymous());

        verify(customerGateway).findFirstById(validCustomerId);
        verify(customerGateway).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve atualizar cliente anônimo para cliente não anônimo")
    void shouldUpdateAnonymousCustomerToNonAnonymCustomer() {
        // Arrange
        Customer existingAnonymousCustomer = Customer.build(
                validCustomerId,
                "Cliente Anônimo",
                null,
                null,
                true
        );

        when(customerGateway.findFirstById(validCustomerId)).thenReturn(existingAnonymousCustomer);
        when(customerGateway.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Act
        Customer result = updateCustomerUseCase.execute(validCustomerId, updateCustomerInputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(validCustomerId, result.getId());
        assertEquals("João Silva Atualizado", result.getName());
        assertEquals("joao.silva.novo@email.com", result.getEmail());
        assertEquals("12345678901", result.getUnformattedCpf());
        assertFalse(result.isAnonymous());

        verify(customerGateway).findFirstById(validCustomerId);
        verify(customerGateway).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve retornar customer construído com novos dados")
    void shouldReturnCustomerBuiltWithNewData() {
        // Arrange
        when(customerGateway.findFirstById(validCustomerId)).thenReturn(existingCustomer);
        when(customerGateway.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Act
        Customer result = updateCustomerUseCase.execute(validCustomerId, updateCustomerInputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedCustomer.getName(), result.getName());
        assertEquals(updatedCustomer.getEmail(), result.getEmail());
        assertEquals(updatedCustomer.getUnformattedCpf(), result.getUnformattedCpf());
        assertEquals(updatedCustomer.isAnonymous(), result.isAnonymous());
        assertEquals(validCustomerId, result.getId());
    }

    @Test
    @DisplayName("Deve atualizar apenas campos específicos")
    void shouldUpdateOnlySpecificFields() {
        // Arrange
        CustomerInputDTO partialUpdateDTO = new CustomerInputDTO(
                "Novo Nome",
                "novo.email@test.com",
                "987.654.321-00",
                false
        );

        Customer partialUpdatedCustomer = Customer.build(
                validCustomerId,
                "Novo Nome",
                "novo.email@test.com",
                "987.654.321-00",
                false
        );

        when(customerGateway.findFirstById(validCustomerId)).thenReturn(existingCustomer);
        when(customerGateway.save(any(Customer.class))).thenReturn(partialUpdatedCustomer);

        // Act
        Customer result = updateCustomerUseCase.execute(validCustomerId, partialUpdateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Novo Nome", result.getName());
        assertEquals("novo.email@test.com", result.getEmail());
        assertEquals("98765432100", result.getUnformattedCpf());
        assertEquals(validCustomerId, result.getId());
        verify(customerGateway).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve manter o ID original do customer durante atualização")
    void shouldMaintainOriginalCustomerIdDuringUpdate() {
        // Arrange
        when(customerGateway.findFirstById(validCustomerId)).thenReturn(existingCustomer);
        when(customerGateway.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Act
        Customer result = updateCustomerUseCase.execute(validCustomerId, updateCustomerInputDTO);

        // Assert
        assertEquals(validCustomerId, result.getId());
        assertEquals(existingCustomer.getId(), result.getId());
        verify(customerGateway).save(argThat(customer ->
            customer.getId().equals(validCustomerId)
        ));
    }

    @Test
    @DisplayName("Deve tratar ID nulo")
    void shouldHandleNullId() {
        // Arrange
        when(customerGateway.findFirstById(null)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            updateCustomerUseCase.execute(null, updateCustomerInputDTO);
        });

        verify(customerGateway).findFirstById(null);
        verify(customerGateway, never()).save(any(Customer.class));
    }
}
