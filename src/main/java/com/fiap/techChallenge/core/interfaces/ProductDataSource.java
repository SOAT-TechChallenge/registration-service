package com.fiap.techChallenge.core.interfaces;

import java.util.List;
import java.util.UUID;

import com.fiap.techChallenge.core.application.dto.product.ProductDTO;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;

public interface ProductDataSource {

    ProductDTO save(ProductDTO product);

    ProductDTO findById(UUID id);

    ProductDTO findByName(String name);

    List<Category> listAvailableCategorys();

    List<ProductDTO> list();

    List<ProductDTO> listByCategory(Category category);

    List<ProductDTO> listByStatusAndCategory(ProductStatus status, Category category);

    List<ProductDTO> listByStatus(ProductStatus status);

    void delete(UUID id);

    void deleteByCategory(Category category);
}
