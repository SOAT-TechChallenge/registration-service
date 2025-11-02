package com.fiap.techChallenge.core.domain.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EntityNotFoundException - Testes")
class EntityNotFoundExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem formatada")
    void shouldCreateExceptionWithFormattedMessage() {
        // Given
        String entityName = "Produto";

        // When
        EntityNotFoundException exception = new EntityNotFoundException(entityName);

        // Then
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Registro não encontrado"));
        assertTrue(exception.getMessage().contains(entityName));
    }
}

