package com.gabrielnfr.heracles_api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> home() {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Heracles API</title>
            </head>
            <body>
                <h1>Heracles API</h1>
                <p>API REST para gerenciamento de treinos e exercícios.</p>
                <p><a href="/swagger-ui.html">Documentação interativa (Swagger)</a></p>
            </body>
            </html>
            """;
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(html);
    }
}