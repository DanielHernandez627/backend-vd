package com.back.vd.application.queries;

import com.back.vd.application.dtos.EpisodeResponseDto;
import com.back.vd.application.dtos.MediaContentResponseDto;
import com.back.vd.application.dtos.SeasonResponseDto;
import com.back.vd.application.dtos.SkipTimestampResponseDto;
import com.back.vd.domain.model.Episode;
import com.back.vd.domain.model.MediaContent;
import com.back.vd.domain.model.Season;
import com.back.vd.domain.ports.output.MediaContentRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetCatalogQueryHandler {

    private final MediaContentRepositoryPort repositoryPort;

    public GetCatalogQueryHandler(MediaContentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Transactional(readOnly = true)
    public List<MediaContentResponseDto> execute() {
        List<MediaContent> contents = repositoryPort.findAll();
        return contents.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private MediaContentResponseDto toResponseDto(MediaContent mc) {
        List<SeasonResponseDto> seasonDtos = mc.getSeasons().stream()
                .map(this::seasonToDto)
                .collect(Collectors.toList());

        return new MediaContentResponseDto(
                mc.getId(),
                mc.getTitle(),
                mc.getDescription(),
                mc.getCoverImageUrl(),
                mc.getType(),
                mc.getCreatedAt(),
                seasonDtos
        );
    }

    private SeasonResponseDto seasonToDto(Season s) {
        List<EpisodeResponseDto> episodeDtos = s.getEpisodes().stream()
                .map(this::episodeToDto)
                .collect(Collectors.toList());

        return new SeasonResponseDto(
                s.getId(),
                s.getSeasonNumber(),
                s.getTitle(),
                episodeDtos
        );
    }

    private EpisodeResponseDto episodeToDto(Episode e) {
        List<SkipTimestampResponseDto> skipDtos = e.getSkipTimestamps().stream()
                .map(sk -> new SkipTimestampResponseDto(sk.getId(), sk.getType(), sk.getStartTimeSeconds(), sk.getEndTimeSeconds(), sk.getLabel()))
                .collect(Collectors.toList());

        return new EpisodeResponseDto(
                e.getId(),
                e.getEpisodeNumber(),
                e.getTitle(),
                e.getVideoPath(),
                e.getDurationSeconds(),
                skipDtos
        );
    }
}
