package com.fiap.techChallenge.core.gateways.product;

import com.fiap.techChallenge.core.application.dto.product.ProductDTO;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductGatewayImpl - Testes")
class ProductGatewayImplTest {

    @Mock
    private CompositeDataSource compositeDataSource;

    private ProductGatewayImpl productGateway;
    private Product testProduct;
    private ProductDTO testProductDTO;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productGateway = new ProductGatewayImpl(compositeDataSource);
        productId = UUID.randomUUID();
        
        testProduct = Product.build(
                productId,
                "Hambúrguer",
                "Delicioso hambúrguer",
                new BigDecimal("25.50"),
                Category.LANCHE,
                ProductStatus.DISPONIVEL,
                "hamburger.png"
        );

        testProductDTO = new ProductDTO(
                productId,
                "Hambúrguer",
                "Delicioso hambúrguer",
                new BigDecimal("25.50"),
                Category.LANCHE,
                ProductStatus.DISPONIVEL,
                "hamburger.png"
        );
    }

    @Test
    @DisplayName("Deve salvar produto com sucesso")
    void shouldSaveProductSuccessfully() {
        // Given
        when(compositeDataSource.saveProduct(any(ProductDTO.class))).thenReturn(testProductDTO);

        // When
        Product result = productGateway.save(testProduct);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Hambúrguer", result.getName());
        verify(compositeDataSource, times(1)).saveProduct(any(ProductDTO.class));
    }

    @Test
    @DisplayName("Deve encontrar produto por ID quando existe")
    void shouldFindProductByIdWhenExists() {
        // Given
        when(compositeDataSource.findProductById(productId)).thenReturn(testProductDTO);

        // When
        Product result = productGateway.findById(productId);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Hambúrguer", result.getName());
        verify(compositeDataSource, times(1)).findProductById(productId);
    }

    @Test
    @DisplayName("Deve retornar null quando produto não existe por ID")
    void shouldReturnNullWhenProductNotFoundById() {
        // Given
        when(compositeDataSource.findProductById(productId)).thenReturn(null);

        // When
        Product result = productGateway.findById(productId);

        // Then
        assertNull(result);
        verify(compositeDataSource, times(1)).findProductById(productId);
    }

    @Test
    @DisplayName("Deve encontrar produto por nome quando existe")
    void shouldFindProductByNameWhenExists() {
        // Given
        String productName = "Hambúrguer";
        when(compositeDataSource.findProductByName(productName)).thenReturn(testProductDTO);

        // When
        Product result = productGateway.findByName(productName);

        // Then
        assertNotNull(result);
        assertEquals(productName, result.getName());
        verify(compositeDataSource, times(1)).findProductByName(productName);
    }

    @Test
    @DisplayName("Deve retornar null quando produto não existe por nome")
    void shouldReturnNullWhenProductNotFoundByName() {
        // Given
        String productName = "Inexistente";
        when(compositeDataSource.findProductByName(productName)).thenReturn(null);

        // When
        Product result = productGateway.findByName(productName);

        // Then
        assertNull(result);
        verify(compositeDataSource, times(1)).findProductByName(productName);
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void shouldListAllProducts() {
        // Given
        ProductDTO secondDTO = new ProductDTO(
                UUID.randomUUID(),
                "Pizza",
                "Pizza deliciosa",
                new BigDecimal("30.00"),
                Category.LANCHE,
                ProductStatus.DISPONIVEL,
                "pizza.png"
        );
        when(compositeDataSource.listProducts()).thenReturn(Arrays.asList(testProductDTO, secondDTO));

        // When
        List<Product> result = productGateway.list();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(compositeDataSource, times(1)).listProducts();
    }

    @Test
    @DisplayName("Deve listar produtos por status")
    void shouldListProductsByStatus() {
        // Given
        ProductStatus status = ProductStatus.DISPONIVEL;
        when(compositeDataSource.listProductsByStatus(status)).thenReturn(Arrays.asList(testProductDTO));

        // When
        List<Product> result = productGateway.listByStatus(status);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(compositeDataSource, times(1)).listProductsByStatus(status);
    }

    @Test
    @DisplayName("Deve listar produtos por status e categoria")
    void shouldListProductsByStatusAndCategory() {
        // Given
        ProductStatus status = ProductStatus.DISPONIVEL;
        Category category = Category.LANCHE;
        when(compositeDataSource.listProductsByStatusAndCategory(status, category)).thenReturn(Arrays.asList(testProductDTO));

        // When
        List<Product> result = productGateway.listByStatusAndCategory(status, category);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(compositeDataSource, times(1)).listProductsByStatusAndCategory(status, category);
    }

    @Test
    @DisplayName("Deve listar produtos por categoria")
    void shouldListProductsByCategory() {
        // Given
        Category category = Category.LANCHE;
        when(compositeDataSource.listProductsByCategory(category)).thenReturn(Arrays.asList(testProductDTO));

        // When
        List<Product> result = productGateway.listByCategory(category);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(compositeDataSource, times(1)).listProductsByCategory(category);
    }

    @Test
    @DisplayName("Deve listar categorias disponíveis")
    void shouldListAvailableCategories() {
        // Given
        when(compositeDataSource.listAvailableProductCategories()).thenReturn(Arrays.asList(Category.LANCHE, Category.BEBIDA));

        // When
        List<Category> result = productGateway.listAvailableCategorys();

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(compositeDataSource, times(1)).listAvailableProductCategories();
    }

    @Test
    @DisplayName("Deve deletar produto por ID")
    void shouldDeleteProductById() {
        // When
        productGateway.delete(productId);

        // Then
        verify(compositeDataSource, times(1)).deleteProduct(productId);
    }

    @Test
    @DisplayName("Deve deletar produtos por categoria")
    void shouldDeleteProductsByCategory() {
        // Given
        Category category = Category.LANCHE;

        // When
        productGateway.deleteByCategory(category);

        // Then
        verify(compositeDataSource, times(1)).deleteProductByCategory(category);
    }

    @Test
    @DisplayName("Deve retornar true quando produto existe por nome")
    void shouldReturnTrueWhenProductExistsByName() {
        // Given
        String productName = "Hambúrguer";
        when(compositeDataSource.findProductByName(productName)).thenReturn(testProductDTO);

        // When
        boolean exists = productGateway.existisByName(productName);

        // Then
        assertTrue(exists);
        verify(compositeDataSource, times(1)).findProductByName(productName);
    }

    @Test
    @DisplayName("Deve retornar false quando produto não existe por nome")
    void shouldReturnFalseWhenProductNotExistsByName() {
        // Given
        String productName = "Inexistente";
        when(compositeDataSource.findProductByName(productName)).thenReturn(null);

        // When
        boolean exists = productGateway.existisByName(productName);

        // Then
        assertFalse(exists);
        verify(compositeDataSource, times(1)).findProductByName(productName);
    }

    @Test
    @DisplayName("Deve retornar false quando produto encontrado não tem ID")
    void shouldReturnFalseWhenProductFoundHasNoId() {
        // Given
        String productName = "Produto sem ID";
        ProductDTO productWithoutId = new ProductDTO(
                null,
                productName,
                "Descrição",
                new BigDecimal("25.50"),
                Category.LANCHE,
                ProductStatus.DISPONIVEL,
                "image.png"
        );
        when(compositeDataSource.findProductByName(productName)).thenReturn(productWithoutId);

        // When
        boolean exists = productGateway.existisByName(productName);

        // Then
        assertFalse(exists);
    }
}

