package com.fiap.techChallenge.core.application.services.product;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.domain.exceptions.product.ProductNotAvaiableException;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ProductAvailabilityService (Refatorado)")
class ProductAvailabilityServiceTest {

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private ProductAvailabilityService productAvailabilityService;
    
    private Product createTestProduct(UUID id, ProductStatus status) {
        return Product.build(
                id,
                "Standard Product",
                "Standard Description",
                new BigDecimal("1.99"),
                Category.ACOMPANHAMENTO,
                status,
                "default.png"
        );
    }

    @Test
    @DisplayName("Deve retornar o produto quando ele está disponível")
    void shouldReturnProductWhenAvailable() {
        UUID productId = UUID.randomUUID();
        Product product = createTestProduct(productId, ProductStatus.DISPONIVEL);

        when(productGateway.findById(productId)).thenReturn(product);

        Product result = productAvailabilityService.findAvailableProduct(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(productGateway, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o produto não é encontrado")
    void shouldThrowExceptionWhenProductIsNotFound() {
        UUID productId = UUID.randomUUID();
        when(productGateway.findById(productId)).thenReturn(null);

        assertThrows(ProductNotAvaiableException.class, () -> {
            productAvailabilityService.findAvailableProduct(productId);
        });
        
        verify(productGateway, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o produto está indisponível")
    void shouldThrowExceptionWhenProductIsUnavailable() {
        UUID productId = UUID.randomUUID();
        Product product = createTestProduct(productId, ProductStatus.INDISPONIVEL);

        when(productGateway.findById(productId)).thenReturn(product);

        assertThrows(ProductNotAvaiableException.class, () -> {
            productAvailabilityService.findAvailableProduct(productId);
        });
        
        verify(productGateway, times(1)).findById(productId);
    }
}