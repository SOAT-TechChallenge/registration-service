package com.fiap.techChallenge.core.application.useCases.user.attendant;

import com.fiap.techChallenge.core.gateways.user.AttendantGateway;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;

import java.util.UUID;

public class FindAttendantUseCase {

    private final AttendantGateway attendantGateway;

    public FindAttendantUseCase(AttendantGateway attendantGateway) {
        this.attendantGateway = attendantGateway;
    }

    public Attendant execute(String cpf) {
        Attendant attendant = attendantGateway.findFirstByCpf(cpf);

        if (attendant == null) {
            throw new EntityNotFoundException("Attendant");
        }

        return attendant;
    }

    public Attendant execute(UUID id) {
        Attendant attendant = attendantGateway.findFirstById(id);

        if (attendant == null) {
            throw new EntityNotFoundException("Attendant");
        }

        return attendant;
    }
}
