package com.fiap.techChallenge.core.domain.exceptions.user;

import com.fiap.techChallenge.core.domain.exceptions.DomainException;

public class UserAlreadyExistsException extends DomainException {
    public UserAlreadyExistsException() {
        super("Este usuário já existe.");
    }
}
