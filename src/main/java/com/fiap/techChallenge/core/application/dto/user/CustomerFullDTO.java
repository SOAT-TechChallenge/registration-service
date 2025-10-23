package com.fiap.techChallenge.core.application.dto.user;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CustomerFullDTO(
        UUID id,
        String name,
        String cpf,
        String email,
        boolean anonymous
) implements CustomerDTO {}
