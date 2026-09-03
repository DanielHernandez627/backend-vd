package com.back.vd.infrastructure.persistence.entity;

import com.back.vd.domain.enums.SkipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "skip_timestamps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkipTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SkipType type;

    @Column(name = "start_time_seconds", nullable = false)
    private Integer startTimeSeconds;

    @Column(name = "end_time_seconds", nullable = false)
    private Integer endTimeSeconds;

    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private EpisodeEntity episode;
}
