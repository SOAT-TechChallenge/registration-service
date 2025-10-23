package com.fiap.techChallenge.core.application.useCases.product;

import com.fiap.techChallenge.core.domain.entities.product.Product;
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
@DisplayName("Testes para ListProductsUseCase")
class ListProductsUseCaseTest {

    @Mock
    private ProductGateway gateway;

    @InjectMocks
    private ListProductsUseCase listProductsUseCase;

    @Test
    @DisplayName("Deve retornar uma lista com todos os produtos")
    void shouldReturnListOfAllProducts() {
        List<Product> expectedProducts = List.of(mock(Product.class), mock(Product.class));
        when(gateway.list()).thenReturn(expectedProducts);

        List<Product> actualProducts = listProductsUseCase.execute();

        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertEquals(expectedProducts, actualProducts);

        verify(gateway, times(1)).list();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não existem produtos")
    void shouldReturnEmptyListWhenNoProductsExist() {
        when(gateway.list()).thenReturn(Collections.emptyList());

        List<Product> actualProducts = listProductsUseCase.execute();

        assertNotNull(actualProducts);
        assertEquals(0, actualProducts.size());

        verify(gateway, times(1)).list();
    }
}