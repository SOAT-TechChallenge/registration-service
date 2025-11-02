package com.fiap.techChallenge.core.presenter;

import com.fiap.techChallenge.core.application.dto.product.ProductResponseDTO;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductPresenter - Testes")
class ProductPresenterTest {

    @Test
    @DisplayName("Deve converter Product para ProductResponseDTO quando product não é nulo")
    void shouldConvertProductToDTOWhenProductIsNotNull() {
        // Given
        UUID productId = UUID.randomUUID();
        Product product = Product.build(
                productId,
                "Hambúrguer",
                "Delicioso hambúrguer",
                new BigDecimal("25.50"),
                Category.LANCHE,
                ProductStatus.DISPONIVEL,
                "hamburger.png"
        );

        // When
        ProductResponseDTO dto = ProductPresenter.toDTO(product);

        // Then
        assertNotNull(dto);
        assertEquals(productId, dto.id());
        assertEquals("Hambúrguer", dto.name());
        assertEquals("Delicioso hambúrguer", dto.description());
        assertEquals(0, new BigDecimal("25.50").compareTo(dto.price()));
        assertEquals(Category.LANCHE.toString(), dto.category());
        assertEquals(ProductStatus.DISPONIVEL.toString(), dto.status());
        assertEquals("hamburger.png", dto.image());
    }
}

