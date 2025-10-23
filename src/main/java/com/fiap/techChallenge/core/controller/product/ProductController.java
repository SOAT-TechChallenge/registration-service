package com.fiap.techChallenge.core.controller.product;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.fiap.techChallenge.core.application.dto.product.CreateProductInputDTO;
import com.fiap.techChallenge.core.application.dto.product.ProductResponseDTO;
import com.fiap.techChallenge.core.application.dto.product.UpdateProductInputDTO;
import com.fiap.techChallenge.core.application.useCases.product.CreateProductUseCase;
import com.fiap.techChallenge.core.application.useCases.product.DeleteProductByIdUseCase;
import com.fiap.techChallenge.core.application.useCases.product.DeleteProductsByCategoryUseCase;
import com.fiap.techChallenge.core.application.useCases.product.FindProductByIdUseCase;
import com.fiap.techChallenge.core.application.useCases.product.FindProductByNameUseCase;
import com.fiap.techChallenge.core.application.useCases.product.ListAvailablesProductsByCategoryUseCase;
import com.fiap.techChallenge.core.application.useCases.product.ListAvailablesProductsUseCase;
import com.fiap.techChallenge.core.application.useCases.product.ListProductsByCategoryUseCase;
import com.fiap.techChallenge.core.application.useCases.product.ListProductsUseCase;
import com.fiap.techChallenge.core.application.useCases.product.UpdateProductUseCase;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;
import com.fiap.techChallenge.core.gateways.product.ProductGatewayImpl;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import com.fiap.techChallenge.core.presenter.ProductPresenter;

public class ProductController {

    private final ProductGateway productGateway;

    private ProductController(CompositeDataSource compositeDataSource) {
        this.productGateway = new ProductGatewayImpl(compositeDataSource);
    }

    public static ProductController build(CompositeDataSource compositeDataSource) {
        return new ProductController(compositeDataSource);
    }

    public ProductResponseDTO create(CreateProductInputDTO dto) {
        CreateProductUseCase createProductUseCase = new CreateProductUseCase(productGateway);

        Product product = createProductUseCase.execute(dto);
        return ProductPresenter.toDTO(product);
    }

    public ProductResponseDTO update(UpdateProductInputDTO dto) {
        UpdateProductUseCase updateProductUseCase = new UpdateProductUseCase(productGateway);

        Product product = updateProductUseCase.execute(dto);
        return ProductPresenter.toDTO(product);
    }

    public ProductResponseDTO findById(UUID id) {
        FindProductByIdUseCase findProductByIdUseCase = new FindProductByIdUseCase(productGateway);
        Product product = findProductByIdUseCase.execute(id);
        return ProductPresenter.toDTO(product);
    }

    public ProductResponseDTO findByName(String name) {
        FindProductByNameUseCase findProductByNameUseCase = new FindProductByNameUseCase(productGateway);
        Product product = findProductByNameUseCase.execute(name);
        return ProductPresenter.toDTO(product);
    }

    public List<ProductResponseDTO> list() {
        ListProductsUseCase listUseCase = new ListProductsUseCase(productGateway);
        List<Product> productsList = listUseCase.execute();
        return productsList.stream().map(product -> ProductPresenter.toDTO(product)).toList();
    }

    public List<ProductResponseDTO> listAvailables() {
        ListAvailablesProductsUseCase listAvailablesUseCase = new ListAvailablesProductsUseCase(productGateway);
        List<Product> productsList = listAvailablesUseCase.execute();
        return productsList.stream().map(product -> ProductPresenter.toDTO(product)).toList();
    }

    public List<ProductResponseDTO> listByCategory(Category category) {
        ListProductsByCategoryUseCase listProductsByCategoryUseCase = new ListProductsByCategoryUseCase(productGateway);
        List<Product> productsList = listProductsByCategoryUseCase.execute(category);
        return productsList.stream().map(product -> ProductPresenter.toDTO(product)).toList();
    }

    public List<ProductResponseDTO> listAvailablesByCategory(Category category) {
        ListAvailablesProductsByCategoryUseCase listAvailablesProductsByCategoryUseCase = new ListAvailablesProductsByCategoryUseCase(productGateway);
        List<Product> productsList = listAvailablesProductsByCategoryUseCase.execute(category);
        return productsList.stream().map(product -> ProductPresenter.toDTO(product)).toList();
    }

    public void delete(UUID id) {
        DeleteProductByIdUseCase deleteProductByIdUseCase = new DeleteProductByIdUseCase(productGateway);
        deleteProductByIdUseCase.execute(id);
    }

    public void deleteByCategory(Category category) {
        DeleteProductsByCategoryUseCase deleteProductsByCategoryUseCase = new DeleteProductsByCategoryUseCase(productGateway);
        deleteProductsByCategoryUseCase.execute(category);
    }

    public List<Category> listCategorys() {
        return Arrays.asList(Category.values());
    }

}
