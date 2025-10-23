package com.fiap.techChallenge.core.application.useCases.user.attendant;

import com.fiap.techChallenge.core.gateways.user.AttendantGateway;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;

import java.util.List;

public class ListAttendantsUseCase {

    private final AttendantGateway attendantGateway;

    public ListAttendantsUseCase(AttendantGateway attendantGateway) {
        this.attendantGateway = attendantGateway;
    }

    public List<Attendant> execute() {
        var attendants = this.attendantGateway.findAll();

        if (attendants == null || attendants.isEmpty()) {
            return List.of();
        }

        return attendants;
    }
}
