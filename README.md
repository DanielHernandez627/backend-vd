# ☕ Backend VD - API REST & Streaming Service

Servicio Backend REST desarrollado en **Java 21** y **Spring Boot 3**, diseñado bajo principios de **Clean Architecture**, **CQRS (Command Query Responsibility Segregation)** y **Clean Code (0 comentarios en clases)**.

---

## 🛠 Tecnologías y Herramientas

- **Lenguaje**: Java 21 LTS
- **Framework**: Spring Boot 3.x
- **Gestor de Construcción**: Gradle (Wrapper Gradle incluído)
- **Base de Datos**: PostgreSQL 16
- **Persistencia**: Spring Data JPA / Hibernate
- **Streaming**: Spring Web (`ResourceRegion` HTTP 206 Partial Content)
- **Documentación API**: OpenAPI 3 / Swagger UI (`springdoc-openapi`)

---

## 📁 Estructura del Proyecto

```text
backend-vd/vd/
├── src/main/java/com/back/vd/
│   ├── domain/                         # Capa de Dominio (Modelos, Enums, Excepciones, Puertos)
│   │   ├── enums/                      # MediaType, SkipType
│   │   ├── exception/                  # DomainException, ResourceNotFoundException
│   │   ├── model/                      # Episode, Season, MediaContent, SkipTimestamp
│   │   └── ports/                      # MediaContentRepositoryPort
│   ├── application/                    # Capa de Aplicación (CQRS Commands & Queries)
│   │   ├── commands/                   # BatchImportEpisodesCommandHandler, AddEpisodeCommandHandler...
│   │   ├── queries/                    # StreamVideoQueryHandler, GetCatalogQueryHandler...
│   │   └── dtos/                       # BatchImportRequestDto, BatchImportResponseDto...
│   ├── infrastructure/                 # Capa de Infraestructura (Persistencia & JPA)
│   │   └── persistence/
│   │       ├── entity/                 # MediaContentEntity, SeasonEntity, EpisodeEntity...
│   │       ├── repository/             # Spring Data Repositories
│   │       ├── adapter/                # MediaContentPersistenceAdapter
│   │       └── mapper/                 # MediaContentEntityMapper
│   └── presentation/                   # Capa de Presentación (Controladores REST & Excepciones)
│       ├── controller/                 # MediaContentController, VideoStreamController
│       ├── config/                     # WebCorsConfig, OpenApiConfig
│       └── exception/                  # GlobalExceptionHandler (@RestControllerAdvice)
└── Dockerfile                          # Multi-stage build (JDK 21 + JRE 21 Alpine)
```

---

## 🚀 Ejecución en Desarrollo

### Prerrequisitos
- JDK 21 instalado localmente (o ejecución mediante Docker).
- PostgreSQL 16 ejecutándose en el puerto `5432` (`proyecto_vd_db`).

### Comandos Principales (Gradle)

```bash
# Compilar código Java
./gradlew compileJava

# Generar archivo JAR ejecutable
./gradlew bootJar

# Ejecutar pruebas unitarias
./gradlew test

# Iniciar aplicación en entorno local (Puerto 8080)
./gradlew bootRun
```

---

## 🔗 Endpoints Principales REST

| Método | Ruta API | Descripción |
|---|---|---|
| `GET` | `/api/media` | Obtiene la lista completa del catálogo multimedia. |
| `GET` | `/api/media/{id}` | Obtiene los detalles de una serie/película con sus temporadas y episodios. |
| `POST` | `/api/media` | Crea un nuevo contenido multimedia individual. |
| `POST` | `/api/media/{mediaId}/batch-import` | Escaneo masivo e incremental de una carpeta local de videos `.mp4`. |
| `GET` | `/api/videos/stream/{episodeId}` | Streaming de video HTTP 206 Partial Content por rangos de bytes. |
| `GET` | `/api/episodes/{episodeId}/skip-timestamps` | Obtiene las marcas de tiempo para Skip Intro por episodio. |

---

## 📖 Swagger UI

Accede a la documentación interactiva OpenAPI disponible en:
`http://localhost:8080/swagger-ui.html`
