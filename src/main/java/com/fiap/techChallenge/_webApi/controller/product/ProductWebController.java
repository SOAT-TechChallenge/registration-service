package com.fiap.techChallenge._webApi.controller.product;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.techChallenge._webApi.dto.product.CreateProductDTO;
import com.fiap.techChallenge._webApi.dto.product.UpdateProductDTO;
import com.fiap.techChallenge.core.application.dto.product.CreateProductInputDTO;
import com.fiap.techChallenge.core.application.dto.product.ProductResponseDTO;
import com.fiap.techChallenge.core.application.dto.product.UpdateProductInputDTO;
import com.fiap.techChallenge.core.controller.product.ProductController;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product", description = "APIs relacionadas ao Produto cadastrados pelos atendentes")
public class ProductWebController {

    private final ProductController productController;

    public ProductWebController(CompositeDataSource compositeDataSource) {
        this.productController = ProductController.build(compositeDataSource);
    }

    @Transactional
    @PostMapping("/create")
    @Operation(summary = "Create", description = "Cria um novo produto")
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid CreateProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productController.create(
                CreateProductInputDTO.builder()
                        .name(dto.name())
                        .description(dto.description())
                        .price(dto.price())
                        .category(dto.category())
                        .status(dto.status())
                        .image(dto.image())
                        .build()
        ));
    }

    @Transactional
    @PostMapping("/update")
    @Operation(summary = "Update", description = "Atualiza um novo produto")
    public ResponseEntity<ProductResponseDTO> update(@RequestBody @Valid UpdateProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productController.update(
                UpdateProductInputDTO.builder()
                        .id(dto.id())
                        .name(dto.name())
                        .description(dto.description())
                        .price(dto.price())
                        .category(dto.category())
                        .status(dto.status())
                        .image(dto.image())
                        .build()
        ));
    }

    @GetMapping("/find-by-id/{id}")
    @Operation(summary = "Find By ID",
            description = "Encontra um produto pelo ID Informado")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(productController.findById(id));
    }

    @GetMapping("/find-by-name/{name}")
    @Operation(summary = "Find By Name", description = "Encontra um produto pelo Nome Informado")
    public ResponseEntity<ProductResponseDTO> findByName(@PathVariable String name) {
        return ResponseEntity.ok(productController.findByName(name));
    }

    @GetMapping("/list")
    @Operation(summary = "List",
            description = "Lista todos os produtos")
    public ResponseEntity<List<ProductResponseDTO>> list() {
        return ResponseEntity.ok(productController.list());
    }

    @GetMapping("/list-availables")
    @Operation(summary = "List Availables",
            description = "Lista todos os produtos disponiveis")
    public ResponseEntity<List<ProductResponseDTO>> listAvailables() {
        return ResponseEntity.ok(productController.listAvailables());
    }

    @GetMapping("/list-by-category/{category}")
    @Operation(summary = "List By Category", description = "Lista todos os produtos da categoria informada")
    public ResponseEntity<List<ProductResponseDTO>> listByCategory(@PathVariable Category category) {
        return ResponseEntity.ok(productController.listByCategory(category));
    }

    @GetMapping("/list-availables-by-category/{category}")
    @Operation(summary = "List Availables",
            description = "Lista todos os produtos disponiveis da categoria informada")
    public ResponseEntity<List<ProductResponseDTO>> listAvailablesByCategory(@PathVariable Category category) {
        return ResponseEntity.ok(productController.listAvailablesByCategory(category));
    }

    @GetMapping("/list-categorys")
    @Operation(summary = "List Categorys",
            description = "Lista todas as categoriasdisponiveis")
    public ResponseEntity<List<Category>> listCategorys() {
        return ResponseEntity.ok(productController.listCategorys());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete",
            description = "Deleta um produto")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        productController.delete(id);
        return ResponseEntity.ok("Produto deletado com sucesso");
    }

    @DeleteMapping("/delete-by-category/{category}")
    @Operation(summary = "Delete By Category",
            description = "Deleta os produtos da categoria informada")
    public ResponseEntity<String> deleteByCategory(@PathVariable Category category) {
        productController.deleteByCategory(category);
        return ResponseEntity.ok("Produtos deletados com sucesso");
    }

}
