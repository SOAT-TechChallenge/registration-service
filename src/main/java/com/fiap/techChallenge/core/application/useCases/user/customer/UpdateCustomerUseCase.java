package com.fiap.techChallenge.core.application.useCases.user.customer;

import com.fiap.techChallenge.core.gateways.user.CustomerGateway;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.application.dto.user.CustomerInputDTO;

import java.util.UUID;

public class UpdateCustomerUseCase {

    private final CustomerGateway customerGateway;

    public UpdateCustomerUseCase(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    public Customer execute(UUID id, CustomerInputDTO customerInputDTO) {
        var existingCustomer = this.customerGateway.findFirstById(id);

        if (existingCustomer == null) {
            throw new EntityNotFoundException("Customer");
        }

        var customer = Customer.build(
                id,
                customerInputDTO.name(),
                customerInputDTO.email(),
                customerInputDTO.cpf(),
                customerInputDTO.anonymous()
        );

        this.customerGateway.save(customer);
        return customer;
    }
}
