package com.back.vd.application.dtos;

import java.util.List;

public record BatchImportResponseDto(
        int importedCount,
        Long seasonId,
        List<String> importedFileNames
) {}
