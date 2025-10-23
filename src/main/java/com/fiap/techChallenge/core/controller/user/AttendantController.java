package com.fiap.techChallenge.core.controller.user;

import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.application.dto.user.AttendantInputDTO;
import com.fiap.techChallenge.core.application.useCases.user.attendant.CreateAttendantUseCase;
import com.fiap.techChallenge.core.application.useCases.user.attendant.FindAttendantUseCase;
import com.fiap.techChallenge.core.application.useCases.user.attendant.ListAttendantsUseCase;
import com.fiap.techChallenge.core.application.useCases.user.attendant.UpdateAttendantUseCase;
import com.fiap.techChallenge.core.gateways.user.AttendantGateway;
import com.fiap.techChallenge.core.gateways.user.AttendantGatewayImpl;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import com.fiap.techChallenge.core.presenter.UserPresenter;

import java.util.List;
import java.util.UUID;

public class AttendantController {

    private final AttendantGateway attendantGateway;

    private AttendantController(CompositeDataSource compositeDataSource) {
        this.attendantGateway = new AttendantGatewayImpl(compositeDataSource);
    }

    public static AttendantController build(CompositeDataSource compositeDataSource) {
        return new AttendantController(compositeDataSource);
    }

    public AttendantDTO create(AttendantInputDTO attendantInputDTO) {
        var createAttendantUseCase = new CreateAttendantUseCase(attendantGateway);

        var attendant = createAttendantUseCase.execute(attendantInputDTO);
        return UserPresenter.toAttendantDTO(attendant);
    }

    public AttendantDTO update(UUID id, AttendantInputDTO attendantInputDTO) {
        var updateAttendantUseCase = new UpdateAttendantUseCase(attendantGateway);

        var attendant = updateAttendantUseCase.execute(id, attendantInputDTO);
        return UserPresenter.toAttendantDTO(attendant);
    }

    public AttendantDTO findByCpf(String cpf) {
        var findAttendantUseCase = new FindAttendantUseCase(attendantGateway);

        var attendant = findAttendantUseCase.execute(cpf);
        return UserPresenter.toAttendantDTO(attendant);
    }

    public AttendantDTO findById(UUID id) {
        var findAttendantUseCase = new FindAttendantUseCase(attendantGateway);

        var attendant = findAttendantUseCase.execute(id);
        return UserPresenter.toAttendantDTO(attendant);
    }

    public List<AttendantDTO> list() {
        var listAttendantsUseCase = new ListAttendantsUseCase(attendantGateway);

        var attendants = listAttendantsUseCase.execute();

        return attendants.stream().map(UserPresenter::toAttendantDTO).toList();
    }
}
