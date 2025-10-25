package org.gogame.server.repositories;

import org.gogame.server.domain.entities.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<ResultEntity, java.util.UUID> {

    Optional<ResultEntity> findByUuid(java.util.UUID uuid);
}
