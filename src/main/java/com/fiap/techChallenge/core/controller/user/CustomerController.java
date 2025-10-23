package com.fiap.techChallenge.core.controller.user;

import com.fiap.techChallenge.core.application.dto.user.CustomerDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerInputDTO;
import com.fiap.techChallenge.core.application.useCases.user.customer.CreateCustomerUseCase;
import com.fiap.techChallenge.core.application.useCases.user.customer.FindCustomerUseCase;
import com.fiap.techChallenge.core.application.useCases.user.customer.ListNotAnonymCustomersUseCase;
import com.fiap.techChallenge.core.application.useCases.user.customer.UpdateCustomerUseCase;
import com.fiap.techChallenge.core.gateways.user.CustomerGateway;
import com.fiap.techChallenge.core.gateways.user.CustomerGatewayImpl;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import com.fiap.techChallenge.core.presenter.UserPresenter;

import java.util.List;
import java.util.UUID;

public class CustomerController {

    private final CustomerGateway customerGateway;

    private CustomerController(CompositeDataSource compositeDataSource) {
        this.customerGateway = new CustomerGatewayImpl(compositeDataSource);
    }

    public static CustomerController build(CompositeDataSource compositeDataSource) {
        return new CustomerController(compositeDataSource);
    }

    public CustomerDTO create(CustomerInputDTO customerInputDTO) {
        var createCustomerUseCase = new CreateCustomerUseCase(customerGateway);

        var customer = createCustomerUseCase.execute(customerInputDTO);

        if (customer.isAnonymous()) {
            return UserPresenter.toAnonymCustomerDTO(customer);
        }

        return UserPresenter.toCustomerDTO(customer);
    }

    public CustomerDTO update(UUID id, CustomerInputDTO customerInputDTO) {
        var updateCustomerUseCase = new UpdateCustomerUseCase(customerGateway);

        var customer = updateCustomerUseCase.execute(id, customerInputDTO);

        if (customer.isAnonymous()) {
            return UserPresenter.toAnonymCustomerDTO(customer);
        }

        return UserPresenter.toCustomerDTO(customer);
    }

    public CustomerDTO findByCpf(String cpf) {
        var findCustomerUseCase = new FindCustomerUseCase(customerGateway);

        var customer = findCustomerUseCase.execute(cpf);

        if (customer.isAnonymous()) {
            return UserPresenter.toAnonymCustomerDTO(customer);
        }

        return UserPresenter.toCustomerDTO(customer);
    }

    public CustomerDTO findById(UUID id) {
        var findCustomerUseCase = new FindCustomerUseCase(customerGateway);

        var customer = findCustomerUseCase.execute(id);

        if (customer.isAnonymous()) {
            return UserPresenter.toAnonymCustomerDTO(customer);
        }

        return UserPresenter.toCustomerDTO(customer);
    }

    public List<CustomerFullDTO> listNotAnonym() {
        var listCustomersUseCase = new ListNotAnonymCustomersUseCase(customerGateway);

        var customers = listCustomersUseCase.execute();

        return customers.stream().map(UserPresenter::toCustomerDTO).toList();
    }
}
