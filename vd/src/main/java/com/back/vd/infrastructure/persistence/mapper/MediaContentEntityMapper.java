package com.back.vd.infrastructure.persistence.mapper;

import com.back.vd.domain.model.Episode;
import com.back.vd.domain.model.MediaContent;
import com.back.vd.domain.model.Season;
import com.back.vd.domain.model.SkipTimestamp;
import com.back.vd.infrastructure.persistence.entity.EpisodeEntity;
import com.back.vd.infrastructure.persistence.entity.MediaContentEntity;
import com.back.vd.infrastructure.persistence.entity.SeasonEntity;
import com.back.vd.infrastructure.persistence.entity.SkipTimestampEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MediaContentEntityMapper {

    public MediaContent toDomain(MediaContentEntity entity) {
        if (entity == null) return null;

        List<Season> seasons = entity.getSeasons() != null ?
                entity.getSeasons().stream()
                        .map(this::seasonToDomain)
                        .sorted(Comparator.comparing(Season::getSeasonNumber, Comparator.nullsLast(Comparator.naturalOrder())))
                        .collect(Collectors.toList()) : new ArrayList<>();

        return new MediaContent(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCoverImageUrl(),
                entity.getType(),
                entity.getCreatedAt(),
                seasons
        );
    }

    public MediaContentEntity toEntity(MediaContent domain) {
        if (domain == null) return null;

        MediaContentEntity entity = new MediaContentEntity();
        entity.setId(domain.getId());
        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        entity.setCoverImageUrl(domain.getCoverImageUrl());
        entity.setType(domain.getType());
        entity.setCreatedAt(domain.getCreatedAt());

        if (domain.getSeasons() != null) {
            List<SeasonEntity> seasonEntities = domain.getSeasons().stream().map(s -> {
                SeasonEntity se = seasonToEntity(s);
                se.setMediaContent(entity);
                return se;
            }).collect(Collectors.toList());
            entity.setSeasons(seasonEntities);
        }

        return entity;
    }

    public Season seasonToDomain(SeasonEntity entity) {
        if (entity == null) return null;

        List<Episode> episodes = entity.getEpisodes() != null ?
                entity.getEpisodes().stream()
                        .map(this::episodeToDomain)
                        .sorted(Comparator.comparing(Episode::getEpisodeNumber, Comparator.nullsLast(Comparator.naturalOrder())))
                        .collect(Collectors.toList()) : new ArrayList<>();

        List<SkipTimestamp> defaultSkipTimestamps = entity.getDefaultSkipTimestamps() != null ?
                entity.getDefaultSkipTimestamps().stream().map(this::skipTimestampToDomain).collect(Collectors.toList()) : new ArrayList<>();

        return new Season(
                entity.getId(),
                entity.getSeasonNumber(),
                entity.getTitle(),
                episodes,
                defaultSkipTimestamps
        );
    }

    public SeasonEntity seasonToEntity(Season domain) {
        if (domain == null) return null;

        SeasonEntity entity = new SeasonEntity();
        entity.setId(domain.getId());
        entity.setSeasonNumber(domain.getSeasonNumber());
        entity.setTitle(domain.getTitle());

        if (domain.getEpisodes() != null) {
            List<EpisodeEntity> episodeEntities = domain.getEpisodes().stream().map(e -> {
                EpisodeEntity ee = episodeToEntity(e);
                ee.setSeason(entity);
                return ee;
            }).collect(Collectors.toList());
            entity.setEpisodes(episodeEntities);
        }

        if (domain.getDefaultSkipTimestamps() != null) {
            List<SkipTimestampEntity> defaultSkipEntities = domain.getDefaultSkipTimestamps().stream()
                    .map(sk -> {
                        SkipTimestampEntity ske = skipTimestampToEntity(sk);
                        ske.setDefaultSeason(entity);
                        return ske;
                    })
                    .collect(Collectors.toList());
            entity.setDefaultSkipTimestamps(defaultSkipEntities);
        }

        return entity;
    }

    public Episode episodeToDomain(EpisodeEntity entity) {
        if (entity == null) return null;

        List<SkipTimestamp> skipTimestamps = entity.getSkipTimestamps() != null ?
                entity.getSkipTimestamps().stream().map(this::skipTimestampToDomain).collect(Collectors.toList()) : new ArrayList<>();

        return new Episode(
                entity.getId(),
                entity.getEpisodeNumber(),
                entity.getTitle(),
                entity.getVideoPath(),
                entity.getDurationSeconds(),
                skipTimestamps
        );
    }

    public EpisodeEntity episodeToEntity(Episode domain) {
        if (domain == null) return null;

        EpisodeEntity entity = new EpisodeEntity();
        entity.setId(domain.getId());
        entity.setEpisodeNumber(domain.getEpisodeNumber());
        entity.setTitle(domain.getTitle());
        entity.setVideoPath(domain.getVideoPath());
        entity.setDurationSeconds(domain.getDurationSeconds());

        if (domain.getSkipTimestamps() != null) {
            List<SkipTimestampEntity> skipEntities = domain.getSkipTimestamps().stream().map(sk -> {
                SkipTimestampEntity ske = skipTimestampToEntity(sk);
                ske.setEpisode(entity);
                return ske;
            }).collect(Collectors.toList());
            entity.setSkipTimestamps(skipEntities);
        }

        return entity;
    }

    public SkipTimestamp skipTimestampToDomain(SkipTimestampEntity entity) {
        if (entity == null) return null;
        return new SkipTimestamp(
                entity.getId(),
                entity.getType(),
                entity.getStartTimeSeconds(),
                entity.getEndTimeSeconds(),
                entity.getLabel()
        );
    }

    public SkipTimestampEntity skipTimestampToEntity(SkipTimestamp domain) {
        if (domain == null) return null;
        SkipTimestampEntity entity = new SkipTimestampEntity();
        entity.setId(domain.getId());
        entity.setType(domain.getType());
        entity.setStartTimeSeconds(domain.getStartTimeSeconds());
        entity.setEndTimeSeconds(domain.getEndTimeSeconds());
        entity.setLabel(domain.getLabel());
        return entity;
    }
}
