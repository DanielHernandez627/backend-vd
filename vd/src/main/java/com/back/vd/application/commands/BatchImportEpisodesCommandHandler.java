package com.back.vd.application.commands;

import com.back.vd.application.dtos.BatchImportRequestDto;
import com.back.vd.application.dtos.BatchImportResponseDto;
import com.back.vd.domain.enums.MediaType;
import com.back.vd.domain.enums.SkipType;
import com.back.vd.domain.exception.DomainException;
import com.back.vd.domain.exception.ResourceNotFoundException;
import com.back.vd.domain.model.Episode;
import com.back.vd.domain.model.MediaContent;
import com.back.vd.domain.model.Season;
import com.back.vd.domain.model.SkipTimestamp;
import com.back.vd.domain.ports.output.MediaContentRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BatchImportEpisodesCommandHandler {

    private final MediaContentRepositoryPort repositoryPort;

    public BatchImportEpisodesCommandHandler(MediaContentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Transactional
    public BatchImportResponseDto executeForMediaContent(Long mediaContentId, BatchImportRequestDto request) {
        MediaContent targetMediaContent;

        if (mediaContentId != null && mediaContentId > 0) {
            targetMediaContent = repositoryPort.findById(mediaContentId)
                    .orElseGet(() -> createFallbackMediaContent(request));
        } else {
            targetMediaContent = createFallbackMediaContent(request);
        }

        Integer seasonNum = request.seasonNumber() != null ? request.seasonNumber() : 1;
        String seasonTitle = request.seasonTitle() != null && !request.seasonTitle().isBlank() ? request.seasonTitle().trim() : "Temporada " + seasonNum;

        Season targetSeason = targetMediaContent.getSeasons().stream()
                .filter(s -> s.getSeasonNumber() != null && s.getSeasonNumber().equals(seasonNum))
                .findFirst()
                .orElseGet(() -> {
                    Season newSeason = new Season(null, seasonNum, seasonTitle, new ArrayList<>());
                    targetMediaContent.addSeason(newSeason);
                    return newSeason;
                });

        return processBatchImport(targetMediaContent, targetSeason, request);
    }

    @Transactional
    public BatchImportResponseDto execute(Long seasonId, BatchImportRequestDto request) {
        MediaContent targetMediaContent = findMediaContentBySeasonId(seasonId);
        Season targetSeason = findSeasonInMediaContent(targetMediaContent, seasonId);
        return processBatchImport(targetMediaContent, targetSeason, request);
    }

    private MediaContent createFallbackMediaContent(BatchImportRequestDto request) {
        String title = request.mediaTitle() != null && !request.mediaTitle().isBlank() ? request.mediaTitle().trim() : "Serie / Anime Carga Masiva";
        MediaContent newMedia = new MediaContent(
                null,
                title,
                "Contenido importado masivamente desde " + request.directoryPath(),
                "",
                MediaType.ANIME_SERIES,
                LocalDateTime.now(),
                new ArrayList<>()
        );
        return repositoryPort.save(newMedia);
    }

    private BatchImportResponseDto processBatchImport(MediaContent targetMediaContent, Season targetSeason, BatchImportRequestDto request) {
        String sanitizedPath = request.directoryPath() != null ? request.directoryPath().trim() : "";
        File directory = new File(sanitizedPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new DomainException("El directorio especificado no existe o no es una carpeta válida: " + sanitizedPath);
        }

        String rawExt = request.fileExtension() != null && !request.fileExtension().isBlank() ? request.fileExtension().trim().toLowerCase() : ".mp4";
        String extension = rawExt.startsWith(".") ? rawExt : "." + rawExt;

        File[] videoFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(extension));

        if (videoFiles == null || videoFiles.length == 0) {
            throw new DomainException("No se encontraron archivos con extensión '" + extension + "' en el directorio: " + sanitizedPath);
        }

        Arrays.sort(videoFiles, Comparator.comparing(File::getName));

        configureDefaultSeasonSkipTimestamps(targetSeason, request.defaultIntroStart(), request.defaultIntroEnd());

        int nextEpisodeNumber = targetSeason.getEpisodes().size() + 1;
        List<String> importedFileNames = new ArrayList<>();
        List<String> skippedFileNames = new ArrayList<>();

        for (File file : videoFiles) {
            int episodeNum = extractEpisodeNumber(file.getName(), nextEpisodeNumber);

            Optional<Episode> existingEpisode = targetSeason.getEpisodes().stream()
                    .filter(e -> (e.getEpisodeNumber() != null && e.getEpisodeNumber().equals(episodeNum))
                              || (e.getVideoPath() != null && e.getVideoPath().equalsIgnoreCase(file.getAbsolutePath())))
                    .findFirst();

            if (existingEpisode.isPresent()) {
                Episode ep = existingEpisode.get();
                if (!file.getAbsolutePath().equalsIgnoreCase(ep.getVideoPath())) {
                    ep.setVideoPath(file.getAbsolutePath());
                }
                skippedFileNames.add(file.getName());
            } else {
                String title = formatEpisodeTitle(file.getName(), episodeNum);
                Episode newEpisode = new Episode(
                        null,
                        episodeNum,
                        title,
                        file.getAbsolutePath(),
                        0,
                        new ArrayList<>()
                );

                targetSeason.addEpisode(newEpisode);
                importedFileNames.add(file.getName());
                nextEpisodeNumber++;
            }
        }

        MediaContent savedMediaContent = repositoryPort.save(targetMediaContent);
        Season savedSeason = savedMediaContent.getSeasons().stream()
                .filter(s -> s.getSeasonNumber() != null && s.getSeasonNumber().equals(targetSeason.getSeasonNumber()))
                .findFirst()
                .orElse(targetSeason);

        return new BatchImportResponseDto(
                importedFileNames.size(),
                skippedFileNames.size(),
                savedSeason.getId(),
                importedFileNames,
                skippedFileNames
        );
    }

    private MediaContent findMediaContentBySeasonId(Long seasonId) {
        return repositoryPort.findMediaContentBySeasonId(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la serie asociada a la temporada con ID: " + seasonId));
    }

    private Season findSeasonInMediaContent(MediaContent mediaContent, Long seasonId) {
        return mediaContent.getSeasons().stream()
                .filter(s -> s.getId() != null && s.getId().equals(seasonId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la temporada con ID: " + seasonId));
    }

    private void configureDefaultSeasonSkipTimestamps(Season season, String introStart, String introEnd) {
        if (introStart == null || introEnd == null || introStart.isBlank() || introEnd.isBlank()) {
            return;
        }

        int startSeconds = parseTimeToSeconds(introStart);
        int endSeconds = parseTimeToSeconds(introEnd);

        if (startSeconds >= endSeconds) {
            return;
        }

        SkipTimestamp defaultSkip = new SkipTimestamp(
                null,
                SkipType.INTRO,
                startSeconds,
                endSeconds,
                "Saltar Opening (+90s)"
        );

        season.getDefaultSkipTimestamps().clear();
        season.addDefaultSkipTimestamp(defaultSkip);
    }

    private int extractEpisodeNumber(String fileName, int fallbackNumber) {
        Pattern pattern = Pattern.compile("(?:cap(?:itulo)?|ep(?:isodio)?|e)[_.-]?(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        Pattern numberPattern = Pattern.compile("(\\d+)");
        Matcher numberMatcher = numberPattern.matcher(fileName);

        if (numberMatcher.find()) {
            return Integer.parseInt(numberMatcher.group(1));
        }

        return fallbackNumber;
    }

    private String formatEpisodeTitle(String fileName, int episodeNumber) {
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        return "Episodio " + episodeNumber + ": " + nameWithoutExtension;
    }

    private int parseTimeToSeconds(String timeStr) {
        if (timeStr == null || timeStr.isBlank()) return 0;
        String[] parts = timeStr.trim().split(":");
        try {
            if (parts.length == 1) return Integer.parseInt(parts[0]);
            if (parts.length == 2) return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
            if (parts.length == 3) return Integer.parseInt(parts[0]) * 3600 + Integer.parseInt(parts[1]) * 60 + Integer.parseInt(parts[2]);
        } catch (NumberFormatException ignored) {}
        return 0;
    }
}
