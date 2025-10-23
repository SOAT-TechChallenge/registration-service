package com.fiap.techChallenge._webApi.mappers;

import com.fiap.techChallenge._webApi.data.persistence.entity.user.AttendantEntity;
import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;

public class AttendantMapper {

    public static Attendant DtoToDomain(AttendantDTO dto) {
        if (dto == null) {
            return null;
        }

        return Attendant.build(
                dto.id(),
                dto.name(),
                dto.email(),
                dto.cpf()
        );
    }

    public static AttendantDTO domainToDto(Attendant domain) {
        if (domain == null) {
            return null;
        }

        return AttendantDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .cpf(domain.getFormattedCpf())
                .email(domain.getEmail())
                .build();
    }

    public static AttendantDTO entityToDto(AttendantEntity attendantEntity) {
        if (attendantEntity == null) {
            return null;
        }

        return AttendantDTO.builder()
                .id(attendantEntity.getId())
                .name(attendantEntity.getName())
                .cpf(attendantEntity.getCpf().getNumber())
                .email(attendantEntity.getEmail())
                .build();
    }

    public static AttendantEntity dtoToEntity(AttendantDTO attendantDTO) {
        if (attendantDTO == null) {
            return null;
        }

        var attendantEntity = new AttendantEntity();
        attendantEntity.setId(attendantDTO.id());
        attendantEntity.setName(attendantDTO.name());
        attendantEntity.setEmail(attendantDTO.email());
        attendantEntity.setCpf(attendantDTO.cpf());

        return attendantEntity;
    }
}
