package com.back.vd.application.queries;

import com.back.vd.application.dtos.SkipTimestampResponseDto;
import com.back.vd.domain.exception.ResourceNotFoundException;
import com.back.vd.domain.model.Episode;
import com.back.vd.domain.ports.output.MediaContentRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        return episode.getSkipTimestamps().stream()
                .map(sk -> new SkipTimestampResponseDto(sk.getId(), sk.getType(), sk.getStartTimeSeconds(), sk.getEndTimeSeconds(), sk.getLabel()))
                .collect(Collectors.toList());
    }
}
