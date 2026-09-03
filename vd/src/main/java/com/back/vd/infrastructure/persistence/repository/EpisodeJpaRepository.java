package com.back.vd.infrastructure.persistence.repository;

import com.back.vd.infrastructure.persistence.entity.EpisodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeJpaRepository extends JpaRepository<EpisodeEntity, Long> {
}
