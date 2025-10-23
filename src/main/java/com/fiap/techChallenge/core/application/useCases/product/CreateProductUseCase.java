package com.fiap.techChallenge.core.application.useCases.product;

import com.fiap.techChallenge.core.application.dto.product.CreateProductInputDTO;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.domain.exceptions.product.NameAlreadyRegisteredException;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class CreateProductUseCase {

    private final ProductGateway gateway;

    public CreateProductUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }

    public Product execute(CreateProductInputDTO dto) {
        if (gateway.existisByName(dto.name())) {
            throw new NameAlreadyRegisteredException(dto.name());
        }

        Product product = Product.build(
            null,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category(),
                dto.status(),
                dto.image()
        );

        return gateway.save(product);
    }
}
