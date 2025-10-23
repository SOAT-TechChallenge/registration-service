package com.fiap.techChallenge.core.application.useCases.product;

import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class DeleteProductsByCategoryUseCase {

    private final ProductGateway gateway;

    public DeleteProductsByCategoryUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(Category category) {
        gateway.deleteByCategory(category);
    }
}
