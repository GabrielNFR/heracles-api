package com.gabrielnfr.heracles_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Heracles API")
                .version("1.0")
                .description("API REST para registro de treinos, execuções, e progressão de carga")
                .contact(new Contact()
                    .name("GabrielNFR")
                    .email("skicklew@gmail.com")
                    .url("https://github.com/GabrielNFR/heracles-api")));  
    }
}
