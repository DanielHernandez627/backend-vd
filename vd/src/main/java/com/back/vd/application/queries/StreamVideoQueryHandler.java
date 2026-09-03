package com.back.vd.application.queries;

import com.back.vd.domain.exception.MediaStorageException;
import com.back.vd.domain.exception.ResourceNotFoundException;
import com.back.vd.domain.model.Episode;
import com.back.vd.domain.ports.output.MediaContentRepositoryPort;
import com.back.vd.domain.ports.output.VideoStoragePort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class StreamVideoQueryHandler {

    private final MediaContentRepositoryPort repositoryPort;
    private final VideoStoragePort videoStoragePort;

    public StreamVideoQueryHandler(MediaContentRepositoryPort repositoryPort, VideoStoragePort videoStoragePort) {
        this.repositoryPort = repositoryPort;
        this.videoStoragePort = videoStoragePort;
    }

    @Transactional(readOnly = true)
    public ResourceRegion execute(Long episodeId, HttpHeaders headers) {
        Episode episode = repositoryPort.findEpisodeById(episodeId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el episodio con ID: " + episodeId));

        if (!videoStoragePort.exists(episode.getVideoPath())) {
            throw new MediaStorageException("El archivo de video no existe en el sistema de archivos: " + episode.getVideoPath());
        }

        try {
            Resource videoResource = videoStoragePort.loadVideoAsResource(episode.getVideoPath());
            long contentLength = videoResource.contentLength();
            List<HttpRange> ranges = headers.getRange();

            if (!ranges.isEmpty()) {
                HttpRange range = ranges.get(0);
                long start = range.getRangeStart(contentLength);
                long end = range.getRangeEnd(contentLength);
                long rangeLength = Math.min(1024 * 1024L, end - start + 1); // Bloque máximo de 1MB por fragmento
                return new ResourceRegion(videoResource, start, rangeLength);
            } else {
                long rangeLength = Math.min(1024 * 1024L, contentLength);
                return new ResourceRegion(videoResource, 0, rangeLength);
            }
        } catch (IOException ex) {
            throw new MediaStorageException("Error leyendo el archivo de video: " + ex.getMessage(), ex);
        }
    }
}
