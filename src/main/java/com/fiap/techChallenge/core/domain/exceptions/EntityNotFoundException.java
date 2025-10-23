package com.fiap.techChallenge.core.domain.exceptions;

public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String entityName) {
        super(String.format("Registro não encontrado: " + entityName));
    }
}
