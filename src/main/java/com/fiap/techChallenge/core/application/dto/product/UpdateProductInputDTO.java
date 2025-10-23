package com.fiap.techChallenge.core.application.dto.product;

import java.math.BigDecimal;

import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateProductInputDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Category category,
        ProductStatus status,
        String image
        ) {

}
