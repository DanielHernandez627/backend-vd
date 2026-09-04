package com.back.vd.application.dtos;

import java.util.List;

public record BatchImportResponseDto(
        int importedCount,
        int skippedCount,
        Long seasonId,
        List<String> importedFileNames,
        List<String> skippedFileNames
) {}
