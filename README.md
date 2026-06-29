# Heracles API (WIP)

API REST para registro de treinos, execuções e progressão de carga. 

## Stack

| Tecnologia | Versao |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.0 |
| Spring Data JPA (Hibernate) | 7.4 |
| Banco de dados | H2 (em memoria) |
| Build | Maven |
| Lombok | (getters, setters, logging, construtores) |

## Como Rodar

**Pre-requisitos:** Java 21

```bash
# 1. Clone o repositorio
git clone https://github.com/GabrielNFR/heracles-api.git
cd heracles-api

# 2. Rode a aplicacao (Maven wrapper incluso, nao precisa instalar Maven)
./mvnw spring-boot:run

# 3. Acesse a API
#    http://localhost:8080/treinos
```

Ao iniciar, o DataLoader popula o banco H2 (em memoria) com treinos e execucoes de exemplo. Os dados somem ao parar a aplicacao.

### Banco H2 Console

Acesse `http://localhost:8080/h2-console`:

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:mem:heraclesdb` |
| Username | `sa` |
| Password | (vazio) |

## Swagger / OpenAPI

Documentacao interativa disponivel em:

http://localhost:8080/swagger-ui.html
