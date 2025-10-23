package com.fiap.techChallenge.core.application.services.product;

import java.util.UUID;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.domain.exceptions.product.ProductNotAvaiableException;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class ProductAvailabilityService {

    private final ProductGateway gateway;

    public ProductAvailabilityService(ProductGateway gateway) {
        this.gateway = gateway;
    }

    public Product findAvailableProduct(UUID productId) {
        Product product = gateway.findById(productId);

        if (product == null || product.getStatus().equals(ProductStatus.INDISPONIVEL)) {
            throw new ProductNotAvaiableException();
        }

        return product;
    }
}
