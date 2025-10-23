package com.fiap.techChallenge.core.gateways.product;

import java.util.List;
import java.util.UUID;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;

public interface ProductGateway {

    Product save(Product product);

    Product findById(UUID id);

    Product findByName(String name);

    List<Category> listAvailableCategorys();

    List<Product> list();

    List<Product> listByCategory(Category category);

    List<Product> listByStatusAndCategory(ProductStatus status, Category category);

    List<Product> listByStatus(ProductStatus status);

    void delete(UUID id);

    void deleteByCategory(Category category);

    boolean existisByName(String name);
}
