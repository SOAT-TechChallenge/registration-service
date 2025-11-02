package com.fiap.techChallenge.core.domain.exceptions.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserAlreadyExistsException - Testes")
class UserAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem padrão")
    void shouldCreateExceptionWithDefaultMessage() {
        // When
        UserAlreadyExistsException exception = new UserAlreadyExistsException();

        // Then
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals("Este usuário já existe.", exception.getMessage());
    }
}

