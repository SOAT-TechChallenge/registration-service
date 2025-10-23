package com.fiap.techChallenge.core.presenter;

import com.fiap.techChallenge.core.application.dto.product.ProductResponseDTO;
import com.fiap.techChallenge.core.domain.entities.product.Product;

public class ProductPresenter {

    public static ProductResponseDTO toDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().toString(),
                product.getStatus().toString(),
                product.getImage());
    }
}
