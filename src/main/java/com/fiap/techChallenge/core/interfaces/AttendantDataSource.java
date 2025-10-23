package com.fiap.techChallenge.core.interfaces;

import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;

import java.util.List;
import java.util.UUID;

public interface AttendantDataSource {

    AttendantDTO save(AttendantDTO attendantDTO);
    AttendantDTO findFirstByCpf(String cpf);
    AttendantDTO findFirstById(UUID id);
    List<AttendantDTO> findAll();
    void delete(UUID id);
}
