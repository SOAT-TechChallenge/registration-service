package com.fiap.techChallenge.core.controller.user;

import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.application.dto.user.AttendantInputDTO;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AttendantController")
class AttendantControllerTest {

    @Mock
    private CompositeDataSource compositeDataSource;

    private AttendantController attendantController;

    private AttendantInputDTO attendantInputDTO;
    private AttendantDTO attendantDto;
    private UUID attendantId;

    @BeforeEach
    void setUp() {
        attendantController = AttendantController.build(compositeDataSource);

        attendantId = UUID.randomUUID();

        attendantInputDTO = new AttendantInputDTO(
                "Carlos Oliveira",
                "carlos.oliveira@empresa.com",
                "111.222.333-44"
        );

        attendantDto = mock(AttendantDTO.class);
    }

    @Test
    @DisplayName("Deve construir AttendantController com sucesso")
    void shouldBuildAttendantControllerSuccessfully() {
        // When
        AttendantController controller = AttendantController.build(compositeDataSource);

        // Then
        assertNotNull(controller);
    }

    @Test
    @DisplayName("Deve criar atendente com sucesso")
    void shouldCreateAttendantSuccessfully() {
        // Given
        when(attendantDto.id()).thenReturn(attendantId);
        when(attendantDto.name()).thenReturn("Carlos Oliveira");
        when(attendantDto.email()).thenReturn("carlos.oliveira@empresa.com");
        when(attendantDto.cpf()).thenReturn("111.222.333-44");

        when(compositeDataSource.saveAttendant(any(AttendantDTO.class))).thenReturn(attendantDto);
        when(compositeDataSource.findFirstAttendantByCpf(anyString())).thenReturn(null);

        // When
        AttendantDTO result = attendantController.create(attendantInputDTO);

        // Then
        assertNotNull(result);
        assertEquals(attendantId, result.id());
        assertEquals("Carlos Oliveira", result.name());
        assertEquals("carlos.oliveira@empresa.com", result.email());
        assertEquals("111.222.333-44", result.cpf());

        verify(compositeDataSource, times(1)).saveAttendant(any(AttendantDTO.class));
    }

    @Test
    @DisplayName("Deve atualizar atendente com sucesso")
    void shouldUpdateAttendantSuccessfully() {
        // Given
        when(attendantDto.id()).thenReturn(attendantId);
        when(attendantDto.name()).thenReturn("Carlos Oliveira");
        when(attendantDto.email()).thenReturn("carlos.oliveira@empresa.com");
        when(attendantDto.cpf()).thenReturn("111.222.333-44");

        when(compositeDataSource.findFirstAttendantById(attendantId)).thenReturn(attendantDto);
        when(compositeDataSource.saveAttendant(any(AttendantDTO.class))).thenReturn(attendantDto);

        AttendantInputDTO updatedInputDTO = new AttendantInputDTO(
                "Carlos Oliveira Silva",
                "carlos.silva@empresa.com",
                "111.222.333-44"
        );

        // When
        AttendantDTO result = attendantController.update(attendantId, updatedInputDTO);

        // Then
        assertNotNull(result);
        assertEquals(attendantId, result.id());
        assertEquals("Carlos Oliveira Silva", result.name());
        assertEquals("carlos.silva@empresa.com", result.email());
        assertEquals("111.222.333-44", result.cpf());

        verify(compositeDataSource, times(1)).findFirstAttendantById(attendantId);
        verify(compositeDataSource, times(1)).saveAttendant(any(AttendantDTO.class));
    }

    @Test
    @DisplayName("Deve encontrar atendente por CPF com sucesso")
    void shouldFindAttendantByCpfSuccessfully() {
        // Given
        when(attendantDto.id()).thenReturn(attendantId);
        when(attendantDto.name()).thenReturn("Carlos Oliveira");
        when(attendantDto.email()).thenReturn("carlos.oliveira@empresa.com");
        when(attendantDto.cpf()).thenReturn("111.222.333-44");

        String cpf = "11122233344";
        when(compositeDataSource.findFirstAttendantByCpf(cpf)).thenReturn(attendantDto);

        // When
        AttendantDTO result = attendantController.findByCpf(cpf);

        // Then
        assertNotNull(result);
        assertEquals(attendantId, result.id());
        assertEquals("Carlos Oliveira", result.name());
        assertEquals("carlos.oliveira@empresa.com", result.email());
        assertEquals("111.222.333-44", result.cpf());

        verify(compositeDataSource, times(1)).findFirstAttendantByCpf(cpf);
    }

    @Test
    @DisplayName("Deve encontrar atendente por ID com sucesso")
    void shouldFindAttendantByIdSuccessfully() {
        // Given
        when(attendantDto.id()).thenReturn(attendantId);
        when(attendantDto.name()).thenReturn("Carlos Oliveira");
        when(attendantDto.email()).thenReturn("carlos.oliveira@empresa.com");
        when(attendantDto.cpf()).thenReturn("111.222.333-44");

        when(compositeDataSource.findFirstAttendantById(attendantId)).thenReturn(attendantDto);

        // When
        AttendantDTO result = attendantController.findById(attendantId);

        // Then
        assertNotNull(result);
        assertEquals(attendantId, result.id());
        assertEquals("Carlos Oliveira", result.name());
        assertEquals("carlos.oliveira@empresa.com", result.email());
        assertEquals("111.222.333-44", result.cpf());

        verify(compositeDataSource, times(1)).findFirstAttendantById(attendantId);
    }

    @Test
    @DisplayName("Deve listar todos os atendentes com sucesso")
    void shouldListAllAttendantsSuccessfully() {
        // Given
        when(attendantDto.id()).thenReturn(attendantId);
        when(attendantDto.name()).thenReturn("Carlos Oliveira");
        when(attendantDto.email()).thenReturn("carlos.oliveira@empresa.com");
        when(attendantDto.cpf()).thenReturn("111.222.333-44");

        AttendantDTO attendantDto2 = mock(AttendantDTO.class);
        UUID attendant2Id = UUID.randomUUID();
        when(attendantDto2.id()).thenReturn(attendant2Id);
        when(attendantDto2.name()).thenReturn("Ana Costa");
        when(attendantDto2.email()).thenReturn("ana.costa@empresa.com");
        when(attendantDto2.cpf()).thenReturn("555.666.777-88");

        List<AttendantDTO> attendantsDto = Arrays.asList(attendantDto, attendantDto2);
        when(compositeDataSource.findAllAttendants()).thenReturn(attendantsDto);

        // When
        List<AttendantDTO> result = attendantController.list();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        AttendantDTO firstAttendant = result.get(0);
        assertEquals(attendantId, firstAttendant.id());
        assertEquals("Carlos Oliveira", firstAttendant.name());
        assertEquals("carlos.oliveira@empresa.com", firstAttendant.email());
        assertEquals("111.222.333-44", firstAttendant.cpf());

        AttendantDTO secondAttendant = result.get(1);
        assertEquals(attendant2Id, secondAttendant.id());
        assertEquals("Ana Costa", secondAttendant.name());
        assertEquals("ana.costa@empresa.com", secondAttendant.email());
        assertEquals("555.666.777-88", secondAttendant.cpf());

        verify(compositeDataSource, times(1)).findAllAttendants();
    }

    @Test
    @DisplayName("Deve lidar com lista vazia quando não houver atendentes")
    void shouldHandleEmptyListWhenNoAttendantsExist() {
        // Given
        when(compositeDataSource.findAllAttendants()).thenReturn(List.of());

        // When
        List<AttendantDTO> result = attendantController.list();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(compositeDataSource, times(1)).findAllAttendants();
    }
}
