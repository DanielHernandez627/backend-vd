package com.back.vd.infrastructure.persistence.repository;

import com.back.vd.infrastructure.persistence.entity.SeasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonJpaRepository extends JpaRepository<SeasonEntity, Long> {
}
