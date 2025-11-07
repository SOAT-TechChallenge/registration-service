package com.fiap.techChallenge.core.application.useCases.product;

import java.util.List;

import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class ListAvaiableCategoriesUseCase {
    
    private final ProductGateway gateway;

    public ListAvaiableCategoriesUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }
    
    public List<Category> execute() {
        return gateway.listAvailableCategorys();
    }

}
