package com.fiap.techChallenge.core.interfaces;

import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;

import java.util.List;
import java.util.UUID;

public interface CustomerDataSource {

    CustomerFullDTO save(CustomerFullDTO attendantDTO);
    CustomerFullDTO findFirstByCpf(String cpf);
    CustomerFullDTO findFirstById(UUID id);
    List<CustomerFullDTO> findAllNotAnonym();
    void delete(UUID id);
}
