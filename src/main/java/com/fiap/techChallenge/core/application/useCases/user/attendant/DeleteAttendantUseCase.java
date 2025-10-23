package com.fiap.techChallenge.core.application.useCases.user.attendant;

import com.fiap.techChallenge.core.gateways.user.AttendantGateway;
import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;

public class DeleteAttendantUseCase {

    private final AttendantGateway attendantGateway;

    public DeleteAttendantUseCase(AttendantGateway attendantGateway) {
        this.attendantGateway = attendantGateway;
    }

    public void execute(String cpf) {
        var attendant = this.attendantGateway.findFirstByCpf(cpf);

        if (attendant == null) {
            throw new EntityNotFoundException("Attendant");
        }

        this.attendantGateway.delete(attendant.getId());
    }
}
