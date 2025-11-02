package com.fiap.techChallenge.core.domain.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DomainException - Testes")
class DomainExceptionTest {

    static class ConcreteDomainException extends DomainException {
        public ConcreteDomainException(String message) {
            super(message);
        }
    }

    @Test
    @DisplayName("Deve criar exceção com mensagem não nula")
    void shouldCreateExceptionWithNonNullMessage() {
        // Given
        String message = "Mensagem de erro";

        // When
        ConcreteDomainException exception = new ConcreteDomainException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar NullPointerException quando mensagem é nula")
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        // When/Then
        assertThrows(NullPointerException.class, () -> {
            new ConcreteDomainException(null);
        });
    }
}

