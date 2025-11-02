package com.fiap.techChallenge.bdd.steps;

import com.fiap.techChallenge.core.application.dto.product.CreateProductInputDTO;
import com.fiap.techChallenge.core.application.useCases.product.CreateProductUseCase;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.domain.exceptions.product.NameAlreadyRegisteredException;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Step Definitions para os cenários de criação de produto usando BDD com Cucumber
 */
public class CreateProductSteps {

    @Mock
    private ProductGateway gateway;

    private CreateProductUseCase createProductUseCase;
    private CreateProductInputDTO productInputDTO;
    private Product createdProduct;
    private NameAlreadyRegisteredException thrownException;
    private InOrder inOrder;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        createProductUseCase = new CreateProductUseCase(gateway);
        reset(gateway);
    }

    @Dado("que não existe produto com o nome {string}")
    public void que_nao_existe_produto_com_o_nome(String productName) {
        // Mock já configurado no @Before, apenas garantir que retorna false
        when(gateway.existisByName(productName)).thenReturn(false);
    }

    @Dado("que já existe um produto com o nome {string}")
    public void que_ja_existe_um_produto_com_o_nome(String productName) {
        reset(gateway);
        when(gateway.existisByName(productName)).thenReturn(true);
    }

    @Quando("eu criar um produto com os seguintes dados:")
    public void eu_criar_um_produto_com_os_seguintes_dados(io.cucumber.datatable.DataTable dataTable) {
        var row = dataTable.asMaps().get(0);
        
        productInputDTO = new CreateProductInputDTO(
                row.get("nome"),
                row.get("descrição"),
                new BigDecimal(row.get("preço")),
                Category.valueOf(row.get("categoria")),
                ProductStatus.valueOf(row.get("status")),
                row.get("imagem")
        );

        UUID productId = UUID.randomUUID();
        Product savedProduct = Product.build(
                productId,
                productInputDTO.name(),
                productInputDTO.description(),
                productInputDTO.price(),
                productInputDTO.category(),
                productInputDTO.status(),
                productInputDTO.image()
        );

        // Garantir que o mock está configurado corretamente
        when(gateway.existisByName(productInputDTO.name())).thenReturn(false);
        when(gateway.save(any(Product.class))).thenReturn(savedProduct);

        createdProduct = createProductUseCase.execute(productInputDTO);
        inOrder = inOrder(gateway);
    }

    @Quando("eu tentar criar um produto com o nome {string}")
    public void eu_tentar_criar_um_produto_com_o_nome(String productName) {
        productInputDTO = new CreateProductInputDTO(
                productName,
                "Descrição do produto",
                new BigDecimal("25.50"),
                Category.LANCHE,
                ProductStatus.DISPONIVEL,
                "image.png"
        );

        try {
            createProductUseCase.execute(productInputDTO);
            fail("Deve ter lançado NameAlreadyRegisteredException");
        } catch (NameAlreadyRegisteredException e) {
            thrownException = e;
        }
    }

    @Então("o produto deve ser criado com sucesso")
    public void o_produto_deve_ser_criado_com_sucesso() {
        assertNotNull(createdProduct, "O produto criado não deve ser nulo");
    }

    @Então("o produto deve ter o nome {string}")
    public void o_produto_deve_ter_o_nome(String expectedName) {
        assertEquals(expectedName, createdProduct.getName(), 
                "O nome do produto deve corresponder ao informado");
    }

    @Então("o produto deve ter a descrição {string}")
    public void o_produto_deve_ter_a_descricao(String expectedDescription) {
        assertEquals(expectedDescription, createdProduct.getDescription(), 
                "A descrição do produto deve corresponder à informada");
    }

    @Então("o produto deve ter o preço {string}")
    public void o_produto_deve_ter_o_preco(String expectedPriceStr) {
        BigDecimal expected = new BigDecimal(expectedPriceStr);
        assertEquals(0, expected.compareTo(createdProduct.getPrice()), 
                String.format("O preço do produto deve corresponder ao informado. Esperado: %s, Atual: %s", 
                        expected, createdProduct.getPrice()));
    }

    @Então("o produto deve ter a categoria {string}")
    public void o_produto_deve_ter_a_categoria(String expectedCategory) {
        assertEquals(Category.valueOf(expectedCategory), createdProduct.getCategory(), 
                "A categoria do produto deve corresponder à informada");
    }

    @Então("o produto deve ter o status {string}")
    public void o_produto_deve_ter_o_status(String expectedStatus) {
        assertEquals(ProductStatus.valueOf(expectedStatus), createdProduct.getStatus(), 
                "O status do produto deve corresponder ao informado");
    }

    @Então("o produto deve ter a imagem {string}")
    public void o_produto_deve_ter_a_imagem(String expectedImage) {
        assertEquals(expectedImage, createdProduct.getImage(), 
                "A imagem do produto deve corresponder à informada");
    }

    @Então("o gateway deve verificar a unicidade do nome antes de salvar")
    public void o_gateway_deve_verificar_a_unicidade_do_nome_antes_de_salvar() {
        inOrder.verify(gateway).existisByName(productInputDTO.name());
        inOrder.verify(gateway).save(any(Product.class));
    }

    @Então("o gateway deve salvar o produto")
    public void o_gateway_deve_salvar_o_produto() {
        verify(gateway, times(1)).save(any(Product.class));
    }

    @Então("deve ser lançada uma exceção do tipo NameAlreadyRegisteredException")
    public void deve_ser_lancada_uma_excecao_do_tipo_name_already_registered_exception() {
        assertNotNull(thrownException, "A exceção NameAlreadyRegisteredException deve ter sido lançada");
        assertInstanceOf(NameAlreadyRegisteredException.class, thrownException);
    }

    @Então("a mensagem da exceção deve conter {string}")
    public void a_mensagem_da_excecao_deve_conter(String expectedMessage) {
        assertNotNull(thrownException.getMessage(), "A exceção deve conter uma mensagem");
        assertTrue(thrownException.getMessage().contains(expectedMessage), 
                "A mensagem da exceção deve conter: " + expectedMessage);
    }

    @Então("o produto não deve ser salvo")
    public void o_produto_nao_deve_ser_salvo() {
        verify(gateway, times(1)).existisByName(productInputDTO.name());
        verify(gateway, never()).save(any(Product.class));
    }
}

