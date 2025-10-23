package com.fiap.techChallenge.core.application.dto.user;

public record CustomerInputDTO(
        String name,
        String email,
        String cpf,
        boolean anonymous
) {}
