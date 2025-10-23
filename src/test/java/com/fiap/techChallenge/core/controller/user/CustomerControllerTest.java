package com.fiap.techChallenge.core.controller.user;

import com.fiap.techChallenge.core.application.dto.user.CustomerAnonymDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerInputDTO;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CustomerController")
class CustomerControllerTest {

    @Mock
    private CompositeDataSource compositeDataSource;

    private CustomerController customerController;
    private CustomerInputDTO customerInputDTO;
    private CustomerInputDTO anonymousCustomerInputDTO;
    private CustomerFullDTO customerDTO;
    private CustomerFullDTO anonymousCustomer;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerController = CustomerController.build(compositeDataSource);

        customerId = UUID.randomUUID();

        customerInputDTO = new CustomerInputDTO(
                "João Silva",
                "joao.silva@email.com",
                "123.456.789-01",
                false
        );

        anonymousCustomerInputDTO = new CustomerInputDTO(
                null,
                null,
                null,
                true
        );

        customerDTO = mock(CustomerFullDTO.class);
        anonymousCustomer = mock(CustomerFullDTO.class);
    }

    @Test
    @DisplayName("Deve construir CustomerController com sucesso")
    void shouldBuildCustomerControllerSuccessfully() {
        // When
        CustomerController controller = CustomerController.build(compositeDataSource);

        // Then
        assertNotNull(controller);
    }

    @Test
    @DisplayName("Deve criar cliente com sucesso")
    void shouldCreateNonAnonymousCustomerSuccessfully() {
        // Given
        when(customerDTO.name()).thenReturn("João Silva");
        when(customerDTO.email()).thenReturn("joao.silva@email.com");
        when(customerDTO.cpf()).thenReturn("123.456.789-01");
        when(customerDTO.id()).thenReturn(customerId);
        when(customerDTO.anonymous()).thenReturn(false);

        when(compositeDataSource.saveCustomer(any(CustomerFullDTO.class))).thenReturn(customerDTO);
        when(compositeDataSource.findFirstCustomerByCpf(anyString())).thenReturn(null);

        // When
        CustomerFullDTO result = (CustomerFullDTO) customerController.create(customerInputDTO);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.id());
        assertEquals("João Silva", result.name());
        assertEquals("joao.silva@email.com", result.email());
        assertEquals("123.456.789-01", result.cpf());

        verify(compositeDataSource, times(1)).saveCustomer(any(CustomerFullDTO.class));
    }

    @Test
    @DisplayName("Deve criar cliente anônimo com sucesso")
    void shouldCreateAnonymousCustomerSuccessfully() {
        // Given
        when(anonymousCustomer.id()).thenReturn(customerId);
        when(anonymousCustomer.anonymous()).thenReturn(true);
        when(anonymousCustomer.name()).thenReturn(null);
        when(anonymousCustomer.email()).thenReturn(null);
        when(anonymousCustomer.cpf()).thenReturn(null);

        when(compositeDataSource.saveCustomer(any(CustomerFullDTO.class))).thenReturn(anonymousCustomer);

        // When
        CustomerAnonymDTO result = (CustomerAnonymDTO) customerController.create(anonymousCustomerInputDTO);

        // Then
        assertNotNull(result);
        assertNotNull(result.id());

        verify(compositeDataSource, times(1)).saveCustomer(any(CustomerFullDTO.class));
    }

    @Test
    @DisplayName("Deve atualizar cliente com sucesso")
    void shouldUpdateNonAnonymousCustomerSuccessfully() {
        // Given
        when(customerDTO.name()).thenReturn("João Silva");
        when(customerDTO.email()).thenReturn("joao.silva@email.com");
        when(customerDTO.cpf()).thenReturn("123.456.789-01");
        when(customerDTO.id()).thenReturn(customerId);
        when(customerDTO.anonymous()).thenReturn(false);

        when(compositeDataSource.findFirstCustomerById(customerId)).thenReturn(customerDTO);
        when(compositeDataSource.saveCustomer(any(CustomerFullDTO.class))).thenReturn(customerDTO);

        // When
        CustomerFullDTO result = (CustomerFullDTO) customerController.update(customerId, customerInputDTO);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.id());
        assertEquals("João Silva", result.name());
        assertEquals("joao.silva@email.com", result.email());
        assertEquals("123.456.789-01", result.cpf());

        verify(compositeDataSource, times(1)).findFirstCustomerById(customerId);
        verify(compositeDataSource, times(1)).saveCustomer(any(CustomerFullDTO.class));
    }

    @Test
    @DisplayName("Deve atualizar cliente anônimo com sucesso")
    void shouldUpdateAnonymousCustomerSuccessfully() {
        // Given
        when(anonymousCustomer.id()).thenReturn(customerId);
        when(anonymousCustomer.anonymous()).thenReturn(true);
        when(anonymousCustomer.name()).thenReturn(null);
        when(anonymousCustomer.email()).thenReturn(null);
        when(anonymousCustomer.cpf()).thenReturn(null);

        UUID anonymousId = UUID.randomUUID();
        when(compositeDataSource.findFirstCustomerById(anonymousId)).thenReturn(anonymousCustomer);
        when(compositeDataSource.saveCustomer(any(CustomerFullDTO.class))).thenReturn(anonymousCustomer);

        // When
        CustomerAnonymDTO result = (CustomerAnonymDTO) customerController.update(anonymousId, anonymousCustomerInputDTO);

        // Then
        assertNotNull(result);
        assertNotNull(result.id());

        verify(compositeDataSource, times(1)).findFirstCustomerById(anonymousId);
        verify(compositeDataSource, times(1)).saveCustomer(any(CustomerFullDTO.class));
    }

    @Test
    @DisplayName("Deve encontrar cliente por CPF com sucesso")
    void shouldFindNonAnonymousCustomerByCpfSuccessfully() {
        // Given
        when(customerDTO.name()).thenReturn("João Silva");
        when(customerDTO.email()).thenReturn("joao.silva@email.com");
        when(customerDTO.cpf()).thenReturn("123.456.789-01");
        when(customerDTO.id()).thenReturn(customerId);
        when(customerDTO.anonymous()).thenReturn(false);

        String cpf = "12345678901";
        when(compositeDataSource.findFirstCustomerByCpf(cpf)).thenReturn(customerDTO);

        // When
        CustomerFullDTO result = (CustomerFullDTO) customerController.findByCpf(cpf);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.id());
        assertEquals("João Silva", result.name());
        assertEquals("joao.silva@email.com", result.email());
        assertEquals("123.456.789-01", result.cpf());

        verify(compositeDataSource, times(1)).findFirstCustomerByCpf(cpf);
    }

    @Test
    @DisplayName("Deve encontrar cliente anônimo por CPF com sucesso")
    void shouldFindAnonymousCustomerByCpfSuccessfully() {
        // Given
        when(anonymousCustomer.id()).thenReturn(customerId);
        when(anonymousCustomer.anonymous()).thenReturn(true);
        when(anonymousCustomer.name()).thenReturn(null);
        when(anonymousCustomer.email()).thenReturn(null);
        when(anonymousCustomer.cpf()).thenReturn(null);

        String cpf = "anonymous";
        when(compositeDataSource.findFirstCustomerByCpf(cpf)).thenReturn(anonymousCustomer);

        // When
        CustomerAnonymDTO result = (CustomerAnonymDTO) customerController.findByCpf(cpf);

        // Then
        assertNotNull(result);
        assertNotNull(result.id());

        verify(compositeDataSource, times(1)).findFirstCustomerByCpf(cpf);
    }

    @Test
    @DisplayName("Deve encontrar cliente por ID com sucesso")
    void shouldFindNonAnonymousCustomerByIdSuccessfully() {
        // Given
        when(customerDTO.name()).thenReturn("João Silva");
        when(customerDTO.email()).thenReturn("joao.silva@email.com");
        when(customerDTO.cpf()).thenReturn("123.456.789-01");
        when(customerDTO.id()).thenReturn(customerId);
        when(customerDTO.anonymous()).thenReturn(false);

        when(compositeDataSource.findFirstCustomerById(customerId)).thenReturn(customerDTO);

        // When
        CustomerFullDTO result = (CustomerFullDTO) customerController.findById(customerId);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.id());
        assertEquals("João Silva", result.name());
        assertEquals("joao.silva@email.com", result.email());
        assertEquals("123.456.789-01", result.cpf());

        verify(compositeDataSource, times(1)).findFirstCustomerById(customerId);
    }

    @Test
    @DisplayName("Deve encontrar cliente anônimo por ID com sucesso")
    void shouldFindAnonymousCustomerByIdSuccessfully() {
        // Given
        when(anonymousCustomer.id()).thenReturn(customerId);
        when(anonymousCustomer.anonymous()).thenReturn(true);
        when(anonymousCustomer.name()).thenReturn(null);
        when(anonymousCustomer.email()).thenReturn(null);
        when(anonymousCustomer.cpf()).thenReturn(null);

        UUID anonymousId = UUID.randomUUID();
        when(compositeDataSource.findFirstCustomerById(anonymousId)).thenReturn(anonymousCustomer);

        // When
        CustomerAnonymDTO result = (CustomerAnonymDTO) customerController.findById(anonymousId);

        // Then
        assertNotNull(result);
        assertNotNull(result.id());

        verify(compositeDataSource, times(1)).findFirstCustomerById(anonymousId);
    }

    @Test
    @DisplayName("Deve listar clientes com sucesso")
    void shouldListNotAnonymCustomersSuccessfully() {
        // Given
        when(customerDTO.name()).thenReturn("João Silva");
        when(customerDTO.email()).thenReturn("joao.silva@email.com");
        when(customerDTO.cpf()).thenReturn("123.456.789-01");
        when(customerDTO.id()).thenReturn(customerId);
        when(customerDTO.anonymous()).thenReturn(false);

        CustomerFullDTO customerDto2 = mock(CustomerFullDTO.class);
        when(customerDto2.id()).thenReturn(UUID.randomUUID());
        when(customerDto2.name()).thenReturn("Maria Santos");
        when(customerDto2.email()).thenReturn("maria.santos@email.com");
        when(customerDto2.cpf()).thenReturn("987.654.321-09");
        when(customerDto2.anonymous()).thenReturn(false);

        List<CustomerFullDTO> customers = Arrays.asList(customerDTO, customerDto2);
        when(compositeDataSource.findAllCustomerNotAnonym()).thenReturn(customers);

        // When
        List<CustomerFullDTO> result = customerController.listNotAnonym();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        CustomerFullDTO firstCustomer = result.get(0);
        assertEquals(customerId, firstCustomer.id());
        assertEquals("João Silva", firstCustomer.name());
        assertEquals("joao.silva@email.com", firstCustomer.email());
        assertEquals("123.456.789-01", firstCustomer.cpf());

        verify(compositeDataSource, times(1)).findAllCustomerNotAnonym();
    }
}
