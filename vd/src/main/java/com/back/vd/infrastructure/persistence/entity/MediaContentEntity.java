package com.back.vd.infrastructure.persistence.entity;

import com.back.vd.domain.enums.MediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "media_contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MediaType type;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "mediaContent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("seasonNumber ASC")
    private List<SeasonEntity> seasons = new ArrayList<>();
}
