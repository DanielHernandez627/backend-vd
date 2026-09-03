package com.back.vd.domain.model;

import com.back.vd.domain.enums.SkipType;

public class SkipTimestamp {
    private Long id;
    private SkipType type;
    private Integer startTimeSeconds;
    private Integer endTimeSeconds;
    private String label;

    public SkipTimestamp() {
    }

    public SkipTimestamp(Long id, SkipType type, Integer startTimeSeconds, Integer endTimeSeconds, String label) {
        this.id = id;
        this.type = type;
        this.startTimeSeconds = startTimeSeconds;
        this.endTimeSeconds = endTimeSeconds;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkipType getType() {
        return type;
    }

    public void setType(SkipType type) {
        this.type = type;
    }

    public Integer getStartTimeSeconds() {
        return startTimeSeconds;
    }

    public void setStartTimeSeconds(Integer startTimeSeconds) {
        this.startTimeSeconds = startTimeSeconds;
    }

    public Integer getEndTimeSeconds() {
        return endTimeSeconds;
    }

    public void setEndTimeSeconds(Integer endTimeSeconds) {
        this.endTimeSeconds = endTimeSeconds;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
