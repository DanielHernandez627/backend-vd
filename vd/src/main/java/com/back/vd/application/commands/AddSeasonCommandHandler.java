package com.back.vd.application.commands;

import com.back.vd.application.dtos.AddSeasonDto;
import com.back.vd.application.dtos.SeasonResponseDto;
import com.back.vd.domain.exception.ResourceNotFoundException;
import com.back.vd.domain.model.MediaContent;
import com.back.vd.domain.model.Season;
import com.back.vd.domain.ports.output.MediaContentRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class AddSeasonCommandHandler {

    private final MediaContentRepositoryPort repositoryPort;

    public AddSeasonCommandHandler(MediaContentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Transactional
    public SeasonResponseDto execute(Long mediaContentId, AddSeasonDto dto) {
        MediaContent mediaContent = repositoryPort.findById(mediaContentId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el contenido multimedia con ID: " + mediaContentId));

        Season season = new Season(
                null,
                dto.seasonNumber(),
                dto.title(),
                new ArrayList<>()
        );

        mediaContent.addSeason(season);
        MediaContent updated = repositoryPort.save(mediaContent);

        Season savedSeason = updated.getSeasons().stream()
                .filter(s -> s.getSeasonNumber().equals(dto.seasonNumber()))
                .findFirst()
                .orElse(season);

        return new SeasonResponseDto(
                savedSeason.getId(),
                savedSeason.getSeasonNumber(),
                savedSeason.getTitle(),
                new ArrayList<>()
        );
    }
}
