package com.fiap.techChallenge.core.application.useCases.attendant;

import com.fiap.techChallenge.core.application.dto.user.AttendantInputDTO;
import com.fiap.techChallenge.core.application.useCases.user.attendant.UpdateAttendantUseCase;
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
@DisplayName("UpdateAttendantUseCase Tests")
class UpdateAttendantUseCaseTest {

    @Mock
    private AttendantGateway attendantGateway;

    @InjectMocks
    private UpdateAttendantUseCase updateAttendantUseCase;

    private UUID validAttendantId;
    private UUID invalidAttendantId;
    private AttendantInputDTO updateAttendantInputDTO;
    private Attendant existingAttendant;
    private Attendant updatedAttendant;

    @BeforeEach
    void setUp() {
        validAttendantId = UUID.randomUUID();
        invalidAttendantId = UUID.randomUUID();

        updateAttendantInputDTO = new AttendantInputDTO(
                "João Silva Atualizado",
                "joao.silva.novo@email.com",
                "12345678901"
        );

        existingAttendant = Attendant.build(
                validAttendantId,
                "João Silva",
                "joao.silva@email.com",
                "12345678901"
        );

        updatedAttendant = Attendant.build(
                validAttendantId,
                "João Silva Atualizado",
                "joao.silva.novo@email.com",
                "12345678901"
        );
    }

    @Test
    @DisplayName("Deve atualizar attendant com sucesso quando attendant existe")
    void shouldUpdateAttendantSuccessfullyWhenAttendantExists() {
        // Arrange
        when(attendantGateway.findFirstById(validAttendantId)).thenReturn(existingAttendant);
        when(attendantGateway.save(any(Attendant.class))).thenReturn(updatedAttendant);

        // Act
        Attendant result = updateAttendantUseCase.execute(validAttendantId, updateAttendantInputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(validAttendantId, result.getId());
        assertEquals("João Silva Atualizado", result.getName());
        assertEquals("joao.silva.novo@email.com", result.getEmail());
        assertEquals("12345678901", result.getUnformattedCpf());

        verify(attendantGateway).findFirstById(validAttendantId);
        verify(attendantGateway).save(any(Attendant.class));
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando attendant não existe")
    void shouldThrowEntityNotFoundExceptionWhenAttendantDoesNotExist() {
        // Arrange
        when(attendantGateway.findFirstById(invalidAttendantId)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> updateAttendantUseCase.execute(invalidAttendantId, updateAttendantInputDTO)
        );

        assertEquals("Registro não encontrado: Attendant", exception.getMessage());
        verify(attendantGateway).findFirstById(invalidAttendantId);
        verify(attendantGateway, never()).save(any(Attendant.class));
    }

    @Test
    @DisplayName("Deve retornar attendant construído com novos dados")
    void shouldReturnAttendantBuiltWithNewData() {
        // Arrange
        when(attendantGateway.findFirstById(validAttendantId)).thenReturn(existingAttendant);
        when(attendantGateway.save(any(Attendant.class))).thenReturn(updatedAttendant);

        // Act
        Attendant result = updateAttendantUseCase.execute(validAttendantId, updateAttendantInputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updateAttendantInputDTO.name(), result.getName());
        assertEquals(updateAttendantInputDTO.email(), result.getEmail());
        assertEquals(updateAttendantInputDTO.cpf(), result.getUnformattedCpf());
        assertEquals(validAttendantId, result.getId());
    }

    @Test
    @DisplayName("Deve atualizar apenas campos específicos")
    void shouldUpdateOnlySpecificFields() {
        // Arrange
        AttendantInputDTO partialUpdateDTO = new AttendantInputDTO(
                "Novo Nome",
                "novo.email@test.com",
                "98765432100"
        );

        Attendant partialUpdatedAttendant = Attendant.build(
                validAttendantId,
                "Novo Nome",
                "novo.email@test.com",
                "98765432100"
        );

        when(attendantGateway.findFirstById(validAttendantId)).thenReturn(existingAttendant);
        when(attendantGateway.save(any(Attendant.class))).thenReturn(partialUpdatedAttendant);

        // Act
        Attendant result = updateAttendantUseCase.execute(validAttendantId, partialUpdateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Novo Nome", result.getName());
        assertEquals("novo.email@test.com", result.getEmail());
        assertEquals("98765432100", result.getUnformattedCpf());
        assertEquals(validAttendantId, result.getId());
        verify(attendantGateway).save(any(Attendant.class));
    }

    @Test
    @DisplayName("Deve manter o ID original do attendant durante atualização")
    void shouldMaintainOriginalAttendantIdDuringUpdate() {
        // Arrange
        when(attendantGateway.findFirstById(validAttendantId)).thenReturn(existingAttendant);
        when(attendantGateway.save(any(Attendant.class))).thenReturn(updatedAttendant);

        // Act
        Attendant result = updateAttendantUseCase.execute(validAttendantId, updateAttendantInputDTO);

        // Assert
        assertEquals(validAttendantId, result.getId());
        assertEquals(existingAttendant.getId(), result.getId());
        verify(attendantGateway).save(argThat(attendant ->
            attendant.getId().equals(validAttendantId)
        ));
    }

    @Test
    @DisplayName("Deve validar que save é chamado mesmo quando dados são iguais")
    void shouldCallSaveEvenWhenDataIsTheSame() {
        // Arrange
        AttendantInputDTO sameDataDTO = new AttendantInputDTO(
                existingAttendant.getName(),
                existingAttendant.getEmail(),
                existingAttendant.getUnformattedCpf()
        );

        when(attendantGateway.findFirstById(validAttendantId)).thenReturn(existingAttendant);
        when(attendantGateway.save(any(Attendant.class))).thenReturn(existingAttendant);

        // Act
        Attendant result = updateAttendantUseCase.execute(validAttendantId, sameDataDTO);

        // Assert
        assertNotNull(result);
        verify(attendantGateway).findFirstById(validAttendantId);
        verify(attendantGateway).save(any(Attendant.class));
    }

    @Test
    @DisplayName("Deve tratar ID nulo")
    void shouldHandleNullId() {
        // Arrange
        when(attendantGateway.findFirstById(null)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            updateAttendantUseCase.execute(null, updateAttendantInputDTO);
        });

        verify(attendantGateway).findFirstById(null);
        verify(attendantGateway, never()).save(any(Attendant.class));
    }

    @Test
    @DisplayName("Deve tratar DTO nulo")
    void shouldHandleNullDTO() {
        // Arrange
        when(attendantGateway.findFirstById(validAttendantId)).thenReturn(existingAttendant);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            updateAttendantUseCase.execute(validAttendantId, null);
        });

        verify(attendantGateway).findFirstById(validAttendantId);
        verify(attendantGateway, never()).save(any(Attendant.class));
    }
}
