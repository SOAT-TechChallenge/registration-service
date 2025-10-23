package com.fiap.techChallenge.core.application.useCases.product;

import java.util.List;

import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class ListAvailablesProductsByCategoryUseCase {

    private final ProductGateway gateway;

    public ListAvailablesProductsByCategoryUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }
    
    public List<Product> execute(Category category) {
        return gateway.listByStatusAndCategory(ProductStatus.DISPONIVEL, category);
    }
}
