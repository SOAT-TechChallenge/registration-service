package com.fiap.techChallenge._webApi.mappers;

import com.fiap.techChallenge._webApi.data.persistence.entity.user.CustomerEntity;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;

public class CustomerMapper {

    public static Customer customerDtoToDomain(CustomerFullDTO dto) {
        return Customer.build(
                dto.id(),
                dto.name(),
                dto.email(),
                dto.cpf(),
                dto.anonymous()
        );
    }

    public static CustomerFullDTO customerDomainToDto(Customer customer) {
        return CustomerFullDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .cpf(customer.getFormattedCpf())
                .email(customer.getEmail())
                .anonymous(customer.isAnonymous())
                .build();
    }

    public static CustomerFullDTO customerEntityToDto(CustomerEntity customerEntity) {
        var cpf = customerEntity.getCpf() != null ? customerEntity.getCpf().getNumber() : null;
        var email = customerEntity.getEmail() != null ? customerEntity.getEmail() : null;
        var name = customerEntity.getName() != null ? customerEntity.getName() : null;

        return CustomerFullDTO.builder()
                .id(customerEntity.getId())
                .name(name)
                .cpf(cpf)
                .email(email)
                .anonymous(customerEntity.isAnonymous())
                .build();
    }

    public static CustomerEntity customerDtoToEntity(CustomerFullDTO customerDTO) {
        var customerEntity = new CustomerEntity();
        customerEntity.setId(customerDTO.id());
        customerEntity.setName(customerDTO.name());
        customerEntity.setEmail(customerDTO.email());
        customerEntity.setCpf(customerDTO.cpf());
        customerEntity.setAnonymous(customerDTO.anonymous());

        return customerEntity;
    }
}
