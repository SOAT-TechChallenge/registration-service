package com.fiap.techChallenge._webApi.data.persistence.repository.user;

import java.util.List;

import com.fiap.techChallenge._webApi.data.persistence.entity.user.CPFEmbeddable;
import com.fiap.techChallenge._webApi.data.persistence.entity.user.CustomerEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaCustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    Optional<CustomerEntity> findByCpf(CPFEmbeddable cpf);
    List<CustomerEntity> findByAnonymousFalse();
    CustomerEntity findFirstByCpf(CPFEmbeddable cpf);
    CustomerEntity findFirstById(UUID id);
}
