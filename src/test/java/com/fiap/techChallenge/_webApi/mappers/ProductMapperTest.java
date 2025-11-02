package com.fiap.techChallenge._webApi.mappers;

import com.fiap.techChallenge._webApi.data.persistence.entity.product.ProductEntity;
import com.fiap.techChallenge.core.application.dto.product.ProductDTO;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductMapper - Testes")
class ProductMapperTest {

    @Test
    @DisplayName("Deve converter ProductEntity para ProductDTO quando entity não é nula")
    void shouldConvertEntityToDTOWhenEntityIsNotNull() {
        // Given
        UUID productId = UUID.randomUUID();
        ProductEntity entity = new ProductEntity();
        entity.setId(productId);
        entity.setName("Hambúrguer");
        entity.setDescription("Delicioso hambúrguer");
        entity.setPrice(new BigDecimal("25.50"));
        entity.setCategory(Category.LANCHE);
        entity.setStatus(ProductStatus.DISPONIVEL);
        entity.setImage("hamburger.png");

        // When
        ProductDTO dto = ProductMapper.toDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals(productId, dto.id());
        assertEquals("Hambúrguer", dto.name());
        assertEquals("Delicioso hambúrguer", dto.description());
        assertEquals(0, new BigDecimal("25.50").compareTo(dto.price()));
        assertEquals(Category.LANCHE, dto.category());
        assertEquals(ProductStatus.DISPONIVEL, dto.status());
        assertEquals("hamburger.png", dto.image());
    }

    @Test
    @DisplayName("Deve retornar null quando entity é nula")
    void shouldReturnNullWhenEntityIsNull() {
        // When
        ProductDTO dto = ProductMapper.toDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    @DisplayName("Deve converter ProductDTO para ProductEntity quando dto não é nulo")
    void shouldConvertDTOToEntityWhenDTOIsNotNull() {
        // Given
        UUID productId = UUID.randomUUID();
        ProductDTO dto = ProductDTO.builder()
                .id(productId)
                .name("Hambúrguer")
                .description("Delicioso hambúrguer")
                .price(new BigDecimal("25.50"))
                .category(Category.LANCHE)
                .status(ProductStatus.DISPONIVEL)
                .image("hamburger.png")
                .build();

        // When
        ProductEntity entity = ProductMapper.toEntity(dto);

        // Then
        assertNotNull(entity);
        assertEquals(productId, entity.getId());
        assertEquals("Hambúrguer", entity.getName());
        assertEquals("Delicioso hambúrguer", entity.getDescription());
        assertEquals(0, new BigDecimal("25.50").compareTo(entity.getPrice()));
        assertEquals(Category.LANCHE, entity.getCategory());
        assertEquals(ProductStatus.DISPONIVEL, entity.getStatus());
        assertEquals("hamburger.png", entity.getImage());
    }

    @Test
    @DisplayName("Deve retornar null quando dto é nulo")
    void shouldReturnNullWhenDTOIsNull() {
        // When
        ProductEntity entity = ProductMapper.toEntity(null);

        // Then
        assertNull(entity);
    }
}

