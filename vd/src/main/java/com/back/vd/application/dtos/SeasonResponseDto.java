package com.back.vd.application.dtos;

import java.util.List;

public record SeasonResponseDto(
        Long id,
        Integer seasonNumber,
        String title,
        List<EpisodeResponseDto> episodes
) {}
