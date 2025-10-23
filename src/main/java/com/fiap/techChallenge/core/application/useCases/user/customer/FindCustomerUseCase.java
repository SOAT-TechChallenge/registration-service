package com.fiap.techChallenge.core.application.useCases.user.customer;

import com.fiap.techChallenge.core.gateways.user.CustomerGateway;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;

import java.util.UUID;

public class FindCustomerUseCase {

    private final CustomerGateway customerGateway;

    public FindCustomerUseCase(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    public Customer execute(String cpf) {
        Customer customer = customerGateway.findFirstByCpf(cpf);

        if (customer == null) {
            throw new EntityNotFoundException("Customer");
        }

        return customer;
    }

    public Customer execute(UUID id) {
        Customer customer = customerGateway.findFirstById(id);

        if (customer == null) {
            throw new EntityNotFoundException("Customer");
        }

        return customer;
    }
}
