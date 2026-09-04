package com.back.vd.application.dtos;

import com.back.vd.domain.enums.SkipType;

public record SkipTimestampResponseDto(
        Long id,
        SkipType type,
        Integer startTimeSeconds,
        Integer endTimeSeconds,
        String label
) {}
