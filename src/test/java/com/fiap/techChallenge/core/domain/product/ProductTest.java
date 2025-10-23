package com.fiap.techChallenge.core.domain.product;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;

@DisplayName("Testes para a Entidade Product")
class ProductTest {

    @Nested
    @DisplayName("Testes de Sucesso")
    class SuccessTests {

        @Test
        @DisplayName("Deve criar um produto com sucesso quando os dados são válidos")
        void shouldCreateProductSuccessfullyWhenDataIsValid() {
            UUID id = UUID.randomUUID();
            String name = "Hambúrguer Clássico";
            String description = "Pão, carne e queijo";
            BigDecimal price = new BigDecimal("19.90");
            Category category = Category.LANCHE;
            ProductStatus status = ProductStatus.DISPONIVEL;
            String image = "https://example.com/image.png";

            Product product = Product.build(id, name, description, price, category, status, image);

            assertNotNull(product);
            assertEquals(id, product.getId());
            assertEquals(name, product.getName());
            assertEquals(description, product.getDescription());
            assertEquals(0, price.compareTo(product.getPrice()));
            assertEquals(category, product.getCategory());
            assertEquals(status, product.getStatus());
            assertEquals(image, product.getImage());
        }
        
        @Test
        @DisplayName("Deve atualizar as propriedades do produto corretamente via setters")
        void shouldUpdateProductPropertiesViaSetters() {
            Product product = Product.build(
                    UUID.randomUUID(),
                    "Nome Original",
                    "Descrição Original",
                    new BigDecimal("10.00"),
                    Category.LANCHE,
                    ProductStatus.DISPONIVEL,
                    "original.png"
            );

            String newName = "Nome Atualizado";
            String newDescription = "Descrição Atualizada";
            BigDecimal newPrice = new BigDecimal("15.50");
            Category newCategory = Category.ACOMPANHAMENTO;
            ProductStatus newStatus = ProductStatus.INDISPONIVEL;
            String newImage = "updated.png";
            
            product.setName(newName);
            product.setDescription(newDescription);
            product.setPrice(newPrice);
            product.setCategory(newCategory);
            product.setStatus(newStatus);
            product.setImage(newImage);

            assertEquals(newName, product.getName());
            assertEquals(newDescription, product.getDescription());
            assertEquals(0, newPrice.compareTo(product.getPrice()));
            assertEquals(newCategory, product.getCategory());
            assertEquals(newStatus, product.getStatus());
            assertEquals(newImage, product.getImage());
        }
    }

    @Nested
    @DisplayName("Testes de Falha (Validação de Regras de Negócio)")
    class FailureTests {

        @Test
        @DisplayName("Deve lançar exceção ao criar produto com nome nulo")
        void shouldThrowExceptionWhenNameIsNull() {
            String expectedMessage = "O nome do produto deve ser preenchido";

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                Product.build(UUID.randomUUID(), null, "Desc", new BigDecimal("10.0"), Category.LANCHE, ProductStatus.DISPONIVEL, "img.png")
            );

            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar produto com descrição vazia")
        void shouldThrowExceptionWhenDescriptionIsBlank() {
            String expectedMessage = "A descrição do produto deve ser preenchida.";

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                Product.build(UUID.randomUUID(), "Nome", "  ", new BigDecimal("10.0"), Category.LANCHE, ProductStatus.DISPONIVEL, "img.png")
            );

            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar produto com preço negativo")
        void shouldThrowExceptionWhenPriceIsNegative() {
            String expectedMessage = "O preço do produto deve estar preenchido e não pode ser negativo.";

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                Product.build(UUID.randomUUID(), "Nome", "Desc", new BigDecimal("-1.0"), Category.LANCHE, ProductStatus.DISPONIVEL, "img.png")
            );
            
            assertEquals(expectedMessage, exception.getMessage());
        }
        
        @Test
        @DisplayName("Deve lançar exceção ao criar produto com preço nulo")
        void shouldThrowExceptionWhenPriceIsNull() {
            String expectedMessage = "O preço do produto deve estar preenchido e não pode ser negativo.";

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                Product.build(UUID.randomUUID(), "Nome", "Desc", null, Category.LANCHE, ProductStatus.DISPONIVEL, "img.png")
            );
            
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar produto com categoria nula")
        void shouldThrowExceptionWhenCategoryIsNull() {
            String expectedMessage = "A categoria do produto deve ser preenchida.";

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                Product.build(UUID.randomUUID(), "Nome", "Desc", new BigDecimal("10.0"), null, ProductStatus.DISPONIVEL, "img.png")
            );
            
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar produto com imagem nula")
        void shouldThrowExceptionWhenImageIsNull() {
            String expectedMessage = "A imagem do produto deve ser informada.";

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                Product.build(UUID.randomUUID(), "Nome", "Desc", new BigDecimal("10.0"), Category.LANCHE, ProductStatus.DISPONIVEL, null)
            );
            
            assertEquals(expectedMessage, exception.getMessage());
        }
    }
}