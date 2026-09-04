package com.back.vd.application.dtos;

public record BatchImportRequestDto(
        String mediaTitle,
        Integer seasonNumber,
        String seasonTitle,
        String directoryPath,
        String fileExtension,
        String defaultIntroStart,
        String defaultIntroEnd
) {}
