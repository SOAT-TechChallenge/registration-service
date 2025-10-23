package com.fiap.techChallenge.core.application.useCases.product;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ListAvailablesProductsByCategoryUseCase")
class ListAvailablesProductsByCategoryUseCaseTest {

    @Mock
    private ProductGateway gateway;

    @InjectMocks
    private ListAvailablesProductsByCategoryUseCase listAvailablesProductsByCategoryUseCase;

    @Test
    @DisplayName("Deve retornar a lista de produtos disponíveis para uma dada categoria")
    void shouldReturnListOfAvailableProductsForGivenCategory() {
        Category targetCategory = Category.ACOMPANHAMENTO;
        List<Product> expectedProducts = List.of(mock(Product.class), mock(Product.class));

        when(gateway.listByStatusAndCategory(ProductStatus.DISPONIVEL, targetCategory))
                .thenReturn(expectedProducts);

        List<Product> actualProducts = listAvailablesProductsByCategoryUseCase.execute(targetCategory);

        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertEquals(expectedProducts, actualProducts);

        verify(gateway, times(1)).listByStatusAndCategory(ProductStatus.DISPONIVEL, targetCategory);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não há produtos disponíveis na categoria")
    void shouldReturnEmptyListWhenNoAvailableProductsInCategory() {
        Category targetCategory = Category.SOBREMESA;
        
        when(gateway.listByStatusAndCategory(ProductStatus.DISPONIVEL, targetCategory))
                .thenReturn(Collections.emptyList());

        List<Product> actualProducts = listAvailablesProductsByCategoryUseCase.execute(targetCategory);

        assertNotNull(actualProducts);
        assertEquals(0, actualProducts.size());

        verify(gateway, times(1)).listByStatusAndCategory(ProductStatus.DISPONIVEL, targetCategory);
    }
}