package com.fiap.techChallenge.core.application.useCases.product;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techChallenge.core.application.dto.product.CreateProductInputDTO;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.domain.exceptions.product.NameAlreadyRegisteredException;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

/**
 * Testes unitários tradicionais para CreateProductUseCase
 * 
 * Nota: Os testes BDD com Cucumber estão implementados em:
 * - src/test/resources/features/product/CreateProduct.feature
 * - src/test/java/com/fiap/techChallenge/bdd/steps/CreateProductSteps.java
 * 
 * Para executar os testes BDD, use: mvn test -Dtest=CucumberTestRunner
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateProductUseCase - Testes Unitários")
class CreateProductUseCaseTest {

    @Mock
    private ProductGateway gateway;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    private CreateProductInputDTO createProductInputDTO;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        createProductInputDTO = new CreateProductInputDTO(
                "Hambúrguer de Bacon",
                "Delicioso hambúrguer com bacon crocante",
                new BigDecimal("35.50"),
                Category.LANCHE,
                ProductStatus.DISPONIVEL,
                "bacon_burger.png"
        );

        UUID productId = UUID.randomUUID();
        savedProduct = Product.build(
                productId,
                createProductInputDTO.name(),
                createProductInputDTO.description(),
                createProductInputDTO.price(),
                createProductInputDTO.category(),
                createProductInputDTO.status(),
                createProductInputDTO.image()
        );
    }

    @Nested
    @DisplayName("Cenário: Criar produto com sucesso")
    class CreateProductSuccessfully {

        @Test
        @DisplayName("Dado que não existe produto com o nome informado, " +
                     "Quando executar a criação do produto, " +
                     "Então deve criar e salvar o produto com sucesso")
        void givenNoProductWithNameExists_whenCreatingProduct_thenShouldCreateAndSaveSuccessfully() {
            // Given - Dado que não existe produto com o nome informado
            String productName = createProductInputDTO.name();
            when(gateway.existisByName(productName)).thenReturn(false);
            when(gateway.save(any(Product.class))).thenReturn(savedProduct);

            // When - Quando executar a criação do produto
            Product result = createProductUseCase.execute(createProductInputDTO);

            // Then - Então deve criar e salvar o produto com sucesso
            assertNotNull(result, "O produto criado não deve ser nulo");
            assertEquals(productName, result.getName(), "O nome do produto deve corresponder ao informado");
            assertEquals(createProductInputDTO.description(), result.getDescription(), 
                    "A descrição do produto deve corresponder à informada");
            assertEquals(0, createProductInputDTO.price().compareTo(result.getPrice()), 
                    "O preço do produto deve corresponder ao informado");
            assertEquals(createProductInputDTO.category(), result.getCategory(), 
                    "A categoria do produto deve corresponder à informada");
            assertEquals(createProductInputDTO.status(), result.getStatus(), 
                    "O status do produto deve corresponder ao informado");
            assertEquals(createProductInputDTO.image(), result.getImage(), 
                    "A imagem do produto deve corresponder à informada");

            // Verificações de interação
            verify(gateway, times(1)).existisByName(productName);
            verify(gateway, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Dado que o produto será criado, " +
                     "Quando executar a criação, " +
                     "Então deve verificar a unicidade do nome antes de salvar")
        void givenProductCreation_whenExecuting_thenShouldCheckNameUniquenessBeforeSaving() {
            // Given - Dado que o produto será criado
            when(gateway.existisByName(createProductInputDTO.name())).thenReturn(false);
            when(gateway.save(any(Product.class))).thenReturn(savedProduct);

            // When - Quando executar a criação
            createProductUseCase.execute(createProductInputDTO);

            // Then - Então deve verificar a unicidade do nome antes de salvar
            var inOrder = inOrder(gateway);
            inOrder.verify(gateway).existisByName(createProductInputDTO.name());
            inOrder.verify(gateway).save(any(Product.class));
        }
    }

    @Nested
    @DisplayName("Cenário: Falha na criação - Nome já registrado")
    class CreateProductFailure {

        @Test
        @DisplayName("Dado que já existe um produto com o nome informado, " +
                     "Quando tentar criar o produto, " +
                     "Então deve lançar NameAlreadyRegisteredException e não salvar")
        void givenProductNameAlreadyExists_whenCreatingProduct_thenShouldThrowExceptionAndNotSave() {
            // Given - Dado que já existe um produto com o nome informado
            String existingProductName = createProductInputDTO.name();
            when(gateway.existisByName(existingProductName)).thenReturn(true);

            // When/Then - Quando tentar criar o produto, então deve lançar exceção
            NameAlreadyRegisteredException exception = assertThrows(
                    NameAlreadyRegisteredException.class,
                    () -> createProductUseCase.execute(createProductInputDTO),
                    "Deve lançar NameAlreadyRegisteredException quando o nome já existe"
            );

            // Then - Verificar que a exceção contém a mensagem correta
            assertNotNull(exception.getMessage(), "A exceção deve conter uma mensagem");
            assertTrue(exception.getMessage().contains(existingProductName), 
                    "A mensagem da exceção deve conter o nome do produto");

            // Then - Verificar que não foi salvo
            verify(gateway, times(1)).existisByName(existingProductName);
            verify(gateway, never()).save(any(Product.class));
        }
    }
}