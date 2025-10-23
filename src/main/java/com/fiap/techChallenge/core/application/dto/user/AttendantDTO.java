package com.fiap.techChallenge.core.application.dto.user;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AttendantDTO(
        UUID id,
        String name,
        String email,
        String cpf
) {}
