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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para DeleteProductByIdUseCase")
class DeleteProductByIdUseCaseTest {

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private DeleteProductByIdUseCase deleteProductByIdUseCase;

    @Test
    @DisplayName("Deve deletar o produto com sucesso quando ele existe")
    void shouldDeleteProductWhenItExists() {
        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.build(
                productId, "Product to delete", "Description",
                new BigDecimal("10.0"), Category.LANCHE, ProductStatus.DISPONIVEL, "image.png"
        );

        when(productGateway.findById(productId)).thenReturn(existingProduct);
        doNothing().when(productGateway).delete(productId);

        assertDoesNotThrow(() -> deleteProductByIdUseCase.execute(productId));

        verify(productGateway, times(1)).findById(productId);
        verify(productGateway, times(1)).delete(productId);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando o produto não existe")
    void shouldThrowExceptionWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();

        when(productGateway.findById(productId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            deleteProductByIdUseCase.execute(productId);
        });

        verify(productGateway, times(1)).findById(productId);
        verify(productGateway, never()).delete(any(UUID.class));
    }
}
