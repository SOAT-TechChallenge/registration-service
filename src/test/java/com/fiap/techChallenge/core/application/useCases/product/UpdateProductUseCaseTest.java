package com.fiap.techChallenge.core.application.useCases.product;

import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.domain.exceptions.product.NameAlreadyRegisteredException;
import com.fiap.techChallenge.core.application.dto.product.UpdateProductInputDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para UpdateProductUseCase")
class UpdateProductUseCaseTest {

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private UpdateProductUseCase updateProductUseCase;

    @Test
    @DisplayName("Deve atualizar o produto com sucesso")
    void shouldUpdateProductSuccessfully() {
        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.build(
                productId, "Old Name", "Old Desc", new BigDecimal("10.0"),
                Category.LANCHE, ProductStatus.INDISPONIVEL, "old.png"
        );
        UpdateProductInputDTO dto = new UpdateProductInputDTO(
                productId, "New Name", "New Desc", new BigDecimal("20.0"),
                Category.BEBIDA, ProductStatus.DISPONIVEL, "new.png"
        );

        when(productGateway.findById(productId)).thenReturn(existingProduct);
        when(productGateway.findByName(dto.name())).thenReturn(null);
        when(productGateway.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = updateProductUseCase.execute(dto);

        assertNotNull(result);
        assertEquals(dto.name(), result.getName());
        assertEquals(dto.description(), result.getDescription());
        assertEquals(dto.price(), result.getPrice());
        verify(productGateway, times(1)).findById(productId);
        verify(productGateway, times(1)).findByName(dto.name());
        verify(productGateway, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve atualizar o produto quando o nome não for alterado")
    void shouldUpdateProductWhenNameIsUnchanged() {
        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.build(
                productId, "Same Name", "Old Desc", new BigDecimal("10.0"),
                Category.LANCHE, ProductStatus.DISPONIVEL, "old.png"
        );
        UpdateProductInputDTO dto = new UpdateProductInputDTO(
                productId, "Same Name", "New Desc", new BigDecimal("20.0"),
                Category.LANCHE, ProductStatus.DISPONIVEL, "new.png"
        );

        when(productGateway.findById(productId)).thenReturn(existingProduct);
        when(productGateway.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = updateProductUseCase.execute(dto);

        assertNotNull(result);
        assertEquals("New Desc", result.getDescription());
        verify(productGateway, times(1)).findById(productId);
        verify(productGateway, never()).findByName(anyString());
        verify(productGateway, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando o produto não existe")
    void shouldThrowEntityNotFoundExceptionWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();
        UpdateProductInputDTO dto = new UpdateProductInputDTO(
                productId, "Any Name", "Any Desc", new BigDecimal("10.0"),
                Category.LANCHE, ProductStatus.DISPONIVEL, "any.png"
        );

        when(productGateway.findById(productId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> updateProductUseCase.execute(dto));

        verify(productGateway, times(1)).findById(productId);
        verify(productGateway, never()).findByName(anyString());
        verify(productGateway, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar NameAlreadyRegisteredException quando o nome já pertence a outro produto")
    void shouldThrowNameAlreadyRegisteredExceptionWhenNameExistsForAnotherProduct() {
        UUID productIdToUpdate = UUID.randomUUID();
        Product productToUpdate = Product.build(
                productIdToUpdate, "Old Name", "Desc 1", new BigDecimal("10.0"),
                Category.LANCHE, ProductStatus.DISPONIVEL, "img1.png"
        );
        Product productWithSameName = Product.build(
                UUID.randomUUID(), "New Name", "Desc 2", new BigDecimal("20.0"),
                Category.SOBREMESA, ProductStatus.DISPONIVEL, "img2.png"
        );
        UpdateProductInputDTO dto = new UpdateProductInputDTO(
                productIdToUpdate, "New Name", "New Desc", new BigDecimal("30.0"),
                Category.BEBIDA, ProductStatus.DISPONIVEL, "img3.png"
        );

        when(productGateway.findById(productIdToUpdate)).thenReturn(productToUpdate);
        when(productGateway.findByName("New Name")).thenReturn(productWithSameName);

        assertThrows(NameAlreadyRegisteredException.class, () -> updateProductUseCase.execute(dto));

        verify(productGateway, times(1)).findById(productIdToUpdate);
        verify(productGateway, times(1)).findByName("New Name");
        verify(productGateway, never()).save(any(Product.class));
    }
}
