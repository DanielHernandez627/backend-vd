package com.back.vd.application.commands;

import com.back.vd.application.dtos.AddEpisodeDto;
import com.back.vd.application.dtos.EpisodeResponseDto;
import com.back.vd.application.dtos.SkipTimestampResponseDto;
import com.back.vd.domain.exception.ResourceNotFoundException;
import com.back.vd.domain.model.Episode;
import com.back.vd.domain.model.MediaContent;
import com.back.vd.domain.model.Season;
import com.back.vd.domain.model.SkipTimestamp;
import com.back.vd.domain.ports.output.MediaContentRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddEpisodeCommandHandler {

    private final MediaContentRepositoryPort repositoryPort;

    public AddEpisodeCommandHandler(MediaContentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Transactional
    public EpisodeResponseDto execute(Long seasonId, AddEpisodeDto dto) {
        List<MediaContent> allContent = repositoryPort.findAll();
        Season targetSeason = null;
        MediaContent targetMediaContent = null;

        for (MediaContent mc : allContent) {
            for (Season s : mc.getSeasons()) {
                if (s.getId() != null && s.getId().equals(seasonId)) {
                    targetSeason = s;
                    targetMediaContent = mc;
                    break;
                }
            }
            if (targetSeason != null) break;
        }

        if (targetSeason == null || targetMediaContent == null) {
            throw new ResourceNotFoundException("No se encontró la temporada con ID: " + seasonId);
        }

        List<SkipTimestamp> skipTimestamps = new ArrayList<>();
        if (dto.skipTimestamps() != null) {
            skipTimestamps = dto.skipTimestamps().stream()
                    .map(sk -> new SkipTimestamp(null, sk.type(), sk.startTimeSeconds(), sk.endTimeSeconds(), sk.label()))
                    .collect(Collectors.toList());
        }

        Episode episode = new Episode(
                null,
                dto.episodeNumber(),
                dto.title(),
                dto.videoPath(),
                dto.durationSeconds(),
                skipTimestamps
        );

        targetSeason.addEpisode(episode);
        repositoryPort.save(targetMediaContent);

        List<SkipTimestampResponseDto> skipDtos = episode.getSkipTimestamps().stream()
                .map(sk -> new SkipTimestampResponseDto(sk.getId(), sk.getType(), sk.getStartTimeSeconds(), sk.getEndTimeSeconds(), sk.getLabel()))
                .collect(Collectors.toList());

        return new EpisodeResponseDto(
                episode.getId(),
                episode.getEpisodeNumber(),
                episode.getTitle(),
                episode.getVideoPath(),
                episode.getDurationSeconds(),
                skipDtos
        );
    }
}
