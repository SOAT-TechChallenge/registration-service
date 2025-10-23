package com.fiap.techChallenge.core.gateways.user;

import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;

import java.util.List;
import java.util.UUID;

public interface AttendantGateway {
    Attendant save(Attendant attendant);
    Attendant findFirstById(UUID id);
    Attendant findFirstByCpf(String cpf);
    List<Attendant> findAll();
    void delete(UUID id);
}
