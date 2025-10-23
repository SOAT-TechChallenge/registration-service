package com.fiap.techChallenge.core.application.useCases.product;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techChallenge.core.application.dto.product.CreateProductInputDTO;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.domain.exceptions.product.NameAlreadyRegisteredException;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para CreateProductUseCase")
class CreateProductUseCaseTest {

    @Mock
    private ProductGateway gateway;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    private CreateProductInputDTO createProductInputDTO;

    @BeforeEach
    void setUp() {
        createProductInputDTO = new CreateProductInputDTO(
                "Hambúrguer de Bacon",
                "Delicioso hambúrguer com bacon crocante",
                new BigDecimal("35.50"),
                Category.LANCHE,
                ProductStatus.DISPONIVEL,
                "bacon_burger.png"
        );
    }

    @Test
    @DisplayName("Deve criar e salvar um produto com sucesso quando o nome é único")
    void shouldCreateAndSaveProductWhenNameIsUnique() {
        when(gateway.existisByName(createProductInputDTO.name())).thenReturn(false);

        when(gateway.save(any(Product.class))).thenAnswer(invocation -> {
            Product productToSave = invocation.getArgument(0);
            return productToSave;
        });

        Product result = createProductUseCase.execute(createProductInputDTO);

        assertNotNull(result);
        assertEquals(createProductInputDTO.name(), result.getName());
        assertEquals(createProductInputDTO.description(), result.getDescription());
        assertEquals(0, createProductInputDTO.price().compareTo(result.getPrice()));

        verify(gateway, times(1)).existisByName(createProductInputDTO.name());
        verify(gateway, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar NameAlreadyRegisteredException quando o nome do produto já existe")
    void shouldThrowNameAlreadyRegisteredExceptionWhenNameExists() {
        when(gateway.existisByName(createProductInputDTO.name())).thenReturn(true);

        assertThrows(NameAlreadyRegisteredException.class, () -> {
            createProductUseCase.execute(createProductInputDTO);
        });

        verify(gateway, times(1)).existisByName(createProductInputDTO.name());
        verify(gateway, never()).save(any(Product.class));
    }
}