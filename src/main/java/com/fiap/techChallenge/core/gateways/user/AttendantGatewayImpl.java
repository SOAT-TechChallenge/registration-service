package com.fiap.techChallenge.core.gateways.user;

import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;

import java.util.List;
import java.util.UUID;

public class AttendantGatewayImpl implements AttendantGateway {

    private final CompositeDataSource compositeDataSource;

    public AttendantGatewayImpl(CompositeDataSource compositeDataSource) {
        this.compositeDataSource = compositeDataSource;
    }

    @Override
    public Attendant save(Attendant attendant) {
        var attendantDto = new AttendantDTO(
                attendant.getId(),
                attendant.getName(),
                attendant.getEmail(),
                attendant.getUnformattedCpf()
        );

        var newAttendant = compositeDataSource.saveAttendant(attendantDto);

        return Attendant.build(
                newAttendant.id(),
                newAttendant.name(),
                newAttendant.email(),
                newAttendant.cpf()
        );
    }


    @Override
    public Attendant findFirstById(UUID id) {
        var attendantDto = compositeDataSource.findFirstAttendantById(id);

        if (attendantDto == null) {
            return null;
        }

        return Attendant.build(
                attendantDto.id(),
                attendantDto.name(),
                attendantDto.email(),
                attendantDto.cpf()
        );
    }

    @Override
    public Attendant findFirstByCpf(String cpf) {
        var attendantDto = compositeDataSource.findFirstAttendantByCpf(cpf);

        if (attendantDto == null) {
            return null;
        }

        return Attendant.build(
                attendantDto.id(),
                attendantDto.name(),
                attendantDto.email(),
                attendantDto.cpf()
        );
    }

    @Override
    public List<Attendant> findAll() {
        var attendantDtoList = compositeDataSource.findAllAttendants();

        return attendantDtoList.stream()
                .map(attendantDto -> Attendant.build(
                        attendantDto.id(),
                        attendantDto.name(),
                        attendantDto.email(),
                        attendantDto.cpf()
                ))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        compositeDataSource.deleteAttendant(id);
    }
}
