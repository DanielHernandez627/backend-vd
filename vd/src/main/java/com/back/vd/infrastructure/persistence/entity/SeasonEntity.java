package com.back.vd.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seasons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeasonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "season_number", nullable = false)
    private Integer seasonNumber;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_content_id", nullable = false)
    private MediaContentEntity mediaContent;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EpisodeEntity> episodes = new ArrayList<>();

    @OneToMany(mappedBy = "defaultSeason", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkipTimestampEntity> defaultSkipTimestamps = new ArrayList<>();
}
