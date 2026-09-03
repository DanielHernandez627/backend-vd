package com.back.vd.application.dtos;

import com.back.vd.domain.enums.MediaType;

public record CreateMediaContentDto(
        String title,
        String description,
        String coverImageUrl,
        MediaType type
) {}
