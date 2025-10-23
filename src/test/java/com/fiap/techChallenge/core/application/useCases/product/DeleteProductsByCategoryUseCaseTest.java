package com.fiap.techChallenge.core.application.useCases.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para DeleteProductsByCategoryUseCase")
class DeleteProductsByCategoryUseCaseTest {

    @Mock
    private ProductGateway gateway;

    @InjectMocks
    private DeleteProductsByCategoryUseCase deleteProductsByCategoryUseCase;

    @Test
    @DisplayName("Deve chamar o método deleteByCategory do gateway com a categoria correta")
    void shouldCallGatewayDeleteByCategoryWithCorrectCategory() {
        Category categoryToDelete = Category.BEBIDA;

        deleteProductsByCategoryUseCase.execute(categoryToDelete);

        verify(gateway, times(1)).deleteByCategory(categoryToDelete);
    }
}