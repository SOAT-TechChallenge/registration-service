package com.fiap.techChallenge.core.application.useCases.user.customer;

import com.fiap.techChallenge.core.application.dto.user.CustomerInputDTO;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import com.fiap.techChallenge.core.domain.exceptions.user.UserAlreadyExistsException;
import com.fiap.techChallenge.core.gateways.user.CustomerGateway;

public class CreateCustomerUseCase {

    private final CustomerGateway customerGateway;

    public CreateCustomerUseCase(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    public Customer execute(CustomerInputDTO customerInputDTO) {
        var existingCustomer = this.customerGateway.findFirstByCpf(customerInputDTO.cpf());

        if (existingCustomer != null) {
            throw new UserAlreadyExistsException();
        }

        var customer = Customer.build(
                null,
                customerInputDTO.name(),
                customerInputDTO.email(),
                customerInputDTO.cpf(),
                customerInputDTO.anonymous()
        );

        return this.customerGateway.save(customer);
    }
}
