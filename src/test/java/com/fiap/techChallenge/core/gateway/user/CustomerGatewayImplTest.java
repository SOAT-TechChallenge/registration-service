package com.fiap.techChallenge.core.gateway.user;

import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import com.fiap.techChallenge.core.gateways.user.CustomerGatewayImpl;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerGatewayImplTest {

    @Mock
    private CompositeDataSource compositeDataSource;

    private CustomerGatewayImpl customerGateway;

    private UUID testId;
    private String testName;
    private String testEmail;
    private String testCpf;
    private boolean testAnonymous;
    private Customer testCustomer;
    private CustomerFullDTO testCustomerDTO;

    @BeforeEach
    void setUp() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            customerGateway = new CustomerGatewayImpl(compositeDataSource);

            testId = UUID.randomUUID();
            testName = "João Silva";
            testEmail = "joao.silva@example.com";
            testCpf = "12345678901";
            testAnonymous = false;

            testCustomer = Customer.build(testId, testName, testEmail, testCpf, testAnonymous);
            testCustomerDTO = new CustomerFullDTO(testId, testName, testCpf, testEmail, testAnonymous);
        }
    }

    @Test
    @DisplayName("Deve retornar cliente quando um cliente válido for fornecido")
    void saveShouldReturnCustomerWhenValidCustomerProvided() {
        // Arrange
        when(compositeDataSource.saveCustomer(any(CustomerFullDTO.class))).thenReturn(testCustomerDTO);

        // Act
        Customer result = customerGateway.save(testCustomer);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testName, result.getName());
        assertEquals(testEmail, result.getEmail());
        assertEquals(testCpf, result.getUnformattedCpf());
        assertEquals(testAnonymous, result.isAnonymous());

        verify(compositeDataSource, times(1)).saveCustomer(any(CustomerFullDTO.class));
    }

    @Test
    @DisplayName("Deve chamar o DataSource com o DTO correto")
    void saveShouldCallDataSourceWithCorrectDTO() {
        // Arrange
        when(compositeDataSource.saveCustomer(any(CustomerFullDTO.class))).thenReturn(testCustomerDTO);

        // Act
        customerGateway.save(testCustomer);

        // Assert
        verify(compositeDataSource, times(1)).saveCustomer(argThat(dto ->
                dto.id().equals(testId) &&
                dto.name().equals(testName) &&
                dto.email().equals(testEmail) &&
                dto.cpf().equals(testCpf) &&
                dto.anonymous() == testAnonymous
        ));
    }

    @Test
    @DisplayName("Deve retornar cliente quando o cliente existir por ID")
    void findFirstByIdShouldReturnCustomerWhenCustomerExists() {
        // Arrange
        when(compositeDataSource.findFirstCustomerById(testId)).thenReturn(testCustomerDTO);

        // Act
        Customer result = customerGateway.findFirstById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testName, result.getName());
        assertEquals(testEmail, result.getEmail());
        assertEquals(testCpf, result.getUnformattedCpf());
        assertEquals(testAnonymous, result.isAnonymous());

        verify(compositeDataSource, times(1)).findFirstCustomerById(testId);
    }

    @Test
    @DisplayName("Deve retornar nulo quando o cliente não existir por ID")
    void findFirstByIdShouldReturnNullWhenCustomerNotExists() {
        // Arrange
        when(compositeDataSource.findFirstCustomerById(testId)).thenReturn(null);

        // Act
        Customer result = customerGateway.findFirstById(testId);

        // Assert
        assertNull(result);
        verify(compositeDataSource, times(1)).findFirstCustomerById(testId);
    }

    @Test
    @DisplayName("Deve retornar cliente quando o cliente existir por CPF")
    void findFirstByCpfShouldReturnCustomerWhenCustomerExists() {
        // Arrange
        when(compositeDataSource.findFirstCustomerByCpf(testCpf)).thenReturn(testCustomerDTO);

        // Act
        Customer result = customerGateway.findFirstByCpf(testCpf);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testName, result.getName());
        assertEquals(testEmail, result.getEmail());
        assertEquals(testCpf, result.getUnformattedCpf());
        assertEquals(testAnonymous, result.isAnonymous());

        verify(compositeDataSource, times(1)).findFirstCustomerByCpf(testCpf);
    }

    @Test
    @DisplayName("Deve retornar nulo quando o cliente não existir por CPF")
    void findFirstByCpfShouldReturnNullWhenCustomerNotExists() {
        // Arrange
        when(compositeDataSource.findFirstCustomerByCpf(testCpf)).thenReturn(null);

        // Act
        Customer result = customerGateway.findFirstByCpf(testCpf);

        // Assert
        assertNull(result);
        verify(compositeDataSource, times(1)).findFirstCustomerByCpf(testCpf);
    }

    @Test
    @DisplayName("Deve retornar lista de clientes não anônimos quando existirem")
    void findAllNotAnonymousShouldReturnListOfCustomersWhenCustomersExist() {
        // Arrange
        UUID secondId = UUID.randomUUID();
        CustomerFullDTO secondDTO = new CustomerFullDTO(secondId, "Maria Santos", "98765432100", "maria.santos@example.com", false);
        List<CustomerFullDTO> customerDTOList = Arrays.asList(testCustomerDTO, secondDTO);

        when(compositeDataSource.findAllCustomerNotAnonym()).thenReturn(customerDTOList);

        // Act
        List<Customer> result = customerGateway.findAllNotAnonymous();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        Customer firstCustomer = result.get(0);
        assertEquals(testId, firstCustomer.getId());
        assertEquals(testName, firstCustomer.getName());
        assertEquals(testEmail, firstCustomer.getEmail());
        assertEquals(testCpf, firstCustomer.getUnformattedCpf());
        assertEquals(testAnonymous, firstCustomer.isAnonymous());

        Customer secondCustomer = result.get(1);
        assertEquals(secondId, secondCustomer.getId());
        assertEquals("Maria Santos", secondCustomer.getName());
        assertEquals("maria.santos@example.com", secondCustomer.getEmail());
        assertEquals("98765432100", secondCustomer.getUnformattedCpf());
        assertFalse(secondCustomer.isAnonymous());

        verify(compositeDataSource, times(1)).findAllCustomerNotAnonym();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existirem clientes não anônimos")
    void findAllNotAnonymousShouldReturnEmptyListWhenNoCustomersExist() {
        // Arrange
        when(compositeDataSource.findAllCustomerNotAnonym()).thenReturn(List.of());

        // Act
        List<Customer> result = customerGateway.findAllNotAnonymous();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(compositeDataSource, times(1)).findAllCustomerNotAnonym();
    }

    @Test
    @DisplayName("Deve chamar o método delete do DataSource")
    void deleteShouldCallDataSourceDelete() {
        // Act
        customerGateway.delete(testId);

        // Assert
        verify(compositeDataSource, times(1)).deleteCustomer(testId);
    }

    @Test
    @DisplayName("Deve inicializar o construtor com DataSource")
    void constructorShouldInitializeWithDataSource() {
        // Arrange & Act
        CustomerGatewayImpl gateway = new CustomerGatewayImpl(compositeDataSource);

        // Assert
        assertNotNull(gateway);
    }
}
