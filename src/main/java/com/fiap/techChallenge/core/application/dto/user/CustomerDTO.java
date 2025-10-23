package com.fiap.techChallenge.core.application.dto.user;

public sealed interface CustomerDTO permits CustomerFullDTO, CustomerAnonymDTO {}
