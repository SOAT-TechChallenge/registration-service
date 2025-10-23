package com.fiap.techChallenge.core.gateways.product;

import com.fiap.techChallenge.core.application.dto.product.ProductDTO;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ProductGatewayImpl implements ProductGateway {

    private final CompositeDataSource dataSource;

    public ProductGatewayImpl(CompositeDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Product save(Product product) {
        ProductDTO dto = new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStatus(),
                product.getImage()
        );

        dto = dataSource.saveProduct(dto);

        product = Product.build(
                dto.id(),
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category(),
                dto.status(),
                dto.image()
        );

        return product;
    }

    @Override
    public Product findById(UUID id) {
        ProductDTO dto = dataSource.findProductById(id);

        if (dto == null) {
            return null;
        }

        return Product.build(
                dto.id(),
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category(),
                dto.status(),
                dto.image()
        );
    }

    @Override
    public Product findByName(String name) {
        ProductDTO dto = dataSource.findProductByName(name);

        if (dto == null) {
            return null;
        }

        return Product.build(
                dto.id(),
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category(),
                dto.status(),
                dto.image()
        );
    }

    @Override
    public List<Product> list() {
        List<ProductDTO> dtoList = dataSource.listProducts();

        return dtoList.stream().map(dto -> Product.build(
                dto.id(),
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category(),
                dto.status(),
                dto.image()
        )).toList();
    }

    @Override
    public List<Product> listByStatus(ProductStatus status) {
        List<ProductDTO> dtoList = dataSource.listProductsByStatus(status);

        return dtoList.stream().map(dto -> Product.build(
                dto.id(),
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category(),
                dto.status(),
                dto.image()
        )).toList();
    }

    @Override
    public List<Product> listByStatusAndCategory(ProductStatus status, Category category) {
        List<ProductDTO> dtoList = dataSource.listProductsByStatusAndCategory(status, category);

        return dtoList.stream().map(dto -> Product.build(
                dto.id(),
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category(),
                dto.status(),
                dto.image()
        )).toList();
    }

    @Override
    public List<Product> listByCategory(Category category) {
        List<ProductDTO> dtoList = dataSource.listProductsByCategory(category);

        return dtoList.stream().map(dto -> Product.build(
                dto.id(),
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category(),
                dto.status(),
                dto.image()
        )).toList();
    }

    @Override
    public List<Category> listAvailableCategorys() {
        List<Category> availableCategories = dataSource.listAvailableProductCategories();

        return Arrays.stream(Category.values())
                .filter(availableCategories::contains)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        dataSource.deleteProduct(id);
    }

    @Override
    public void deleteByCategory(Category category) {
        dataSource.deleteProductByCategory(category);
    }

    @Override
    public boolean existisByName(String name) {
        Product existingProduct = this.findByName(name);

        return existingProduct != null && existingProduct.getId() != null;
    }
}
