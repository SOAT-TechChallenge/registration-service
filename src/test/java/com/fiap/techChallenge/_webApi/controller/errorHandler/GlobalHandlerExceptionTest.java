package com.fiap.techChallenge._webApi.controller.errorHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.domain.exceptions.product.NameAlreadyRegisteredException;
import com.fiap.techChallenge.core.domain.exceptions.product.ProductNotAvaiableException;
import com.fiap.techChallenge.core.domain.exceptions.user.UserAlreadyExistsException;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalHandlerException - Testes")
class GlobalHandlerExceptionTest {

    @InjectMocks
    private GlobalHandlerException globalHandlerException;

    @Test
    @DisplayName("Deve tratar EntityNotFoundException")
    void shouldHandleEntityNotFoundException() {
        // Given
        EntityNotFoundException ex = new EntityNotFoundException("Entidade");

        // When
        ResponseEntity<ErrorResponse> response = globalHandlerException.handleExceptions(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("Registro não encontrado"));
        assertTrue(response.getBody().getMessage().contains("Entidade"));
    }

    @Test
    @DisplayName("Deve tratar IllegalArgumentException")
    void shouldHandleIllegalArgumentException() {
        // Given
        IllegalArgumentException ex = new IllegalArgumentException("Argumento ilegal");

        // When
        ResponseEntity<ErrorResponse> response = globalHandlerException.handleExceptions(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Argumento ilegal", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Deve tratar NameAlreadyRegisteredException")
    void shouldHandleNameAlreadyRegisteredException() {
        // Given
        NameAlreadyRegisteredException ex = new NameAlreadyRegisteredException("Produto duplicado");

        // When
        ResponseEntity<ErrorResponse> response = globalHandlerException.handleExceptions(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve tratar UserAlreadyExistsException")
    void shouldHandleUserAlreadyExistsException() {
        // Given
        UserAlreadyExistsException ex = new UserAlreadyExistsException();

        // When
        ResponseEntity<ErrorResponse> response = globalHandlerException.handleExceptions(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve tratar ProductNotAvaiableException")
    void shouldHandleProductNotAvaiableException() {
        // Given
        ProductNotAvaiableException ex = new ProductNotAvaiableException();

        // When
        ResponseEntity<ErrorResponse> response = globalHandlerException.handleExceptions(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve tratar NullPointerException")
    void shouldHandleNullPointerException() {
        // Given
        NullPointerException ex = new NullPointerException("Valor nulo");

        // When
        ResponseEntity<ErrorResponse> response = globalHandlerException.handleExceptions(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve tratar SQLIntegrityConstraintViolationException com mensagem de duplicata")
    void shouldHandleSQLIntegrityConstraintViolationExceptionWithDuplicate() {
        // Given
        SQLIntegrityConstraintViolationException ex = 
                new SQLIntegrityConstraintViolationException("Duplicate entry 'test@email.com' for key 'email'");

        // When
        ResponseEntity<ErrorResponse> response = 
                globalHandlerException.handleSQLIntegrityConstraintViolationException(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Duplicate entry"));
    }

    @Test
    @DisplayName("Deve tratar SQLIntegrityConstraintViolationException sem padrão de duplicata")
    void shouldHandleSQLIntegrityConstraintViolationExceptionWithoutDuplicate() {
        // Given
        SQLIntegrityConstraintViolationException ex = 
                new SQLIntegrityConstraintViolationException("Integrity constraint violation");

        // When
        ResponseEntity<ErrorResponse> response = 
                globalHandlerException.handleSQLIntegrityConstraintViolationException(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException")
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "Erro de validação");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        // When
        ResponseEntity<java.util.HashMap<String, Object>> response = 
                globalHandlerException.handleValidation(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().get("status"));
    }

    @Test
    @DisplayName("Deve tratar HttpMessageNotReadableException com enum inválido")
    void shouldHandleHttpMessageNotReadableExceptionWithInvalidEnum() {
        // Given
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Mensagem", new RuntimeException());

        // When
        ResponseEntity<ErrorResponse> response = 
                globalHandlerException.handleInvalidEnumValueException(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve tratar HttpMessageNotReadableException sem enum inválido")
    void shouldHandleHttpMessageNotReadableExceptionWithoutInvalidEnum() {
        // Given
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Mensagem de erro");

        // When
        ResponseEntity<ErrorResponse> response = 
                globalHandlerException.handleInvalidEnumValueException(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentTypeMismatchException com enum inválido")
    void shouldHandleMethodArgumentTypeMismatchExceptionWithInvalidEnum() {
        // Given
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getRequiredType()).thenReturn((Class) com.fiap.techChallenge.core.domain.enums.Category.class);
        when(ex.getName()).thenReturn("category");
        when(ex.getValue()).thenReturn("INVALIDO");

        // When
        ResponseEntity<ErrorResponse> response = 
                globalHandlerException.handleEnumPathVariableException(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentTypeMismatchException com UUID inválido")
    void shouldHandleMethodArgumentTypeMismatchExceptionWithInvalidUUID() {
        // Given
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getRequiredType()).thenAnswer(invocation -> java.util.UUID.class);
        when(ex.getName()).thenReturn("id");
        when(ex.getValue()).thenReturn("invalid-uuid");

        // When
        ResponseEntity<ErrorResponse> response = 
                globalHandlerException.handleEnumPathVariableException(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentTypeMismatchException genérico")
    void shouldHandleMethodArgumentTypeMismatchExceptionGeneric() {
        // Given
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getRequiredType()).thenAnswer(invocation -> String.class);
        when(ex.getName()).thenReturn("param");

        // When
        ResponseEntity<ErrorResponse> response = 
                globalHandlerException.handleEnumPathVariableException(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Parâmetro inválido.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Deve tratar PropertyValueException")
    void shouldHandlePropertyValueException() {
        // Given
        PropertyValueException ex = mock(PropertyValueException.class);
        when(ex.getPropertyName()).thenReturn("nome");

        // When
        ResponseEntity<String> response = globalHandlerException.handlePropertyValueException(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("nome"));
    }
}

