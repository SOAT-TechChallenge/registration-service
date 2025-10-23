package com.fiap.techChallenge.core.application.useCases.product;

import java.util.List;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class ListAvailablesProductsUseCase {
    
    private final ProductGateway gateway;

    public ListAvailablesProductsUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }


    public List<Product> execute() {
        return gateway.listByStatus(ProductStatus.DISPONIVEL);
    }
}
