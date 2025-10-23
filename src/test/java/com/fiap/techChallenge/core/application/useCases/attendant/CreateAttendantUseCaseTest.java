package com.fiap.techChallenge.core.application.useCases.attendant;

import com.fiap.techChallenge.core.application.dto.user.AttendantInputDTO;
import com.fiap.techChallenge.core.application.useCases.user.attendant.CreateAttendantUseCase;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import com.fiap.techChallenge.core.domain.exceptions.user.UserAlreadyExistsException;
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
@DisplayName("CreateAttendantUseCase Tests")
class CreateAttendantUseCaseTest {

    @Mock
    private AttendantGateway attendantGateway;

    @InjectMocks
    private CreateAttendantUseCase createAttendantUseCase;

    private AttendantInputDTO validAttendantInputDTO;
    private Attendant mockAttendant;

    @BeforeEach
    void setUp() {
        validAttendantInputDTO = new AttendantInputDTO(
                "João Silva",
                "joao.silva@email.com",
                "123.456.789-01"
        );

        mockAttendant = Attendant.build(
                UUID.randomUUID(),
                "João Silva",
                "joao.silva@email.com",
                "12345678901"
        );
    }

    @Test
    @DisplayName("Deve criar attendant com sucesso quando CPF não existe")
    void shouldCreateAttendantSuccessfully_WhenCpfDoesNotExist() {
        // Arrange
        when(attendantGateway.findFirstByCpf(validAttendantInputDTO.cpf())).thenReturn(null);
        when(attendantGateway.save(any(Attendant.class))).thenReturn(mockAttendant);

        // Act
        Attendant result = createAttendantUseCase.execute(validAttendantInputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(mockAttendant, result);
        verify(attendantGateway).findFirstByCpf(validAttendantInputDTO.cpf());
        verify(attendantGateway).save(any(Attendant.class));
    }

    @Test
    @DisplayName("Deve lançar UserAlreadyExistsException quando CPF já existe")
    void shouldThrowUserAlreadyExistsExceptionWhenCpfAlreadyExists() {
        // Arrange
        when(attendantGateway.findFirstByCpf(validAttendantInputDTO.cpf())).thenReturn(mockAttendant);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> {
            createAttendantUseCase.execute(validAttendantInputDTO);
        });

        verify(attendantGateway).findFirstByCpf(validAttendantInputDTO.cpf());
        verify(attendantGateway, never()).save(any(Attendant.class));
    }

    @Test
    @DisplayName("Deve criar attendant com dados mínimos válidos")
    void shouldCreateAttendantWithMinimalValidData() {
        // Arrange
        AttendantInputDTO minimalAttendantDTO = new AttendantInputDTO(
                "Ana",
                "ana@test.com",
                "98765432100"
        );

        Attendant expectedAttendant = Attendant.build(
                UUID.randomUUID(),
                "Ana",
                "ana@test.com",
                "98765432100"
        );

        when(attendantGateway.findFirstByCpf(minimalAttendantDTO.cpf())).thenReturn(null);
        when(attendantGateway.save(any(Attendant.class))).thenReturn(expectedAttendant);

        // Act
        Attendant result = createAttendantUseCase.execute(minimalAttendantDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Ana", result.getName());
        assertEquals("ana@test.com", result.getEmail());
        verify(attendantGateway).findFirstByCpf(minimalAttendantDTO.cpf());
        verify(attendantGateway).save(any(Attendant.class));
    }

    @Test
    @DisplayName("Deve criar attendant construído com ID null")
    void shouldCreateAttendantWithNullId() {
        // Arrange
        when(attendantGateway.findFirstByCpf(validAttendantInputDTO.cpf())).thenReturn(null);
        when(attendantGateway.save(any(Attendant.class))).thenReturn(mockAttendant);

        // Act
        createAttendantUseCase.execute(validAttendantInputDTO);

        // Assert
        verify(attendantGateway).save(argThat(attendant ->
            attendant.getId() == null
        ));
    }

    @Test
    @DisplayName("Deve tratar diferentes dados de entrada")
    void shouldHandleDifferentInputData() {
        // Arrange
        AttendantInputDTO differentDataDTO = new AttendantInputDTO(
                "Maria Oliveira",
                "maria.oliveira@empresa.com",
                "55566677788"
        );

        Attendant differentAttendant = Attendant.build(
                UUID.randomUUID(),
                "Maria Oliveira",
                "maria.oliveira@empresa.com",
                "55566677788"
        );

        when(attendantGateway.findFirstByCpf("55566677788")).thenReturn(null);
        when(attendantGateway.save(any(Attendant.class))).thenReturn(differentAttendant);

        // Act
        Attendant result = createAttendantUseCase.execute(differentDataDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Maria Oliveira", result.getName());
        assertEquals("maria.oliveira@empresa.com", result.getEmail());
        assertEquals("55566677788", result.getUnformattedCpf());
        verify(attendantGateway).findFirstByCpf("55566677788");
        verify(attendantGateway).save(any(Attendant.class));
    }
}
