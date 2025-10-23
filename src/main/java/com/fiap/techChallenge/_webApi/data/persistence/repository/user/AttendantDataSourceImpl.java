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

    private final JpaAttendantRepository jpaAttendantRepository;

    public AttendantDataSourceImpl(JpaAttendantRepository jpaAttendantRepository) {
        this.jpaAttendantRepository = jpaAttendantRepository;
    }

    @Override
    public AttendantDTO save(AttendantDTO attendantDTO) {
        var attendantEntity = AttendantMapper.dtoToEntity(attendantDTO);
        var newAttendant = jpaAttendantRepository.save(attendantEntity);

        return AttendantMapper.entityToDto(newAttendant);
    }

    @Override
    public AttendantDTO findFirstByCpf(String cpf) {
        var attendantEntity = jpaAttendantRepository.findFirstByCpf(new CPFEmbeddable(cpf));

        if (attendantEntity == null) {
            return null;
        }

        return AttendantMapper.entityToDto(attendantEntity);
    }

    @Override
    public AttendantDTO findFirstById(UUID id) {
        var attendantEntity = jpaAttendantRepository.findFirstById(id);

        if (attendantEntity == null) {
            return null;
        }

        return AttendantMapper.entityToDto(attendantEntity);
    }

    @Override
    public List<AttendantDTO> findAll() {
        var attendantEntities = jpaAttendantRepository.findAll();

        return attendantEntities.stream().map(AttendantMapper::entityToDto).toList();
    }

    @Override
    public void delete(UUID id) {
        jpaAttendantRepository.deleteById(id);
    }
}
