package com.fiap.techChallenge._webApi.data.persistence.repository.user;

import com.fiap.techChallenge._webApi.mappers.CustomerMapper;
import com.fiap.techChallenge._webApi.data.persistence.entity.user.CPFEmbeddable;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.interfaces.CustomerDataSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerDataSourceImpl implements CustomerDataSource {

    private final JpaCustomerRepository jpaCustomerRepository;

    public CustomerDataSourceImpl(JpaCustomerRepository jpaCustomerRepository) {
        this.jpaCustomerRepository = jpaCustomerRepository;
    }

    @Override
    public CustomerFullDTO save(CustomerFullDTO customerDTO) {
        var customerEntity = CustomerMapper.customerDtoToEntity(customerDTO);
        var newCustomer = jpaCustomerRepository.save(customerEntity);

        return CustomerMapper.customerEntityToDto(newCustomer);
    }

    @Override
    public CustomerFullDTO findFirstByCpf(String cpf) {
        var customerEntity = jpaCustomerRepository.findFirstByCpf(new CPFEmbeddable(cpf));

        if (customerEntity == null) {
            return null;
        }

        return CustomerMapper.customerEntityToDto(customerEntity);
    }

    @Override
    public CustomerFullDTO findFirstById(UUID id) {
        var customerEntity = jpaCustomerRepository.findFirstById(id);

        if (customerEntity == null) {
            return null;
        }

        return CustomerMapper.customerEntityToDto(customerEntity);
    }

    @Override
    public List<CustomerFullDTO> findAllNotAnonym() {
        var customerEntities = jpaCustomerRepository.findByAnonymousFalse();

        return customerEntities.stream().map(CustomerMapper::customerEntityToDto).toList();
    }

    @Override
    public void delete(UUID id) {
        jpaCustomerRepository.deleteById(id);
    }
}
