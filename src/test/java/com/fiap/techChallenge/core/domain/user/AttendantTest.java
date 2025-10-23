package com.fiap.techChallenge.core.domain.user;

import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AttendantTest {

    @Nested
    @DisplayName("Testes de Sucesso")
    class SuccessTests {

        @Test
        @DisplayName("Deve criar um Attendant com dados válidos")
        void shouldCreateAttendantWithValidData() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String email = "joao.silva@email.com";
            String cpfNumber = "12345678901";

            // Act
            Attendant attendant = Attendant.build(id, name, email, cpfNumber);

            // Assert
            assertNotNull(attendant);
            assertEquals(id, attendant.getId());
            assertEquals(name, attendant.getName());
            assertEquals(email, attendant.getEmail());
            assertEquals(cpfNumber, attendant.getUnformattedCpf());
        }

        @Test
        @DisplayName("Deve criar Attendant com CPF formatado")
        void shouldCreateAttendantWithFormattedCPF() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "Maria Santos";
            String email = "maria.santos@email.com";
            String cpfNumber = "123.456.789-01";

            // Act
            Attendant attendant = Attendant.build(id, name, email, cpfNumber);

            // Assert
            assertNotNull(attendant);
            assertEquals(name, attendant.getName());
            assertEquals(email, attendant.getEmail());
            assertEquals(cpfNumber, attendant.getFormattedCpf());
        }

        @Test
        @DisplayName("Deve criar Attendant com UUID nulo")
        void shouldCreateAttendantWithNullId() {
            // Arrange
            String name = "Pedro Costa";
            String email = "pedro.costa@email.com";
            String cpfNumber = "12345678901";

            // Act
            Attendant attendant = Attendant.build(null, name, email, cpfNumber);

            // Assert
            assertNotNull(attendant);
            assertNull(attendant.getId());
            assertEquals(name, attendant.getName());
            assertEquals(email, attendant.getEmail());
            assertEquals(cpfNumber, attendant.getUnformattedCpf());
        }

        @Test
        @DisplayName("Deve permitir alteração do nome")
        void shouldAllowNameUpdate() {
            // Arrange
            UUID id = UUID.randomUUID();
            String originalName = "Ana Lima";
            String email = "ana.lima@email.com";
            String cpfNumber = "12345678901";
            String newName = "Ana Lima Santos";

            Attendant attendant = Attendant.build(id, originalName, email, cpfNumber);

            // Act
            attendant.setName(newName);

            // Assert
            assertEquals(newName, attendant.getName());
        }

        @Test
        @DisplayName("Deve permitir alteração do email")
        void shouldAllowEmailUpdate() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "Carlos Oliveira";
            String originalEmail = "carlos@email.com";
            String cpfNumber = "12345678901";
            String newEmail = "carlos.oliveira@example.com";

            Attendant attendant = Attendant.build(id, name, originalEmail, cpfNumber);

            // Act
            attendant.setEmail(newEmail);

            // Assert
            assertEquals(newEmail, attendant.getEmail());
        }
    }

    @Nested
    @DisplayName("Testes de Falha")
    class FailureTests {

        @Test
        @DisplayName("Deve falhar ao criar Attendant com nome nulo")
        void shouldFailWhenNameIsNull() {
            // Arrange
            UUID id = UUID.randomUUID();
            String email = "test@email.com";
            String cpfNumber = "12345678901";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Attendant.build(id, null, email, cpfNumber)
            );
            assertEquals("Nome obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Attendant com nome vazio")
        void shouldFailWhenNameIsEmpty() {
            // Arrange
            UUID id = UUID.randomUUID();
            String email = "test@email.com";
            String cpfNumber = "12345678901";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Attendant.build(id, "", email, cpfNumber)
            );
            assertEquals("Nome obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Attendant com email nulo")
        void shouldFailWhenEmailIsNull() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String cpfNumber = "12345678901";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Attendant.build(id, name, null, cpfNumber)
            );
            assertEquals("Email obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Attendant com email vazio")
        void shouldFailWhenEmailIsEmpty() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String cpfNumber = "12345678901";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Attendant.build(id, name, "", cpfNumber)
            );
            assertEquals("Email obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Attendant com CPF nulo")
        void shouldFailWhenCpfIsNull() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String email = "joao@email.com";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Attendant.build(id, name, email, null)
            );
            assertEquals("CPF obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Attendant com CPF vazio")
        void shouldFailWhenCpfIsEmpty() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String email = "joao@email.com";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Attendant.build(id, name, email, "")
            );
            assertEquals("CPF obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("Deve falhar ao criar Attendant com CPF inválido")
        void shouldFailWhenCpfIsInvalid() {
            // Arrange
            UUID id = UUID.randomUUID();
            String name = "João Silva";
            String email = "joao@email.com";
            String invalidCpf = "123456789";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Attendant.build(id, name, email, invalidCpf)
            );
            assertEquals("CPF inválido.", exception.getMessage());
        }
    }
}
