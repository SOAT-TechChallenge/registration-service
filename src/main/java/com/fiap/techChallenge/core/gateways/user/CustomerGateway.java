package com.fiap.techChallenge.core.gateways.user;

import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerGateway {
    Customer save(Customer attendant);
    Customer findFirstById(UUID id);
    Customer findFirstByCpf(String cpf);
    List<Customer> findAllNotAnonymous();
    void delete(UUID id);
}
