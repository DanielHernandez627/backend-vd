package com.back.vd.presentation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Proyecto VD - Video Streaming & Catalog API")
                        .version("1.0")
                        .description("API RESTful autodocumentada para la gestión de contenidos multimedia (Series, Anime, Películas), marcas de tiempo de Skip Intro y servicio de Streaming de Video HTTP 206 Partial Content."));
    }
}
