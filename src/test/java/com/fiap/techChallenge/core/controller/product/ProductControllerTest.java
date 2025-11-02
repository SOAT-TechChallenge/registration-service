package com.fiap.techChallenge.core.controller.product;

import com.fiap.techChallenge.core.application.dto.product.CreateProductInputDTO;
import com.fiap.techChallenge.core.application.dto.product.ProductResponseDTO;
import com.fiap.techChallenge.core.application.dto.product.UpdateProductInputDTO;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController - Testes")
class ProductControllerTest {

    @Mock
    private CompositeDataSource compositeDataSource;

    private ProductController productController;

    @BeforeEach
    void setUp() {
        productController = ProductController.build(compositeDataSource);
    }

    @Test
    @DisplayName("Deve construir ProductController com sucesso")
    void shouldBuildProductControllerSuccessfully() {
        // When
        ProductController controller = ProductController.build(compositeDataSource);

        // Then
        assertNotNull(controller);
    }

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void shouldCreateProductSuccessfully() {
        // Given
        CreateProductInputDTO inputDTO = CreateProductInputDTO.builder()
                .name("Hambúrguer")
                .description("Delicioso hambúrguer")
                .price(new BigDecimal("25.50"))
                .category(Category.LANCHE)
                .status(ProductStatus.DISPONIVEL)
                .image("hamburger.png")
                .build();

        when(compositeDataSource.findProductByName(anyString())).thenReturn(null);
        when(compositeDataSource.saveProduct(any())).thenAnswer(invocation -> {
            var dto = invocation.getArgument(0, com.fiap.techChallenge.core.application.dto.product.ProductDTO.class);
            return com.fiap.techChallenge.core.application.dto.product.ProductDTO.builder()
                    .id(UUID.randomUUID())
                    .name(dto.name())
                    .description(dto.description())
                    .price(dto.price())
                    .category(dto.category())
                    .status(dto.status())
                    .image(dto.image())
                    .build();
        });

        // When
        ProductResponseDTO result = productController.create(inputDTO);

        // Then
        assertNotNull(result);
        assertEquals("Hambúrguer", result.name());
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void shouldUpdateProductSuccessfully() {
        // Given
        UUID productId = UUID.randomUUID();
        UpdateProductInputDTO inputDTO = UpdateProductInputDTO.builder()
                .id(productId)
                .name("Hambúrguer Atualizado")
                .description("Descrição atualizada")
                .price(new BigDecimal("30.00"))
                .category(Category.LANCHE)
                .status(ProductStatus.DISPONIVEL)
                .image("hamburger_updated.png")
                .build();

        when(compositeDataSource.findProductById(productId)).thenReturn(
                com.fiap.techChallenge.core.application.dto.product.ProductDTO.builder()
                        .id(productId)
                        .name("Hambúrguer")
                        .description("Delicioso hambúrguer")
                        .price(new BigDecimal("25.50"))
                        .category(Category.LANCHE)
                        .status(ProductStatus.DISPONIVEL)
                        .image("hamburger.png")
                        .build()
        );

        when(compositeDataSource.saveProduct(any())).thenAnswer(invocation -> {
            var dto = invocation.getArgument(0, com.fiap.techChallenge.core.application.dto.product.ProductDTO.class);
            return dto;
        });

        // When
        ProductResponseDTO result = productController.update(inputDTO);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.id());
    }

    @Test
    @DisplayName("Deve encontrar produto por ID")
    void shouldFindProductById() {
        // Given
        UUID productId = UUID.randomUUID();
        when(compositeDataSource.findProductById(productId)).thenReturn(
                com.fiap.techChallenge.core.application.dto.product.ProductDTO.builder()
                        .id(productId)
                        .name("Hambúrguer")
                        .description("Delicioso hambúrguer")
                        .price(new BigDecimal("25.50"))
                        .category(Category.LANCHE)
                        .status(ProductStatus.DISPONIVEL)
                        .image("hamburger.png")
                        .build()
        );

        // When
        ProductResponseDTO result = productController.findById(productId);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.id());
    }

    @Test
    @DisplayName("Deve encontrar produto por nome")
    void shouldFindProductByName() {
        // Given
        String productName = "Hambúrguer";
        UUID productId = UUID.randomUUID();
        when(compositeDataSource.findProductByName(productName)).thenReturn(
                com.fiap.techChallenge.core.application.dto.product.ProductDTO.builder()
                        .id(productId)
                        .name(productName)
                        .description("Delicioso hambúrguer")
                        .price(new BigDecimal("25.50"))
                        .category(Category.LANCHE)
                        .status(ProductStatus.DISPONIVEL)
                        .image("hamburger.png")
                        .build()
        );

        // When
        ProductResponseDTO result = productController.findByName(productName);

        // Then
        assertNotNull(result);
        assertEquals(productName, result.name());
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void shouldListAllProducts() {
        // Given
        when(compositeDataSource.listProducts()).thenReturn(List.of(
                com.fiap.techChallenge.core.application.dto.product.ProductDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Hambúrguer")
                        .description("Delicioso hambúrguer")
                        .price(new BigDecimal("25.50"))
                        .category(Category.LANCHE)
                        .status(ProductStatus.DISPONIVEL)
                        .image("hamburger.png")
                        .build()
        ));

        // When
        List<ProductResponseDTO> result = productController.list();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve listar produtos disponíveis")
    void shouldListAvailableProducts() {
        // Given
        when(compositeDataSource.listProductsByStatus(ProductStatus.DISPONIVEL)).thenReturn(List.of(
                com.fiap.techChallenge.core.application.dto.product.ProductDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Hambúrguer")
                        .description("Delicioso hambúrguer")
                        .price(new BigDecimal("25.50"))
                        .category(Category.LANCHE)
                        .status(ProductStatus.DISPONIVEL)
                        .image("hamburger.png")
                        .build()
        ));

        // When
        List<ProductResponseDTO> result = productController.listAvailables();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve listar produtos por categoria")
    void shouldListProductsByCategory() {
        // Given
        Category category = Category.LANCHE;
        when(compositeDataSource.listProductsByCategory(category)).thenReturn(List.of(
                com.fiap.techChallenge.core.application.dto.product.ProductDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Hambúrguer")
                        .description("Delicioso hambúrguer")
                        .price(new BigDecimal("25.50"))
                        .category(category)
                        .status(ProductStatus.DISPONIVEL)
                        .image("hamburger.png")
                        .build()
        ));

        // When
        List<ProductResponseDTO> result = productController.listByCategory(category);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve listar produtos disponíveis por categoria")
    void shouldListAvailableProductsByCategory() {
        // Given
        Category category = Category.LANCHE;
        when(compositeDataSource.listProductsByStatusAndCategory(ProductStatus.DISPONIVEL, category)).thenReturn(List.of(
                com.fiap.techChallenge.core.application.dto.product.ProductDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Hambúrguer")
                        .description("Delicioso hambúrguer")
                        .price(new BigDecimal("25.50"))
                        .category(category)
                        .status(ProductStatus.DISPONIVEL)
                        .image("hamburger.png")
                        .build()
        ));

        // When
        List<ProductResponseDTO> result = productController.listAvailablesByCategory(category);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve deletar produto por ID")
    void shouldDeleteProductById() {
        // Given
        UUID productId = UUID.randomUUID();
        when(compositeDataSource.findProductById(productId)).thenReturn(
                com.fiap.techChallenge.core.application.dto.product.ProductDTO.builder()
                        .id(productId)
                        .name("Hambúrguer")
                        .description("Delicioso hambúrguer")
                        .price(new BigDecimal("25.50"))
                        .category(Category.LANCHE)
                        .status(ProductStatus.DISPONIVEL)
                        .image("hamburger.png")
                        .build()
        );

        // When
        productController.delete(productId);

        // Then
        verify(compositeDataSource, times(1)).deleteProduct(productId);
    }

    @Test
    @DisplayName("Deve deletar produtos por categoria")
    void shouldDeleteProductsByCategory() {
        // Given
        Category category = Category.LANCHE;

        // When
        productController.deleteByCategory(category);

        // Then
        verify(compositeDataSource, times(1)).deleteProductByCategory(category);
    }

    @Test
    @DisplayName("Deve listar todas as categorias")
    void shouldListAllCategories() {
        // When
        List<Category> result = productController.listCategorys();

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(Category.values().length, result.size());
    }
}

