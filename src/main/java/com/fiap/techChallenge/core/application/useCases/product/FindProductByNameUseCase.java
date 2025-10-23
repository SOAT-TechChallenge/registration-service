package com.fiap.techChallenge.core.application.useCases.product;

import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class FindProductByNameUseCase {

    private final ProductGateway gateway;

    public FindProductByNameUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }

    public Product execute(String name) {
        Product product = gateway.findByName(name);

        if (product == null) {
            throw new EntityNotFoundException("Product");
        }

        return product;
    }
}
