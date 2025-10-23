package com.fiap.techChallenge.core.application.useCases.product;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
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
@DisplayName("Testes para ListAvailablesProductsUseCase")
class ListAvailablesProductsUseCaseTest {

    @Mock
    private ProductGateway gateway;

    @InjectMocks
    private ListAvailablesProductsUseCase listAvailablesProductsUseCase;

    @Test
    @DisplayName("Deve retornar uma lista de produtos disponíveis")
    void shouldReturnListOfAvailableProducts() {
        List<Product> expectedProducts = List.of(mock(Product.class), mock(Product.class));
        when(gateway.listByStatus(ProductStatus.DISPONIVEL)).thenReturn(expectedProducts);

        List<Product> actualProducts = listAvailablesProductsUseCase.execute();

        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertEquals(expectedProducts, actualProducts);
        verify(gateway, times(1)).listByStatus(ProductStatus.DISPONIVEL);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não há produtos disponíveis")
    void shouldReturnEmptyListWhenNoAvailableProductsExist() {
        when(gateway.listByStatus(ProductStatus.DISPONIVEL)).thenReturn(Collections.emptyList());

        List<Product> actualProducts = listAvailablesProductsUseCase.execute();

        assertNotNull(actualProducts);
        assertEquals(0, actualProducts.size());
        verify(gateway, times(1)).listByStatus(ProductStatus.DISPONIVEL);
    }
}