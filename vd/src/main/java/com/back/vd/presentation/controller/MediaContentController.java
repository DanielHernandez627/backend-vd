package com.back.vd.presentation.controller;

import com.back.vd.application.commands.AddEpisodeCommandHandler;
import com.back.vd.application.commands.AddSeasonCommandHandler;
import com.back.vd.application.commands.BatchImportEpisodesCommandHandler;
import com.back.vd.application.commands.CreateMediaContentCommandHandler;
import com.back.vd.application.dtos.*;
import com.back.vd.application.queries.GetCatalogQueryHandler;
import com.back.vd.application.queries.GetEpisodeSkipTimestampsQueryHandler;
import com.back.vd.application.queries.GetMediaContentDetailsQueryHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Catálogo Multimedia", description = "Endpoints para consultar y administrar contenidos multimedia (series, películas, anime), temporadas y episodios.")
public class MediaContentController {

    private final CreateMediaContentCommandHandler createMediaContentCommandHandler;
    private final AddSeasonCommandHandler addSeasonCommandHandler;
    private final AddEpisodeCommandHandler addEpisodeCommandHandler;
    private final BatchImportEpisodesCommandHandler batchImportEpisodesCommandHandler;
    private final GetCatalogQueryHandler getCatalogQueryHandler;
    private final GetMediaContentDetailsQueryHandler getMediaContentDetailsQueryHandler;
    private final GetEpisodeSkipTimestampsQueryHandler getEpisodeSkipTimestampsQueryHandler;

    public MediaContentController(CreateMediaContentCommandHandler createMediaContentCommandHandler,
                                  AddSeasonCommandHandler addSeasonCommandHandler,
                                  AddEpisodeCommandHandler addEpisodeCommandHandler,
                                  BatchImportEpisodesCommandHandler batchImportEpisodesCommandHandler,
                                  GetCatalogQueryHandler getCatalogQueryHandler,
                                  GetMediaContentDetailsQueryHandler getMediaContentDetailsQueryHandler,
                                  GetEpisodeSkipTimestampsQueryHandler getEpisodeSkipTimestampsQueryHandler) {
        this.createMediaContentCommandHandler = createMediaContentCommandHandler;
        this.addSeasonCommandHandler = addSeasonCommandHandler;
        this.addEpisodeCommandHandler = addEpisodeCommandHandler;
        this.batchImportEpisodesCommandHandler = batchImportEpisodesCommandHandler;
        this.getCatalogQueryHandler = getCatalogQueryHandler;
        this.getMediaContentDetailsQueryHandler = getMediaContentDetailsQueryHandler;
        this.getEpisodeSkipTimestampsQueryHandler = getEpisodeSkipTimestampsQueryHandler;
    }

    @GetMapping("/media")
    @Operation(summary = "Obtener Catálogo Completo", description = "Retorna la lista de todos los contenidos multimedia registrados en la plataforma.")
    public ResponseEntity<List<MediaContentResponseDto>> getCatalog() {
        return ResponseEntity.ok(getCatalogQueryHandler.execute());
    }

    @GetMapping("/media/{id}")
    @Operation(summary = "Obtener Detalle de Contenido por ID", description = "Retorna el detalle completo de un contenido incluyendo sus temporadas y episodios.")
    public ResponseEntity<MediaContentResponseDto> getMediaContentDetails(@PathVariable Long id) {
        return ResponseEntity.ok(getMediaContentDetailsQueryHandler.execute(id));
    }

    @PostMapping("/media")
    @Operation(summary = "Registrar Nuevo Contenido Multimedia", description = "Crea un nuevo registro de contenido (Anime, Serie, Película, Documental).")
    public ResponseEntity<MediaContentResponseDto> createMediaContent(@RequestBody CreateMediaContentDto dto) {
        MediaContentResponseDto created = createMediaContentCommandHandler.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/media/{id}/seasons")
    @Operation(summary = "Añadir Temporada a un Contenido", description = "Agrega una nueva temporada al contenido especificado por su ID.")
    public ResponseEntity<SeasonResponseDto> addSeason(@PathVariable Long id, @RequestBody AddSeasonDto dto) {
        SeasonResponseDto createdSeason = addSeasonCommandHandler.execute(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeason);
    }

    @PostMapping("/seasons/{seasonId}/episodes")
    @Operation(summary = "Añadir Episodio Individual a una Temporada", description = "Agrega un nuevo episodio indicando la ruta del video (.mp4) y marcas de tiempo de skip intro/outro.")
    public ResponseEntity<EpisodeResponseDto> addEpisode(@PathVariable Long seasonId, @RequestBody AddEpisodeDto dto) {
        EpisodeResponseDto createdEpisode = addEpisodeCommandHandler.execute(seasonId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEpisode);
    }

    @PostMapping("/media/{mediaContentId}/batch-import")
    @Operation(summary = "Escaneo e Importación Masiva por Serie/Anime", description = "Crea o busca la temporada especificada, escanea la carpeta local de videos (.mp4) e importa todos los episodios en una sola transacción.")
    public ResponseEntity<BatchImportResponseDto> batchImportSeason(@PathVariable Long mediaContentId, @RequestBody BatchImportRequestDto request) {
        BatchImportResponseDto response = batchImportEpisodesCommandHandler.executeForMediaContent(mediaContentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/seasons/{seasonId}/batch-import")
    @Operation(summary = "Escaneo e Importación Masiva de Videos Locales (.mp4)", description = "Escanea una carpeta en el disco local y registra en lote todos los archivos de video asociándolos a la temporada especificada.")
    public ResponseEntity<BatchImportResponseDto> batchImportEpisodes(@PathVariable Long seasonId, @RequestBody BatchImportRequestDto request) {
        BatchImportResponseDto response = batchImportEpisodesCommandHandler.execute(seasonId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/episodes/{episodeId}/skip-timestamps")
    @Operation(summary = "Obtener Marcas de Skip Intro (Con Herencia de Temporada)", description = "Retorna los intervalos de tiempo configurados. Si el episodio no tiene marcas propias, se heredan automáticamente de su Temporada.")
    public ResponseEntity<List<SkipTimestampResponseDto>> getSkipTimestamps(@PathVariable Long episodeId) {
        return ResponseEntity.ok(getEpisodeSkipTimestampsQueryHandler.execute(episodeId));
    }
}
