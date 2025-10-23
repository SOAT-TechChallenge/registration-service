package com.fiap.techChallenge.core.application.useCases.product;

import java.util.UUID;

import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class FindProductByIdUseCase {

    private final ProductGateway gateway;

    public FindProductByIdUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }

    public Product execute(UUID id) {
        Product product = gateway.findById(id);

        if (product == null) {
            throw new EntityNotFoundException("Product");
        }

        return product;
    }
}
