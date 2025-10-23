package com.fiap.techChallenge.core.application.useCases.attendant;

import com.fiap.techChallenge.core.application.useCases.user.attendant.FindAttendantUseCase;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindAttendantUseCase Tests")
class FindAttendantUseCaseTest {

    @Mock
    private AttendantGateway attendantGateway;

    @InjectMocks
    private FindAttendantUseCase findAttendantUseCase;

    private UUID validAttendantId;
    private String validCpf;
    private String invalidCpf;
    private Attendant mockAttendant;

    @BeforeEach
    void setUp() {
        validAttendantId = UUID.randomUUID();
        validCpf = "12345678901";
        invalidCpf = "99999999999";

        mockAttendant = Attendant.build(
                validAttendantId,
                "João Silva",
                "joao.silva@email.com",
                validCpf
        );
    }

    @Test
    @DisplayName("Deve encontrar attendant por CPF quando existe")
    void shouldFindAttendantByCpfWhenExists() {
        // Arrange
        when(attendantGateway.findFirstByCpf(validCpf)).thenReturn(mockAttendant);

        // Act
        Attendant result = findAttendantUseCase.execute(validCpf);

        // Assert
        assertNotNull(result);
        assertEquals(mockAttendant, result);
        assertEquals(validCpf, result.getUnformattedCpf());
        assertEquals("João Silva", result.getName());
        verify(attendantGateway).findFirstByCpf(validCpf);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando não encontra por CPF")
    void shouldThrowEntityNotFoundExceptionWhenNotFoundByCpf() {
        // Arrange
        when(attendantGateway.findFirstByCpf(invalidCpf)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> findAttendantUseCase.execute(invalidCpf)
        );

        assertEquals("Registro não encontrado: Attendant", exception.getMessage());
        verify(attendantGateway).findFirstByCpf(invalidCpf);
    }

    @Test
    @DisplayName("Deve encontrar attendant por ID quando existe")
    void shouldFindAttendantByIdWhenExists() {
        // Arrange
        when(attendantGateway.findFirstById(validAttendantId)).thenReturn(mockAttendant);

        // Act
        Attendant result = findAttendantUseCase.execute(validAttendantId);

        // Assert
        assertNotNull(result);
        assertEquals(mockAttendant, result);
        assertEquals(validAttendantId, result.getId());
        assertEquals("João Silva", result.getName());
        verify(attendantGateway).findFirstById(validAttendantId);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando não encontra por ID")
    void shouldThrowEntityNotFoundExceptionWhenNotFoundById() {
        // Arrange
        UUID invalidId = UUID.randomUUID();
        when(attendantGateway.findFirstById(invalidId)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> findAttendantUseCase.execute(invalidId)
        );

        assertEquals("Registro não encontrado: Attendant", exception.getMessage());
        verify(attendantGateway).findFirstById(invalidId);
    }

    @Test
    @DisplayName("Deve chamar gateway com CPF correto")
    void shouldCallGatewayWithCorrectCpf() {
        // Arrange
        String testCpf = "11111111111";
        when(attendantGateway.findFirstByCpf(testCpf)).thenReturn(mockAttendant);

        // Act
        findAttendantUseCase.execute(testCpf);

        // Assert
        verify(attendantGateway).findFirstByCpf(testCpf);
        verify(attendantGateway, never()).findFirstById(any(UUID.class));
    }

    @Test
    @DisplayName("Deve retornar o mesmo objeto Attendant retornado pelo gateway por ID")
    void shouldReturnSameAttendantObjectFromGatewayById() {
        // Arrange
        when(attendantGateway.findFirstById(validAttendantId)).thenReturn(mockAttendant);

        // Act
        Attendant result = findAttendantUseCase.execute(validAttendantId);

        // Assert
        assertSame(mockAttendant, result);
    }

    @Test
    @DisplayName("Deve tratar CPF nulo")
    void shouldHandleNullCpf() {
        // Arrange
        when(attendantGateway.findFirstByCpf(null)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            findAttendantUseCase.execute((String) null);
        });

        verify(attendantGateway).findFirstByCpf(null);
    }

    @Test
    @DisplayName("Deve tratar CPF vazio")
    void shouldHandleEmptyCpf() {
        // Arrange
        when(attendantGateway.findFirstByCpf("")).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            findAttendantUseCase.execute("");
        });

        verify(attendantGateway).findFirstByCpf("");
    }

    @Test
    @DisplayName("Deve tratar ID nulo")
    void shouldHandleNullId() {
        // Arrange
        when(attendantGateway.findFirstById(null)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            findAttendantUseCase.execute((UUID) null);
        });

        verify(attendantGateway).findFirstById(null);
    }
}
