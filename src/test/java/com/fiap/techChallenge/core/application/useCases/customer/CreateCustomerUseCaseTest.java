package com.fiap.techChallenge.core.application.useCases.customer;

import com.fiap.techChallenge.core.application.dto.user.CustomerInputDTO;
import com.fiap.techChallenge.core.application.useCases.user.customer.CreateCustomerUseCase;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import com.fiap.techChallenge.core.domain.exceptions.user.UserAlreadyExistsException;
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
@DisplayName("CreateCustomerUseCase Tests")
class CreateCustomerUseCaseTest {

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private CreateCustomerUseCase createCustomerUseCase;

    private CustomerInputDTO validCustomerInputDTO;
    private CustomerInputDTO anonymousCustomerInputDTO;
    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        validCustomerInputDTO = new CustomerInputDTO(
                "João Silva",
                "joao.silva@email.com",
                "123.456.789-01",
                false
        );

        anonymousCustomerInputDTO = new CustomerInputDTO(
                "Cliente Anônimo",
                null,
                null,
                true
        );

        mockCustomer = Customer.build(
                UUID.randomUUID(),
                "João Silva",
                "joao.silva@email.com",
                "12345678901",
                false
        );
    }

    @Test
    @DisplayName("Deve criar um cliente com sucesso quando CPF não existe")
    void shouldCreateCustomerSuccessfullyWhenCpfDoesNotExist() {
        // Arrange
        when(customerGateway.findFirstByCpf(validCustomerInputDTO.cpf())).thenReturn(null);
        when(customerGateway.save(any(Customer.class))).thenReturn(mockCustomer);

        // Act
        Customer result = createCustomerUseCase.execute(validCustomerInputDTO);

        // Assert
        assertNotNull(result);
        verify(customerGateway).findFirstByCpf(validCustomerInputDTO.cpf());
        verify(customerGateway).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve criar um cliente anônimo com sucesso")
    void shouldCreateAnonymousCustomerSuccessfully() {
        // Arrange
        Customer anonymousCustomer = Customer.build(
                UUID.randomUUID(),
                "Cliente Anônimo",
                null,
                null,
                true
        );
        when(customerGateway.findFirstByCpf(null)).thenReturn(null);
        when(customerGateway.save(any(Customer.class))).thenReturn(anonymousCustomer);

        // Act
        Customer result = createCustomerUseCase.execute(anonymousCustomerInputDTO);

        // Assert
        assertNotNull(result);
        assertTrue(result.isAnonymous());
        verify(customerGateway).findFirstByCpf(null);
        verify(customerGateway).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve lançar UserAlreadyExistsException quando CPF já existe")
    void shouldThrowUserAlreadyExistsExceptionWhenCpfAlreadyExists() {
        // Arrange
        when(customerGateway.findFirstByCpf(validCustomerInputDTO.cpf())).thenReturn(mockCustomer);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> {
            createCustomerUseCase.execute(validCustomerInputDTO);
        });

        verify(customerGateway).findFirstByCpf(validCustomerInputDTO.cpf());
        verify(customerGateway, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve criar cliente com dados mínimos válidos")
    void shouldCreateCustomerWithMinimalValidData() {
        // Arrange
        CustomerInputDTO minimalCustomerDTO = new CustomerInputDTO(
                "Ana",
                "ana@test.com",
                "987.654.321-00",
                false
        );

        Customer expectedCustomer = Customer.build(
                UUID.randomUUID(),
                "Ana",
                "ana@test.com",
                "98765432100",
                false
        );

        when(customerGateway.findFirstByCpf(minimalCustomerDTO.cpf())).thenReturn(null);
        when(customerGateway.save(any(Customer.class))).thenReturn(expectedCustomer);

        // Act
        Customer result = createCustomerUseCase.execute(minimalCustomerDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Ana", result.getName());
        assertEquals("ana@test.com", result.getEmail());
        assertFalse(result.isAnonymous());
        verify(customerGateway).findFirstByCpf(minimalCustomerDTO.cpf());
        verify(customerGateway).save(any(Customer.class));
    }
}
