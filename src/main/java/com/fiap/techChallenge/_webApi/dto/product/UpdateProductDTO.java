package com.fiap.techChallenge._webApi.dto.product;

import java.math.BigDecimal;

import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProductDTO(
        @NotNull
        UUID id,
        @NotBlank(message = "Nome é obrigatório")
        String name,
        @NotBlank(message = "Descrição é obrigatório")
        String description,
        @NotNull
        BigDecimal price,
        @NotNull
        Category category,
        @NotNull
        ProductStatus status,
        @NotBlank(message = "Imagem é obrigatória")
        String image
        ) {

}
