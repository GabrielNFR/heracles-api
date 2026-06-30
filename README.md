# Heracles API

API REST para registro de treinos, execucoes e progressao de carga.
Inspirada nos 12 Trabalhos de Hercules.

## Stack

| Tecnologia | Versao |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.0 |
| Spring Data JPA (Hibernate) | 7.4 |
| Spring Web MVC | Tomcat embedado |
| Bean Validation | Jakarta Validation |
| Spring Actuator | Health checks |
| PostgreSQL | Producao (Neon) |
| H2 | Desenvolvimento (em memoria) |
| Lombok | Getters, setters, logging, construtores |
| Swagger / OpenAPI | springdoc-openapi 2.8.8 |
| Docker | Containerizacao |
| Maven | Build |

## Deploy

| Ambiente | URL |
|---|---|
| [App (Vercel)](https://heracles-tracker.vercel.app) | |
| [API (Render + Docker)](https://heracles-api-qdt8.onrender.com) | |
| [Swagger](https://heracles-api-qdt8.onrender.com/swagger-ui/index.html#/) | |

## Como Rodar

**Pre-requisitos:** Java 21

### Desenvolvimento (H2 em memoria)

```bash
# 1. Clone o repositorio
git clone https://github.com/GabrielNFR/heracles-api.git
cd heracles-api

# 2. Rode a aplicacao (Maven wrapper incluso, nao precisa instalar Maven)
./mvnw spring-boot:run

# 3. Acesse a API
#    http://localhost:8080/treinos
```

Perfil `default`: H2, DataLoader popula o banco com dados de exemplo.

### Banco H2 Console

Acesse `http://localhost:8080/h2-console`:

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:mem:heraclesdb` |
| Username | `sa` |
| Password | (vazio) |

## Swagger / OpenAPI

Documentacao interativa disponivel em:

```
http://localhost:8080/swagger-ui.html
```

### Schemas documentados

Todos os DTOs possuem `@Schema` com `description` e `example`. Endpoints possuem `@ApiResponses` com codigos 200, 201, 204, 400, 404, 500.

## Tratamento de Erros

Respostas de erro seguem o formato `ErrorResponse`:

```json
{
  "timestamp": "2026-06-30T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Treino nao encontrado com ID: 999",
  "path": "/treinos/999",
  "fieldErrors": null
}
```

| Codigo | Handler | Quando ocorre |
|---|---|---|
| 400 | `@Valid` DTO / `IllegalArgumentException` | Validacao de campos, exercicio de outro treino |
| 404 | `EntityNotFoundException` / `NoResourceFoundException` | ID inexistente, rota invalida |
| 500 | `Exception` (generico) | Erro interno (logado no servidor) |

Erros 400 de validacao incluem `fieldErrors` com mensagens por campo.

## Proximos Passos

### Autenticacao

- Spring Security + JWT
- Registro de usuario (`POST /auth/register`)
- Login (`POST /auth/login` → retorna token JWT)
- Endpoints protegidos (usuario so acessa seus proprios treinos)
