package com.fiap.techChallenge.core.application.useCases.user.customer;

import com.fiap.techChallenge.core.gateways.user.CustomerGateway;
import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;

import java.util.UUID;

public class DeleteCustomerUseCase {

    private final CustomerGateway customerGateway;

    public DeleteCustomerUseCase(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    public void execute(UUID id) {
        var customer = customerGateway.findFirstById(id);

        if (customer == null) {
            throw new EntityNotFoundException("Customer");
        }

        this.customerGateway.delete(id);
    }
}
