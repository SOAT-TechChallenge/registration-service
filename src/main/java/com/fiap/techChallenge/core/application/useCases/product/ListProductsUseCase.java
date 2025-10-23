package com.fiap.techChallenge.core.application.useCases.product;

import java.util.List;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class ListProductsUseCase {

    private final ProductGateway gateway;

    public ListProductsUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }

    public List<Product> execute() {
        return gateway.list();
    }
}
