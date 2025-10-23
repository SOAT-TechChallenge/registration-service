package com.fiap.techChallenge.core.application.useCases.product;

import java.util.UUID;

import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class DeleteProductByIdUseCase {

    private final ProductGateway gateway;

    public DeleteProductByIdUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(UUID id) {
        Product product = gateway.findById(id);

        if (product == null) {
            throw new EntityNotFoundException("Product");
        }
        gateway.delete(id);
    }
}
