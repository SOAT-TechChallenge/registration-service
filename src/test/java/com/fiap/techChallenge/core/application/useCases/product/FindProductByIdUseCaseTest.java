package com.fiap.techChallenge.core.application.useCases.product;

import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para FindProductByIdUseCase")
class FindProductByIdUseCaseTest {

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private FindProductByIdUseCase findProductByIdUseCase;

    @Test
    @DisplayName("Deve retornar o produto quando encontrado")
    void shouldReturnProductWhenFound() {
        UUID productId = UUID.randomUUID();
        Product expectedProduct = Product.build(
                productId, "Found Product", "Description",
                new BigDecimal("50.0"), Category.SOBREMESA, ProductStatus.DISPONIVEL, "image.png"
        );

        when(productGateway.findById(productId)).thenReturn(expectedProduct);

        Product result = findProductByIdUseCase.execute(productId);

        assertEquals(expectedProduct, result);
        verify(productGateway, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando o produto não for encontrado")
    void shouldThrowExceptionWhenProductNotFound() {
        UUID productId = UUID.randomUUID();

        when(productGateway.findById(productId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            findProductByIdUseCase.execute(productId);
        });

        verify(productGateway, times(1)).findById(productId);
    }
}
