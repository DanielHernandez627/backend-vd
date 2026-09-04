package com.back.vd.infrastructure.persistence.adapter;

import com.back.vd.domain.model.Episode;
import com.back.vd.domain.model.MediaContent;
import com.back.vd.domain.model.Season;
import com.back.vd.domain.ports.output.MediaContentRepositoryPort;
import com.back.vd.infrastructure.persistence.entity.EpisodeEntity;
import com.back.vd.infrastructure.persistence.entity.MediaContentEntity;
import com.back.vd.infrastructure.persistence.entity.SeasonEntity;
import com.back.vd.infrastructure.persistence.mapper.MediaContentEntityMapper;
import com.back.vd.infrastructure.persistence.repository.EpisodeJpaRepository;
import com.back.vd.infrastructure.persistence.repository.MediaContentJpaRepository;
import com.back.vd.infrastructure.persistence.repository.SeasonJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MediaContentRepositoryAdapter implements MediaContentRepositoryPort {

    private final MediaContentJpaRepository mediaContentJpaRepository;
    private final SeasonJpaRepository seasonJpaRepository;
    private final EpisodeJpaRepository episodeJpaRepository;
    private final MediaContentEntityMapper mapper;

    public MediaContentRepositoryAdapter(MediaContentJpaRepository mediaContentJpaRepository,
                                         SeasonJpaRepository seasonJpaRepository,
                                         EpisodeJpaRepository episodeJpaRepository,
                                         MediaContentEntityMapper mapper) {
        this.mediaContentJpaRepository = mediaContentJpaRepository;
        this.seasonJpaRepository = seasonJpaRepository;
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
    public Optional<Season> findSeasonById(Long seasonId) {
        return seasonJpaRepository.findById(seasonId)
                .map(mapper::seasonToDomain);
    }

    @Override
    public Optional<MediaContent> findMediaContentBySeasonId(Long seasonId) {
        return seasonJpaRepository.findById(seasonId)
                .map(SeasonEntity::getMediaContent)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Episode> findEpisodeById(Long episodeId) {
        return episodeJpaRepository.findById(episodeId)
                .map(mapper::episodeToDomain);
    }

    @Override
    public Optional<Season> findSeasonByEpisodeId(Long episodeId) {
        Optional<EpisodeEntity> episodeEntity = episodeJpaRepository.findById(episodeId);
        if (episodeEntity.isPresent() && episodeEntity.get().getSeason() != null) {
            return Optional.of(mapper.seasonToDomain(episodeEntity.get().getSeason()));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        mediaContentJpaRepository.deleteById(id);
    }
}
