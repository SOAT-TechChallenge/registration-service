package com.fiap.techChallenge.core.application.useCases.product;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ListProductsByCategoryUseCase")
class ListProductsByCategoryUseCaseTest {

    @Mock
    private ProductGateway gateway;

    @InjectMocks
    private ListProductsByCategoryUseCase listProductsByCategoryUseCase;

    @Test
    @DisplayName("Deve retornar uma lista de produtos para a categoria informada")
    void shouldReturnListOfProductsForGivenCategory() {
        Category targetCategory = Category.LANCHE;
        List<Product> expectedProducts = List.of(mock(Product.class));

        when(gateway.listByCategory(targetCategory)).thenReturn(expectedProducts);

        List<Product> actualProducts = listProductsByCategoryUseCase.execute(targetCategory);

        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.size());
        assertEquals(expectedProducts, actualProducts);

        verify(gateway, times(1)).listByCategory(targetCategory);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não existem produtos para a categoria")
    void shouldReturnEmptyListWhenNoProductsExistForCategory() {
        Category targetCategory = Category.SOBREMESA;
        when(gateway.listByCategory(targetCategory)).thenReturn(Collections.emptyList());

        List<Product> actualProducts = listProductsByCategoryUseCase.execute(targetCategory);

        assertNotNull(actualProducts);
        assertEquals(0, actualProducts.size());

        verify(gateway, times(1)).listByCategory(targetCategory);
    }
}