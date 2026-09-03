package com.back.vd.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Season {
    private Long id;
    private Integer seasonNumber;
    private String title;
    private List<Episode> episodes = new ArrayList<>();

    public Season() {
    }

    public Season(Long id, Integer seasonNumber, String title, List<Episode> episodes) {
        this.id = id;
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.episodes = episodes != null ? episodes : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public void addEpisode(Episode episode) {
        this.episodes.add(episode);
    }
}
