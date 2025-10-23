package com.fiap.techChallenge.core.application.useCases.user.attendant;

import com.fiap.techChallenge.core.gateways.user.AttendantGateway;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import com.fiap.techChallenge.core.domain.exceptions.user.UserAlreadyExistsException;
import com.fiap.techChallenge.core.application.dto.user.AttendantInputDTO;

public class CreateAttendantUseCase {

    private final AttendantGateway attendantGateway;

    public CreateAttendantUseCase(AttendantGateway attendantGateway) {
        this.attendantGateway = attendantGateway;
    }

    public Attendant execute(AttendantInputDTO attendantInputDTO) {
        var existingAttendant = this.attendantGateway.findFirstByCpf(attendantInputDTO.cpf());

        if (existingAttendant != null) {
            throw new UserAlreadyExistsException();
        }

        var attendant = Attendant.build(
                null,
                attendantInputDTO.name(),
                attendantInputDTO.email(),
                attendantInputDTO.cpf()
        );

        return this.attendantGateway.save(attendant);
    }
}
