package com.fiap.techChallenge.core.application.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String category,
        String status,
        String image
        ) {

}
