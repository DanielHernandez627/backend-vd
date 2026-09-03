package com.back.vd.application.dtos;

import java.util.List;

public record AddEpisodeDto(
        Integer episodeNumber,
        String title,
        String videoPath,
        Integer durationSeconds,
        List<CreateSkipTimestampDto> skipTimestamps
) {}
