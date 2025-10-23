package com.fiap.techChallenge.core.gateways.user;

import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;

import java.util.List;
import java.util.UUID;

public class CustomerGatewayImpl implements CustomerGateway {

    private final CompositeDataSource compositeDataSource;

    public CustomerGatewayImpl(CompositeDataSource compositeDataSource) {
        this.compositeDataSource = compositeDataSource;
    }

    @Override
    public Customer save(Customer customer) {
        var cpf = customer.getUnformattedCpf() == null ? null : customer.getUnformattedCpf();
        var email = customer.getEmail() == null ? null : customer.getEmail();

        var customerDTO = new CustomerFullDTO(
                customer.getId(),
                customer.getName(),
                cpf,
                email,
                customer.isAnonymous()
        );

        var newCustomer = compositeDataSource.saveCustomer(customerDTO);

        return Customer.build(
                newCustomer.id(),
                newCustomer.name(),
                newCustomer.email(),
                newCustomer.cpf(),
                newCustomer.anonymous()
        );
    }


    @Override
    public Customer findFirstById(UUID id) {
        var customerDTO = compositeDataSource.findFirstCustomerById(id);

        if (customerDTO == null) {
            return null;
        }

        return Customer.build(
                customerDTO.id(),
                customerDTO.name(),
                customerDTO.email(),
                customerDTO.cpf(),
                customerDTO.anonymous()
        );
    }

    @Override
    public Customer findFirstByCpf(String cpf) {
        var customerDTO = compositeDataSource.findFirstCustomerByCpf(cpf);

        if (customerDTO == null) {
            return null;
        }

        return Customer.build(
                customerDTO.id(),
                customerDTO.name(),
                customerDTO.email(),
                customerDTO.cpf(),
                customerDTO.anonymous()
        );
    }

    @Override
    public List<Customer> findAllNotAnonymous() {
        var customerDtoList = compositeDataSource.findAllCustomerNotAnonym();

        return customerDtoList.stream()
                .map(customerDTO -> Customer.build(
                        customerDTO.id(),
                        customerDTO.name(),
                        customerDTO.email(),
                        customerDTO.cpf(),
                        customerDTO.anonymous()
                ))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        compositeDataSource.deleteCustomer(id);
    }
}
