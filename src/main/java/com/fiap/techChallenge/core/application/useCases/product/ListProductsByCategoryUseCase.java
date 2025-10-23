package com.fiap.techChallenge.core.application.useCases.product;

import java.util.List;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class ListProductsByCategoryUseCase {

    private final ProductGateway gateway;

    public ListProductsByCategoryUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }

    public List<Product> execute(Category category) {
        return gateway.listByCategory(category);
    }

}
