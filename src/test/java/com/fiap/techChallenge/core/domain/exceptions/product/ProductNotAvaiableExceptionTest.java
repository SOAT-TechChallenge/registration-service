package com.fiap.techChallenge.core.domain.exceptions.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductNotAvaiableException - Testes")
class ProductNotAvaiableExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem padrão")
    void shouldCreateExceptionWithDefaultMessage() {
        // When
        ProductNotAvaiableException exception = new ProductNotAvaiableException();

        // Then
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals("O produto informado não está disponivel", exception.getMessage());
    }
}

