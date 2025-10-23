package com.fiap.techChallenge.core.application.useCases.customer;

import com.fiap.techChallenge.core.application.useCases.user.customer.ListNotAnonymCustomersUseCase;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import com.fiap.techChallenge.core.gateways.user.CustomerGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListNotAnonymCustomersUseCase Tests")
class ListNotAnonymCustomersUseCaseTest {

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private ListNotAnonymCustomersUseCase listNotAnonymCustomersUseCase;

    private List<Customer> mockCustomersList;
    private Customer customer1;
    private Customer customer2;
    private Customer customer3;

    @BeforeEach
    void setUp() {
        customer1 = Customer.build(
                UUID.randomUUID(),
                "João Silva",
                "joao.silva@email.com",
                "12345678901",
                false
        );

        customer2 = Customer.build(
                UUID.randomUUID(),
                "Maria Santos",
                "maria.santos@email.com",
                "98765432100",
                false
        );

        customer3 = Customer.build(
                UUID.randomUUID(),
                "Pedro Oliveira",
                "pedro.oliveira@email.com",
                "11122233344",
                false
        );

        mockCustomersList = List.of(customer1, customer2, customer3);
    }

    @Test
    @DisplayName("Deve retornar lista de clientes não anônimos quando existem clientes")
    void shouldReturnListOfNotAnonymousCustomersWhenCustomersExist() {
        // Arrange
        when(customerGateway.findAllNotAnonymous()).thenReturn(mockCustomersList);

        // Act
        List<Customer> result = listNotAnonymCustomersUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(mockCustomersList, result);
        assertTrue(result.contains(customer1));
        assertTrue(result.contains(customer2));
        assertTrue(result.contains(customer3));
        verify(customerGateway).findAllNotAnonymous();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem clientes não anônimos")
    void shouldReturnEmptyListWhenNoNotAnonymousCustomersExist() {
        // Arrange
        when(customerGateway.findAllNotAnonymous()).thenReturn(Collections.emptyList());

        // Act
        List<Customer> result = listNotAnonymCustomersUseCase.execute();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(customerGateway).findAllNotAnonymous();
    }

    @Test
    @DisplayName("Deve retornar lista com um único cliente")
    void shouldReturnListWithSingleCustomer() {
        // Arrange
        List<Customer> singleCustomerList = List.of(customer1);
        when(customerGateway.findAllNotAnonymous()).thenReturn(singleCustomerList);

        // Act
        List<Customer> result = listNotAnonymCustomersUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customer1, result.get(0));
        assertFalse(result.get(0).isAnonymous());
        verify(customerGateway).findAllNotAnonymous();
    }

    @Test
    @DisplayName("Deve garantir que todos os clientes retornados não são anônimos")
    void shouldEnsureAllReturnedCustomersAreNotAnonymous() {
        // Arrange
        when(customerGateway.findAllNotAnonymous()).thenReturn(mockCustomersList);

        // Act
        List<Customer> result = listNotAnonymCustomersUseCase.execute();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        result.forEach(customer -> {
            assertFalse(customer.isAnonymous(),
                "Cliente " + customer.getName() + " não deveria ser anônimo");
            assertNotNull(customer.getName());
            assertNotNull(customer.getEmail());
            assertNotNull(customer.getUnformattedCpf());
        });
    }

    @Test
    @DisplayName("Deve funcionar com lista grande de clientes")
    void shouldWorkWithLargeListOfCustomers() {
        // Arrange
        List<Customer> largeCustomersList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Customer customer = Customer.build(
                    UUID.randomUUID(),
                    "Cliente " + i,
                    "cliente" + i + "@email.com",
                    String.format("%011d", i),
                    false
            );
            largeCustomersList.add(customer);
        }

        when(customerGateway.findAllNotAnonymous()).thenReturn(largeCustomersList);

        // Act
        List<Customer> result = listNotAnonymCustomersUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(100, result.size());
        assertEquals(largeCustomersList, result);
        verify(customerGateway).findAllNotAnonymous();
    }

    @Test
    @DisplayName("Deve manter ordem dos clientes retornada pelo gateway")
    void shouldMaintainOrderOfCustomersFromGateway() {
        // Arrange
        List<Customer> orderedList = List.of(customer3, customer1, customer2);
        when(customerGateway.findAllNotAnonymous()).thenReturn(orderedList);

        // Act
        List<Customer> result = listNotAnonymCustomersUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(customer3, result.get(0));
        assertEquals(customer1, result.get(1));
        assertEquals(customer2, result.get(2));
        verify(customerGateway).findAllNotAnonymous();
    }
}
