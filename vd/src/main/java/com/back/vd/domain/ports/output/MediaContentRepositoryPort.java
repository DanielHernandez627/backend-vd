package com.back.vd.domain.ports.output;

import com.back.vd.domain.model.Episode;
import com.back.vd.domain.model.MediaContent;
import com.back.vd.domain.model.Season;
import java.util.List;
import java.util.Optional;

public interface MediaContentRepositoryPort {
    MediaContent save(MediaContent mediaContent);
    Optional<MediaContent> findById(Long id);
    List<MediaContent> findAll();
    Optional<Season> findSeasonById(Long seasonId);
    Optional<MediaContent> findMediaContentBySeasonId(Long seasonId);
    Optional<Episode> findEpisodeById(Long episodeId);
    Optional<Season> findSeasonByEpisodeId(Long episodeId);
    void deleteById(Long id);
}
