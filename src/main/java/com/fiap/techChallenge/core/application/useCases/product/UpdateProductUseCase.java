package com.fiap.techChallenge.core.application.useCases.product;

import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.domain.exceptions.product.NameAlreadyRegisteredException;
import com.fiap.techChallenge.core.application.dto.product.UpdateProductInputDTO;
import com.fiap.techChallenge.core.domain.entities.product.Product;
import com.fiap.techChallenge.core.gateways.product.ProductGateway;

public class UpdateProductUseCase {

    private final ProductGateway gateway;

    public UpdateProductUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }

    public Product execute(UpdateProductInputDTO dto) {
        Product productToUpdate = gateway.findById(dto.id());

        if (productToUpdate == null) {
            throw new EntityNotFoundException("Product");
        }

        if (!productToUpdate.getName().equals(dto.name())) {
            Product productWithSameName = gateway.findByName(dto.name());

            if (productWithSameName != null && !productWithSameName.getId().equals(productToUpdate.getId())) {
                throw new NameAlreadyRegisteredException(dto.name());
            }
        }

        productToUpdate.setName(dto.name());
        productToUpdate.setDescription(dto.description());
        productToUpdate.setPrice(dto.price());
        productToUpdate.setCategory(dto.category());
        productToUpdate.setStatus(dto.status());
        productToUpdate.setImage(dto.image());

        return gateway.save(productToUpdate);
    }
}
