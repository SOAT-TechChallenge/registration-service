package com.fiap.techChallenge.core.application.useCases.attendant;

import com.fiap.techChallenge.core.application.useCases.user.attendant.DeleteAttendantUseCase;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.gateways.user.AttendantGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteAttendantUseCase Tests")
class DeleteAttendantUseCaseTest {

    @Mock
    private AttendantGateway attendantGateway;

    @InjectMocks
    private DeleteAttendantUseCase deleteAttendantUseCase;

    private String validCpf;
    private String invalidCpf;
    private Attendant mockAttendant;
    private UUID attendantId;

    @BeforeEach
    void setUp() {
        validCpf = "12345678901";
        invalidCpf = "99999999999";
        attendantId = UUID.randomUUID();

        mockAttendant = Attendant.build(
                attendantId,
                "João Silva",
                "joao.silva@email.com",
                validCpf
        );
    }

    @Test
    @DisplayName("Deve deletar attendant com sucesso quando CPF existe")
    void shouldDeleteAttendantSuccessfullyWhenCpfExists() {
        // Arrange
        when(attendantGateway.findFirstByCpf(validCpf)).thenReturn(mockAttendant);
        doNothing().when(attendantGateway).delete(attendantId);

        // Act
        assertDoesNotThrow(() -> deleteAttendantUseCase.execute(validCpf));

        // Assert
        verify(attendantGateway).findFirstByCpf(validCpf);
        verify(attendantGateway).delete(attendantId);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando CPF não existe")
    void shouldThrowEntityNotFoundExceptionWhenCpfDoesNotExist() {
        // Arrange
        when(attendantGateway.findFirstByCpf(invalidCpf)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> deleteAttendantUseCase.execute(invalidCpf)
        );

        assertEquals("Registro não encontrado: Attendant", exception.getMessage());
        verify(attendantGateway).findFirstByCpf(invalidCpf);
        verify(attendantGateway, never()).delete(any(UUID.class));
    }

    @Test
    @DisplayName("Deve chamar delete com ID correto do attendant encontrado")
    void shouldCallDeleteWithCorrectAttendantId() {
        // Arrange
        when(attendantGateway.findFirstByCpf(validCpf)).thenReturn(mockAttendant);
        doNothing().when(attendantGateway).delete(attendantId);

        // Act
        deleteAttendantUseCase.execute(validCpf);

        // Assert
        verify(attendantGateway).delete(attendantId);
        verify(attendantGateway).delete(mockAttendant.getId());
    }

    @Test
    @DisplayName("Deve tratar CPF nulo")
    void shouldHandleNullCpf() {
        // Arrange
        when(attendantGateway.findFirstByCpf(null)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            deleteAttendantUseCase.execute(null);
        });

        verify(attendantGateway).findFirstByCpf(null);
        verify(attendantGateway, never()).delete(any(UUID.class));
    }

    @Test
    @DisplayName("Deve tratar CPF vazio")
    void shouldHandleEmptyCpf() {
        // Arrange
        String emptyCpf = "";
        when(attendantGateway.findFirstByCpf(emptyCpf)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            deleteAttendantUseCase.execute(emptyCpf);
        });

        verify(attendantGateway).findFirstByCpf(emptyCpf);
        verify(attendantGateway, never()).delete(any(UUID.class));
    }

    @Test
    @DisplayName("Deve verificar que método execute não retorna valor")
    void shouldVerifyExecuteMethodReturnsVoid() {
        // Arrange
        when(attendantGateway.findFirstByCpf(validCpf)).thenReturn(mockAttendant);
        doNothing().when(attendantGateway).delete(attendantId);

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleteAttendantUseCase.execute(validCpf);
        });
    }
}
