package com.fiap.techChallenge.core.application.dto.user;

import java.util.UUID;

public record CustomerAnonymDTO(
        UUID id,
        String name,
        boolean anonymous
) implements CustomerDTO {}
