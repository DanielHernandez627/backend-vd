package com.back.vd.infrastructure.persistence.adapter;

import com.back.vd.domain.model.Episode;
import com.back.vd.domain.model.MediaContent;
import com.back.vd.domain.ports.output.MediaContentRepositoryPort;
import com.back.vd.infrastructure.persistence.entity.MediaContentEntity;
import com.back.vd.infrastructure.persistence.mapper.MediaContentEntityMapper;
import com.back.vd.infrastructure.persistence.repository.EpisodeJpaRepository;
import com.back.vd.infrastructure.persistence.repository.MediaContentJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MediaContentRepositoryAdapter implements MediaContentRepositoryPort {

    private final MediaContentJpaRepository mediaContentJpaRepository;
    private final EpisodeJpaRepository episodeJpaRepository;
    private final MediaContentEntityMapper mapper;

    public MediaContentRepositoryAdapter(MediaContentJpaRepository mediaContentJpaRepository,
                                         EpisodeJpaRepository episodeJpaRepository,
                                         MediaContentEntityMapper mapper) {
        this.mediaContentJpaRepository = mediaContentJpaRepository;
        this.episodeJpaRepository = episodeJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public MediaContent save(MediaContent mediaContent) {
        MediaContentEntity entity = mapper.toEntity(mediaContent);
        MediaContentEntity savedEntity = mediaContentJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<MediaContent> findById(Long id) {
        return mediaContentJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<MediaContent> findAll() {
        return mediaContentJpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Episode> findEpisodeById(Long episodeId) {
        return episodeJpaRepository.findById(episodeId)
                .map(mapper::episodeToDomain);
    }

    @Override
    public void deleteById(Long id) {
        mediaContentJpaRepository.deleteById(id);
    }
}
