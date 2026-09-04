package com.back.vd.application.queries;

import com.back.vd.application.dtos.SkipTimestampResponseDto;
import com.back.vd.domain.exception.ResourceNotFoundException;
import com.back.vd.domain.model.Episode;
import com.back.vd.domain.model.Season;
import com.back.vd.domain.model.SkipTimestamp;
import com.back.vd.domain.ports.output.MediaContentRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetEpisodeSkipTimestampsQueryHandler {

    private final MediaContentRepositoryPort repositoryPort;

    public GetEpisodeSkipTimestampsQueryHandler(MediaContentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Transactional(readOnly = true)
    public List<SkipTimestampResponseDto> execute(Long episodeId) {
        Episode episode = repositoryPort.findEpisodeById(episodeId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el episodio con ID: " + episodeId));

        if (episode.getSkipTimestamps() != null && !episode.getSkipTimestamps().isEmpty()) {
            return mapToDtoList(episode.getSkipTimestamps());
        }

        Optional<Season> parentSeason = repositoryPort.findSeasonByEpisodeId(episodeId);
        if (parentSeason.isPresent() && parentSeason.get().getDefaultSkipTimestamps() != null && !parentSeason.get().getDefaultSkipTimestamps().isEmpty()) {
            return mapToDtoList(parentSeason.get().getDefaultSkipTimestamps());
        }

        return List.of();
    }

    private List<SkipTimestampResponseDto> mapToDtoList(List<SkipTimestamp> skipTimestamps) {
        return skipTimestamps.stream()
                .map(sk -> new SkipTimestampResponseDto(sk.getId(), sk.getType(), sk.getStartTimeSeconds(), sk.getEndTimeSeconds(), sk.getLabel()))
                .collect(Collectors.toList());
    }
}
