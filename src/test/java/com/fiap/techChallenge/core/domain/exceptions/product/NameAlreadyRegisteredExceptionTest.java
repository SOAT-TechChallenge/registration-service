package com.fiap.techChallenge.core.domain.exceptions.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NameAlreadyRegisteredException - Testes")
class NameAlreadyRegisteredExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem formatada contendo o nome")
    void shouldCreateExceptionWithFormattedMessageContainingName() {
        // Given
        String productName = "Hambúrguer";

        // When
        NameAlreadyRegisteredException exception = new NameAlreadyRegisteredException(productName);

        // Then
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("já foi cadastrado"));
        assertTrue(exception.getMessage().contains(productName));
    }
}

