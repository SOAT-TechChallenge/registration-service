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
@DisplayName("Testes para FindProductByNameUseCase")
class FindProductByNameUseCaseTest {

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private FindProductByNameUseCase findProductByNameUseCase;

    @Test
    @DisplayName("Deve retornar o produto quando encontrado pelo nome")
    void shouldReturnProductWhenFound() {
        String productName = "X-Burger";
        Product expectedProduct = Product.build(
                UUID.randomUUID(), productName, "Description",
                new BigDecimal("25.50"), Category.LANCHE, ProductStatus.DISPONIVEL, "image.png"
        );

        when(productGateway.findByName(productName)).thenReturn(expectedProduct);

        Product result = findProductByNameUseCase.execute(productName);

        assertEquals(expectedProduct, result);
        verify(productGateway, times(1)).findByName(productName);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando o produto não for encontrado pelo nome")
    void shouldThrowExceptionWhenProductNotFound() {
        String productName = "Non-existent Burger";

        when(productGateway.findByName(productName)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            findProductByNameUseCase.execute(productName);
        });

        verify(productGateway, times(1)).findByName(productName);
    }
}
