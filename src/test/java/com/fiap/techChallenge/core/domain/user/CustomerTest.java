package com.fiap.techChallenge.core.domain.user;

import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Nested
    @DisplayName("Testes de Sucesso - Cliente Não Anônimo")
    class NonAnonymousCustomerSuccessTests {

        @Test
        @DisplayName("Deve criar um Customer não anônimo com dados válidos")
        void shouldCreateNonAnonymousCustomerWithValidData() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String email = "joao.silva@email.com";
            String cpfNumber = "12345678901";
            boolean anonymous = false;

            // Act
            Customer customer = Customer.build(id, name, email, cpfNumber, anonymous);

            // Assert
            assertNotNull(customer);
            assertEquals(id, customer.getId());
            assertEquals(name, customer.getName());
            assertEquals(email, customer.getEmail());
            assertFalse(customer.isAnonymous());
            assertNotNull(customer.getFormattedCpf());
            assertNotNull(customer.getUnformattedCpf());
        }

        @Test
        @DisplayName("Deve criar Customer não anônimo com CPF formatado")
        void shouldCreateNonAnonymousCustomerWithFormattedCPF() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "Maria Santos";
            String email = "maria.santos@email.com";
            String cpfNumber = "123.456.789-01";
            boolean anonymous = false;

            // Act
            Customer customer = Customer.build(id, name, email, cpfNumber, anonymous);

            // Assert
            assertNotNull(customer);
            assertEquals(name, customer.getName());
            assertEquals(email, customer.getEmail());
            assertEquals(cpfNumber, customer.getFormattedCpf());
            assertFalse(customer.isAnonymous());
        }

        @Test
        @DisplayName("Deve permitir alteração do nome do Customer não anônimo")
        void shouldAllowNameUpdateForNonAnonymousCustomer() {
            // Arrange
            UUID id = UUID.randomUUID();
            String originalName = "Ana Lima";
            String email = "ana.lima@email.com";
            String cpfNumber = "12345678901";
            String newName = "Ana Lima Santos";
            boolean anonymous = false;

            Customer customer = Customer.build(id, originalName, email, cpfNumber, anonymous);

            // Act
            customer.setName(newName);

            // Assert
            assertEquals(newName, customer.getName());
        }

        @Test
        @DisplayName("Deve permitir alteração do email do Customer não anônimo")
        void shouldAllowEmailUpdateForNonAnonymousCustomer() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "Carlos Oliveira";
            String originalEmail = "carlos@email.com";
            String cpfNumber = "12345678901";
            String newEmail = "carlos.oliveira@novodominio.com";
            boolean anonymous = false;

            Customer customer = Customer.build(id, name, originalEmail, cpfNumber, anonymous);

            // Act
            customer.setEmail(newEmail);

            // Assert
            assertEquals(newEmail, customer.getEmail());
        }

        @Test
        @DisplayName("Deve permitir alteração do CPF do Customer não anônimo")
        void shouldAllowCpfUpdateForNonAnonymousCustomer() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "Pedro Costa";
            String email = "pedro@email.com";
            String originalCpf = "12345678901";
            String newCpf = "98765432100";
            boolean anonymous = false;

            Customer customer = Customer.build(id, name, email, originalCpf, anonymous);

            // Act
            customer.setCpf(newCpf);

            // Assert
            assertNotNull(customer.getUnformattedCpf());
        }
    }

    @Nested
    @DisplayName("Testes de Sucesso - Cliente Anônimo")
    class AnonymousCustomerSuccessTests {

        @Test
        @DisplayName("Deve criar um Customer anônimo com dados mínimos")
        void shouldCreateAnonymousCustomerWithMinimalData() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "Cliente Anônimo";
            boolean anonymous = true;

            // Act
            Customer customer = Customer.build(id, name, null, null, anonymous);

            // Assert
            assertNotNull(customer);
            assertEquals(id, customer.getId());
            assertEquals(name, customer.getName());
            assertNull(customer.getEmail());
            assertTrue(customer.isAnonymous());
            assertNull(customer.getFormattedCpf());
            assertNull(customer.getUnformattedCpf());
        }

        @Test
        @DisplayName("Deve criar Customer anônimo mesmo com email fornecido")
        void shouldCreateAnonymousCustomerEvenWithEmailProvided() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "Cliente Anônimo";
            String email = "email@teste.com";
            boolean anonymous = true;

            // Act
            Customer customer = Customer.build(id, name, email, null, anonymous);

            // Assert
            assertNotNull(customer);
            assertTrue(customer.isAnonymous());
            assertNull(customer.getEmail());
        }

        @Test
        @DisplayName("Deve criar Customer anônimo mesmo com CPF fornecido")
        void shouldCreateAnonymousCustomerEvenWithCpfProvided() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "Cliente Anônimo";
            String cpf = "12345678901";
            boolean anonymous = true;

            // Act
            Customer customer = Customer.build(id, name, null, cpf, anonymous);

            // Assert
            assertNotNull(customer);
            assertTrue(customer.isAnonymous());
            assertNull(customer.getFormattedCpf());
            assertNull(customer.getUnformattedCpf());
        }

        @Test
        @DisplayName("Deve criar Customer anônimo com UUID nulo")
        void shouldCreateAnonymousCustomerWithNullId() {
            // Arrange
            String name = "Cliente Anônimo";
            boolean anonymous = true;

            // Act
            Customer customer = Customer.build(null, name, null, null, anonymous);

            // Assert
            assertNotNull(customer);
            assertNull(customer.getId());
            assertTrue(customer.isAnonymous());
        }
    }

    @Nested
    @DisplayName("Testes de Falha - Cliente Não Anônimo")
    class NonAnonymousCustomerFailureTests {

        @Test
        @DisplayName("Deve falhar ao criar Customer não anônimo com nome nulo")
        void shouldFailWhenNonAnonymousCustomerNameIsNull() {
            // Arrange
            UUID id = UUID.randomUUID();
            String email = "test@email.com";
            String cpfNumber = "12345678901";
            boolean anonymous = false;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Customer.build(id, null, email, cpfNumber, anonymous)
            );
            assertEquals("Nome obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Customer não anônimo com nome vazio")
        void shouldFailWhenNonAnonymousCustomerNameIsEmpty() {
            // Arrange
            UUID id = UUID.randomUUID();
            String email = "test@email.com";
            String cpfNumber = "12345678901";
            boolean anonymous = false;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Customer.build(id, "", email, cpfNumber, anonymous)
            );
            assertEquals("Nome obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Customer não anônimo com email nulo")
        void shouldFailWhenNonAnonymousCustomerEmailIsNull() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String cpfNumber = "12345678901";
            boolean anonymous = false;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Customer.build(id, name, null, cpfNumber, anonymous)
            );
            assertEquals("Email obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Customer não anônimo com email vazio")
        void shouldFailWhenNonAnonymousCustomerEmailIsEmpty() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String cpfNumber = "12345678901";
            boolean anonymous = false;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Customer.build(id, name, "", cpfNumber, anonymous)
            );
            assertEquals("Email obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Customer não anônimo com CPF nulo")
        void shouldFailWhenNonAnonymousCustomerCpfIsNull() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String email = "joao@email.com";
            boolean anonymous = false;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Customer.build(id, name, email, null, anonymous)
            );
            assertEquals("CPF obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Customer não anônimo com CPF vazio")
        void shouldFailWhenNonAnonymousCustomerCpfIsEmpty() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String email = "joao@email.com";
            boolean anonymous = false;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Customer.build(id, name, email, "", anonymous)
            );
            assertEquals("CPF obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Customer não anônimo com CPF inválido")
        void shouldFailWhenNonAnonymousCustomerCpfIsInvalid() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String email = "joao@email.com";
            String invalidCpf = "123456789";
            boolean anonymous = false;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Customer.build(id, name, email, invalidCpf, anonymous)
            );
            assertEquals("CPF inválido.", exception.getMessage());
        }
    }
}
