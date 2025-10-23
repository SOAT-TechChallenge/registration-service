package com.fiap.techChallenge.core.application.useCases.attendant;

import com.fiap.techChallenge.core.application.useCases.user.attendant.ListAttendantsUseCase;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import com.fiap.techChallenge.core.gateways.user.AttendantGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListAttendantsUseCase Tests")
class ListAttendantsUseCaseTest {

    @Mock
    private AttendantGateway attendantGateway;

    @InjectMocks
    private ListAttendantsUseCase listAttendantsUseCase;

    private List<Attendant> mockAttendantsList;
    private Attendant attendant1;
    private Attendant attendant2;
    private Attendant attendant3;

    @BeforeEach
    void setUp() {
        attendant1 = Attendant.build(
                UUID.randomUUID(),
                "João Silva",
                "joao.silva@email.com",
                "12345678901"
        );

        attendant2 = Attendant.build(
                UUID.randomUUID(),
                "Maria Santos",
                "maria.santos@email.com",
                "98765432100"
        );

        attendant3 = Attendant.build(
                UUID.randomUUID(),
                "Pedro Oliveira",
                "pedro.oliveira@email.com",
                "11122233344"
        );

        mockAttendantsList = List.of(attendant1, attendant2, attendant3);
    }

    @Test
    @DisplayName("Deve retornar lista de attendants quando existem attendants")
    void shouldReturnListOfAttendantsWhenAttendantsExist() {
        // Arrange
        when(attendantGateway.findAll()).thenReturn(mockAttendantsList);

        // Act
        List<Attendant> result = listAttendantsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(mockAttendantsList, result);
        assertTrue(result.contains(attendant1));
        assertTrue(result.contains(attendant2));
        assertTrue(result.contains(attendant3));
        verify(attendantGateway).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem attendants")
    void shouldReturnEmptyListWhenNoAttendantsExist() {
        // Arrange
        when(attendantGateway.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Attendant> result = listAttendantsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(attendantGateway).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista com um único attendant")
    void shouldReturnListWithSingleAttendant() {
        // Arrange
        List<Attendant> singleAttendantList = List.of(attendant1);
        when(attendantGateway.findAll()).thenReturn(singleAttendantList);

        // Act
        List<Attendant> result = listAttendantsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(attendant1, result.get(0));
        verify(attendantGateway).findAll();
    }

    @Test
    @DisplayName("Deve funcionar com lista grande de attendants")
    void shouldWorkWithLargeListOfAttendants() {
        // Arrange
        List<Attendant> largeAttendantsList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Attendant attendant = Attendant.build(
                    UUID.randomUUID(),
                    "Attendant " + i,
                    "attendant" + i + "@email.com",
                    String.format("%011d", i)
            );
            largeAttendantsList.add(attendant);
        }

        when(attendantGateway.findAll()).thenReturn(largeAttendantsList);

        // Act
        List<Attendant> result = listAttendantsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(100, result.size());
        assertEquals(largeAttendantsList, result);
        verify(attendantGateway).findAll();
    }

    @Test
    @DisplayName("Deve manter ordem dos attendants retornada pelo gateway")
    void shouldMaintainOrderOfAttendantsFromGateway() {
        // Arrange
        List<Attendant> orderedList = List.of(attendant3, attendant1, attendant2);
        when(attendantGateway.findAll()).thenReturn(orderedList);

        // Act
        List<Attendant> result = listAttendantsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(attendant3, result.get(0));
        assertEquals(attendant1, result.get(1));
        assertEquals(attendant2, result.get(2));
        verify(attendantGateway).findAll();
    }

    @Test
    @DisplayName("Deve verificar que todos os attendants retornados têm dados válidos")
    void shouldEnsureAllReturnedAttendantsHaveValidData() {
        // Arrange
        when(attendantGateway.findAll()).thenReturn(mockAttendantsList);

        // Act
        List<Attendant> result = listAttendantsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        result.forEach(attendant -> {
            assertNotNull(attendant, "Attendant não deveria ser nulo");
            assertNotNull(attendant.getName(), "Nome do attendant não deveria ser nulo");
            assertNotNull(attendant.getEmail(), "Email do attendant não deveria ser nulo");
            assertNotNull(attendant.getUnformattedCpf(), "CPF do attendant não deveria ser nulo");
        });
    }
}
