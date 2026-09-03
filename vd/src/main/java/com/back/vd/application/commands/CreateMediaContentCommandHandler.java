package com.back.vd.application.commands;

import com.back.vd.application.dtos.CreateMediaContentDto;
import com.back.vd.application.dtos.MediaContentResponseDto;
import com.back.vd.domain.model.MediaContent;
import com.back.vd.domain.ports.output.MediaContentRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CreateMediaContentCommandHandler {

    private final MediaContentRepositoryPort repositoryPort;

    public CreateMediaContentCommandHandler(MediaContentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Transactional
    public MediaContentResponseDto execute(CreateMediaContentDto dto) {
        MediaContent mediaContent = new MediaContent(
                null,
                dto.title(),
                dto.description(),
                dto.coverImageUrl(),
                dto.type(),
                LocalDateTime.now(),
                new ArrayList<>()
        );

        MediaContent saved = repositoryPort.save(mediaContent);

        return new MediaContentResponseDto(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getCoverImageUrl(),
                saved.getType(),
                saved.getCreatedAt(),
                new ArrayList<>()
        );
    }
}
