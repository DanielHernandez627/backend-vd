package com.back.vd.application.dtos;

import java.util.List;

public record EpisodeResponseDto(
        Long id,
        Integer episodeNumber,
        String title,
        String videoPath,
        Integer durationSeconds,
        List<SkipTimestampResponseDto> skipTimestamps
) {}
