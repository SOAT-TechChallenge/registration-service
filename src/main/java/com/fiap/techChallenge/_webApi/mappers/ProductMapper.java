package com.fiap.techChallenge._webApi.mappers;

import com.fiap.techChallenge._webApi.data.persistence.entity.product.ProductEntity;
import com.fiap.techChallenge.core.application.dto.product.ProductDTO;

public class ProductMapper {

    public static ProductDTO toDTO(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProductDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .status(entity.getStatus())
                .image(entity.getImage())
                .build();
    }

    public static ProductEntity toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        ProductEntity entity = new ProductEntity();
        entity.setId(dto.id());
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setStatus(dto.status());
        entity.setCategory(dto.category());
        entity.setImage(dto.image());

        return entity;
    }
}
