package com.back.vd.presentation.controller;

import com.back.vd.application.queries.StreamVideoQueryHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
@Tag(name = "Streaming de Video", description = "Servicio de reproducción por demanda con soporte para HTTP 206 Partial Content (Byte Ranges).")
public class VideoStreamController {

    private final StreamVideoQueryHandler streamVideoQueryHandler;

    public VideoStreamController(StreamVideoQueryHandler streamVideoQueryHandler) {
        this.streamVideoQueryHandler = streamVideoQueryHandler;
    }

    @GetMapping("/stream/{episodeId}")
    @Operation(summary = "Streaming de Video por Demanda (HTTP 206)", description = "Sirve fragmentos del archivo .mp4 local basándose en la cabecera HTTP Range enviada por el navegador.")
    public ResponseEntity<ResourceRegion> streamVideo(
            @PathVariable Long episodeId,
            @RequestHeader HttpHeaders headers) {

        ResourceRegion region = streamVideoQueryHandler.execute(episodeId, headers);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(region.getResource())
                        .orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(region);
    }
}
