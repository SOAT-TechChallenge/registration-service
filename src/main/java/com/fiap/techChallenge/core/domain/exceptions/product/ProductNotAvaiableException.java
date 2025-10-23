package com.fiap.techChallenge.core.domain.exceptions.product;

import com.fiap.techChallenge.core.domain.exceptions.DomainException;

public class ProductNotAvaiableException extends DomainException {

    public ProductNotAvaiableException() {
        super("O produto informado não está disponivel");
    }

}
