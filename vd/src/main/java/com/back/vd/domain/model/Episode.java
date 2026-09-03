package com.back.vd.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Episode {
    private Long id;
    private Integer episodeNumber;
    private String title;
    private String videoPath;
    private Integer durationSeconds;
    private List<SkipTimestamp> skipTimestamps = new ArrayList<>();

    public Episode() {
    }

    public Episode(Long id, Integer episodeNumber, String title, String videoPath, Integer durationSeconds, List<SkipTimestamp> skipTimestamps) {
        this.id = id;
        this.episodeNumber = episodeNumber;
        this.title = title;
        this.videoPath = videoPath;
        this.durationSeconds = durationSeconds;
        this.skipTimestamps = skipTimestamps != null ? skipTimestamps : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public List<SkipTimestamp> getSkipTimestamps() {
        return skipTimestamps;
    }

    public void setSkipTimestamps(List<SkipTimestamp> skipTimestamps) {
        this.skipTimestamps = skipTimestamps;
    }

    public void addSkipTimestamp(SkipTimestamp skipTimestamp) {
        this.skipTimestamps.add(skipTimestamp);
    }
}
