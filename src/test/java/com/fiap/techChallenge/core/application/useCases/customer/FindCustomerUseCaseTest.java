package com.fiap.techChallenge.core.application.useCases.customer;

import com.fiap.techChallenge.core.application.useCases.user.customer.FindCustomerUseCase;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindCustomerUseCase Tests")
class FindCustomerUseCaseTest {

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private FindCustomerUseCase findCustomerUseCase;

    private UUID validCustomerId;
    private String validCpf;
    private String invalidCpf;
    private Customer mockCustomer;
    private Customer mockAnonymousCustomer;

    @BeforeEach
    void setUp() {
        validCustomerId = UUID.randomUUID();
        validCpf = "12345678901";
        invalidCpf = "99999999999";

        mockCustomer = Customer.build(
                validCustomerId,
                "João Silva",
                "joao.silva@email.com",
                validCpf,
                false
        );

        mockAnonymousCustomer = Customer.build(
                UUID.randomUUID(),
                "Cliente Anônimo",
                null,
                null,
                true
        );
    }

    @Test
    @DisplayName("Deve encontrar cliente por CPF quando cliente existe")
    void shouldFindCustomerByCpfWhenCustomerExists() {
        // Arrange
        when(customerGateway.findFirstByCpf(validCpf)).thenReturn(mockCustomer);

        // Act
        Customer result = findCustomerUseCase.execute(validCpf);

        // Assert
        assertNotNull(result);
        assertEquals(mockCustomer, result);
        assertEquals(validCpf, result.getUnformattedCpf());
        assertEquals("João Silva", result.getName());
        verify(customerGateway).findFirstByCpf(validCpf);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando cliente não existe por CPF")
    void shouldThrowEntityNotFoundExceptionWhenCustomerNotFoundByCpf() {
        // Arrange
        when(customerGateway.findFirstByCpf(invalidCpf)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> findCustomerUseCase.execute(invalidCpf)
        );

        assertEquals("Registro não encontrado: Customer", exception.getMessage());
        verify(customerGateway).findFirstByCpf(invalidCpf);
    }

    @Test
    @DisplayName("Deve encontrar cliente por ID quando cliente existe")
    void shouldFindCustomerByIdWhenCustomerExists() {
        // Arrange
        when(customerGateway.findFirstById(validCustomerId)).thenReturn(mockCustomer);

        // Act
        Customer result = findCustomerUseCase.execute(validCustomerId);

        // Assert
        assertNotNull(result);
        assertEquals(mockCustomer, result);
        assertEquals(validCustomerId, result.getId());
        assertEquals("João Silva", result.getName());
        verify(customerGateway).findFirstById(validCustomerId);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando cliente não existe por ID")
    void shouldThrowEntityNotFoundExceptionWhenCustomerNotFoundById() {
        // Arrange
        UUID invalidId = UUID.randomUUID();
        when(customerGateway.findFirstById(invalidId)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> findCustomerUseCase.execute(invalidId)
        );

        assertEquals("Registro não encontrado: Customer", exception.getMessage());
        verify(customerGateway).findFirstById(invalidId);
    }

    @Test
    @DisplayName("Deve encontrar cliente anônimo por ID")
    void shouldFindAnonymousCustomerById() {
        // Arrange
        UUID anonymousId = mockAnonymousCustomer.getId();
        when(customerGateway.findFirstById(anonymousId)).thenReturn(mockAnonymousCustomer);

        // Act
        Customer result = findCustomerUseCase.execute(anonymousId);

        // Assert
        assertNotNull(result);
        assertEquals(mockAnonymousCustomer, result);
        assertTrue(result.isAnonymous());
        verify(customerGateway).findFirstById(anonymousId);
    }

    @Test
    @DisplayName("Deve retornar o mesmo objeto Customer retornado pelo gateway por CPF")
    void shouldReturnSameCustomerObjectFromGatewayByCpf() {
        // Arrange
        when(customerGateway.findFirstByCpf(validCpf)).thenReturn(mockCustomer);

        // Act
        Customer result = findCustomerUseCase.execute(validCpf);

        // Assert
        assertSame(mockCustomer, result);
    }

    @Test
    @DisplayName("Deve retornar o mesmo objeto Customer retornado pelo gateway por ID")
    void shouldReturnSameCustomerObjectFromGatewayById() {
        // Arrange
        when(customerGateway.findFirstById(validCustomerId)).thenReturn(mockCustomer);

        // Act
        Customer result = findCustomerUseCase.execute(validCustomerId);

        // Assert
        assertSame(mockCustomer, result);
    }

    @Test
    @DisplayName("Deve funcionar com CPFs de diferentes formatos")
    void shouldWorkWithDifferentCpfFormats() {
        // Arrange
        String cpfWithDots = "123.456.789-01";
        Customer customerWithFormattedCpf = Customer.build(
                UUID.randomUUID(),
                "Maria Santos",
                "maria@email.com",
                cpfWithDots,
                false
        );

        when(customerGateway.findFirstByCpf(cpfWithDots)).thenReturn(customerWithFormattedCpf);

        // Act
        Customer result = findCustomerUseCase.execute(cpfWithDots);

        // Assert
        assertNotNull(result);
        assertEquals(customerWithFormattedCpf, result);
        verify(customerGateway).findFirstByCpf(cpfWithDots);
    }

    @Test
    @DisplayName("Deve tratar CPF nulo ou vazio")
    void shouldHandleNullOrEmptyCpf() {
        // Arrange
        when(customerGateway.findFirstByCpf(null)).thenReturn(null);
        when(customerGateway.findFirstByCpf("")).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            findCustomerUseCase.execute((String) null);
        });

        assertThrows(EntityNotFoundException.class, () -> {
            findCustomerUseCase.execute("");
        });

        verify(customerGateway).findFirstByCpf(null);
        verify(customerGateway).findFirstByCpf("");
    }

    @Test
    @DisplayName("Deve tratar ID nulo")
    void shouldHandleNullId() {
        // Arrange
        when(customerGateway.findFirstById(null)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            findCustomerUseCase.execute((UUID) null);
        });

        verify(customerGateway).findFirstById(null);
    }
}
