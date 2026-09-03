package com.back.vd.infrastructure.persistence.repository;

import com.back.vd.infrastructure.persistence.entity.MediaContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaContentJpaRepository extends JpaRepository<MediaContentEntity, Long> {
}
