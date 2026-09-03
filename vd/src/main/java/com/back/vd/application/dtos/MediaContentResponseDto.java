package com.back.vd.application.dtos;

import com.back.vd.domain.enums.MediaType;
import java.time.LocalDateTime;
import java.util.List;

public record MediaContentResponseDto(
        Long id,
        String title,
        String description,
        String coverImageUrl,
        MediaType type,
        LocalDateTime createdAt,
        List<SeasonResponseDto> seasons
) {}
