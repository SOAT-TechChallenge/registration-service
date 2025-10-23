package com.fiap.techChallenge.core.gateway.user;

import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import com.fiap.techChallenge.core.gateways.user.AttendantGatewayImpl;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AttendantGatewayImplTest {

    @Mock
    private CompositeDataSource compositeDataSource;

    private AttendantGatewayImpl attendantGateway;

    private UUID testId;
    private String testName;
    private String testEmail;
    private String testCpf;
    private Attendant testAttendant;
    private AttendantDTO testAttendantDTO;

    @BeforeEach
    void setUp() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            attendantGateway = new AttendantGatewayImpl(compositeDataSource);

            testId = UUID.randomUUID();
            testName = "João Silva";
            testEmail = "joao.silva@example.com";
            testCpf = "12345678901";

            testAttendant = Attendant.build(testId, testName, testEmail, testCpf);
            testAttendantDTO = new AttendantDTO(testId, testName, testEmail, testCpf);
        }
    }

    @Test
    @DisplayName("Deve retornar atendente quando um atendente válido for fornecido")
    void saveShouldReturnAttendantWhenValidAttendantProvided() {
        // Arrange
        when(compositeDataSource.saveAttendant(any(AttendantDTO.class))).thenReturn(testAttendantDTO);

        // Act
        Attendant result = attendantGateway.save(testAttendant);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testName, result.getName());
        assertEquals(testEmail, result.getEmail());
        assertEquals(testCpf, result.getUnformattedCpf());

        verify(compositeDataSource, times(1)).saveAttendant(any(AttendantDTO.class));
    }

    @Test
    @DisplayName("Deve chamar o DataSource com o DTO correto")
    void saveShouldCallDataSourceWithCorrectDTO() {
        // Arrange
        when(compositeDataSource.saveAttendant(any(AttendantDTO.class))).thenReturn(testAttendantDTO);

        // Act
        attendantGateway.save(testAttendant);

        // Assert
        verify(compositeDataSource, times(1)).saveAttendant(argThat(dto ->
                dto.id().equals(testId) &&
                dto.name().equals(testName) &&
                dto.email().equals(testEmail) &&
                dto.cpf().equals(testCpf)
        ));
    }

    @Test
    @DisplayName("Deve retornar atendente quando o atendente existir por ID")
    void findFirstByIdShouldReturnAttendantWhenAttendantExists() {
        // Arrange
        when(compositeDataSource.findFirstAttendantById(testId)).thenReturn(testAttendantDTO);

        // Act
        Attendant result = attendantGateway.findFirstById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testName, result.getName());
        assertEquals(testEmail, result.getEmail());
        assertEquals(testCpf, result.getUnformattedCpf());

        verify(compositeDataSource, times(1)).findFirstAttendantById(testId);
    }

    @Test
    @DisplayName("Deve retornar nulo quando o atendente não existir por ID")
    void findFirstByIdShouldReturnNullWhenAttendantNotExists() {
        // Arrange
        when(compositeDataSource.findFirstAttendantById(testId)).thenReturn(null);

        // Act
        Attendant result = attendantGateway.findFirstById(testId);

        // Assert
        assertNull(result);
        verify(compositeDataSource, times(1)).findFirstAttendantById(testId);
    }

    @Test
    @DisplayName("Deve retornar atendente quando o atendente existir por CPF")
    void findFirstByCpfShouldReturnAttendantWhenAttendantExists() {
        // Arrange
        when(compositeDataSource.findFirstAttendantByCpf(testCpf)).thenReturn(testAttendantDTO);

        // Act
        Attendant result = attendantGateway.findFirstByCpf(testCpf);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testName, result.getName());
        assertEquals(testEmail, result.getEmail());
        assertEquals(testCpf, result.getUnformattedCpf());

        verify(compositeDataSource, times(1)).findFirstAttendantByCpf(testCpf);
    }

    @Test
    @DisplayName("Deve retornar nulo quando o atendente não existir por CPF")
    void findFirstByCpfShouldReturnNullWhenAttendantNotExists() {
        // Arrange
        when(compositeDataSource.findFirstAttendantByCpf(testCpf)).thenReturn(null);

        // Act
        Attendant result = attendantGateway.findFirstByCpf(testCpf);

        // Assert
        assertNull(result);
        verify(compositeDataSource, times(1)).findFirstAttendantByCpf(testCpf);
    }

    @Test
    @DisplayName("Deve retornar lista de atendentes quando existirem atendentes")
    void findAllShouldReturnListOfAttendantsWhenAttendantsExist() {
        // Arrange
        UUID secondId = UUID.randomUUID();
        AttendantDTO secondDTO = new AttendantDTO(secondId, "Maria Santos", "maria.santos@example.com", "98765432100");
        List<AttendantDTO> attendantDTOList = Arrays.asList(testAttendantDTO, secondDTO);

        when(compositeDataSource.findAllAttendants()).thenReturn(attendantDTOList);

        // Act
        List<Attendant> result = attendantGateway.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        Attendant firstAttendant = result.get(0);
        assertEquals(testId, firstAttendant.getId());
        assertEquals(testName, firstAttendant.getName());
        assertEquals(testEmail, firstAttendant.getEmail());
        assertEquals(testCpf, firstAttendant.getUnformattedCpf());

        Attendant secondAttendant = result.get(1);
        assertEquals(secondId, secondAttendant.getId());
        assertEquals("Maria Santos", secondAttendant.getName());
        assertEquals("maria.santos@example.com", secondAttendant.getEmail());
        assertEquals("98765432100", secondAttendant.getUnformattedCpf());

        verify(compositeDataSource, times(1)).findAllAttendants();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existirem atendentes")
    void findAllShouldReturnEmptyListWhenNoAttendantsExist() {
        // Arrange
        when(compositeDataSource.findAllAttendants()).thenReturn(List.of());

        // Act
        List<Attendant> result = attendantGateway.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(compositeDataSource, times(1)).findAllAttendants();
    }

    @Test
    @DisplayName("Deve chamar o método delete do DataSource")
    void deleteShouldCallDataSourceDelete() {
        // Act
        attendantGateway.delete(testId);

        // Assert
        verify(compositeDataSource, times(1)).deleteAttendant(testId);
    }

    @Test
    @DisplayName("Deve inicializar o construtor com DataSource")
    void constructorShouldInitializeWithDataSource() {
        // Arrange & Act
        AttendantGatewayImpl gateway = new AttendantGatewayImpl(compositeDataSource);

        // Assert
        assertNotNull(gateway);
    }
}
