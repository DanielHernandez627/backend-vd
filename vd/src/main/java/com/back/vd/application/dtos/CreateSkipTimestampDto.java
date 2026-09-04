package com.back.vd.application.dtos;

import com.back.vd.domain.enums.SkipType;

public record CreateSkipTimestampDto(
        SkipType type,
        Integer startTimeSeconds,
        Integer endTimeSeconds,
        String label
) {}
