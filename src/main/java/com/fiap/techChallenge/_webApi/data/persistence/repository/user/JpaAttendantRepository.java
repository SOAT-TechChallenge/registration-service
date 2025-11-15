package com.fiap.techChallenge._webApi.data.persistence.repository.user;

import com.fiap.techChallenge._webApi.data.persistence.entity.user.AttendantEntity;
import com.fiap.techChallenge._webApi.data.persistence.entity.user.CPFEmbeddable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAttendantRepository extends JpaRepository<AttendantEntity, UUID> {

    Optional<AttendantEntity> findByCpf_Number(String number);

    Optional<AttendantEntity> findFirstByCpf_Number(String number);

    AttendantEntity findFirstById(UUID id);
}