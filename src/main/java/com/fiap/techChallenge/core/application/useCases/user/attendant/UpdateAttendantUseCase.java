package com.fiap.techChallenge.core.application.useCases.user.attendant;

import com.fiap.techChallenge.core.gateways.user.AttendantGateway;
import com.fiap.techChallenge.core.domain.exceptions.EntityNotFoundException;
import com.fiap.techChallenge.core.application.dto.user.AttendantInputDTO;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;

import java.util.UUID;

public class UpdateAttendantUseCase {

    private final AttendantGateway attendantGateway;

    public UpdateAttendantUseCase(AttendantGateway attendantGateway) {
        this.attendantGateway = attendantGateway;
    }

    public Attendant execute(UUID id, AttendantInputDTO attendantInputDTO) {
        var existingAttendant = this.attendantGateway.findFirstById(id);

        if (existingAttendant == null) {
            throw new EntityNotFoundException("Attendant");
        }

        var attendant = Attendant.build(
                id,
                attendantInputDTO.name(),
                attendantInputDTO.email(),
                attendantInputDTO.cpf()
        );

        this.attendantGateway.save(attendant);
        return attendant;
    }
}
