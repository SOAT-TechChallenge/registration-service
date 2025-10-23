package com.fiap.techChallenge.core.application.useCases.user.customer;

import com.fiap.techChallenge.core.gateways.user.CustomerGateway;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;

import java.util.List;

public class ListNotAnonymCustomersUseCase {

    private final CustomerGateway customerGateway;

    public ListNotAnonymCustomersUseCase(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    public List<Customer> execute() {
        var customers = customerGateway.findAllNotAnonymous();

        if (customers == null || customers.isEmpty()) {
            return List.of();
        }

        return customers;
    }
}
