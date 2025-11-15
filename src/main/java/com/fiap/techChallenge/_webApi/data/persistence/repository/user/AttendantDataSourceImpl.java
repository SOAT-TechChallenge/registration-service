package com.fiap.techChallenge._webApi.data.persistence.repository.user;

import com.fiap.techChallenge._webApi.mappers.AttendantMapper;
import com.fiap.techChallenge._webApi.data.persistence.entity.user.CPFEmbeddable;
import com.fiap.techChallenge.core.interfaces.AttendantDataSource;
import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AttendantDataSourceImpl implements AttendantDataSource {

    private final JpaAttendantRepository repository;

    public AttendantDataSourceImpl(JpaAttendantRepository repository) {
        this.repository = repository;
    }

    @Override
    public AttendantDTO save(AttendantDTO attendantDTO) {
        var entity = AttendantMapper.dtoToEntity(attendantDTO);
        var saved = repository.save(entity);
        return AttendantMapper.entityToDto(saved);
    }

    @Override
    public AttendantDTO findFirstByCpf(String cpf) {
        return repository.findFirstByCpf_Number(cleanCpf(cpf))
                .map(AttendantMapper::entityToDto)
                .orElse(null);
    }

    @Override
    public AttendantDTO findFirstById(UUID id) {
        return repository.findFirstById(id) != null
                ? AttendantMapper.entityToDto(repository.findFirstById(id))
                : null;
    }

    @Override
    public List<AttendantDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(AttendantMapper::entityToDto)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private String cleanCpf(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }
}
