package com.back.vd.domain.model;

import com.back.vd.domain.enums.MediaType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MediaContent {
    private Long id;
    private String title;
    private String description;
    private String coverImageUrl;
    private MediaType type;
    private LocalDateTime createdAt;
    private List<Season> seasons = new ArrayList<>();

    public MediaContent() {
    }

    public MediaContent(Long id, String title, String description, String coverImageUrl, MediaType type, LocalDateTime createdAt, List<Season> seasons) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.type = type;
        this.createdAt = createdAt;
        this.seasons = seasons != null ? seasons : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public void addSeason(Season season) {
        this.seasons.add(season);
    }
}
