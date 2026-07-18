# Heracles API

> ### [Acesse o App → Heracles Tracker](https://heracles-tracker.vercel.app)
> Instalável no celular (PWA) — adicione à tela inicial e use offline (Navegadores Chromium e Safari)
> Offline-first com **TinyBase** — salva localmente e sincroniza com o servidor ao conectar

API REST para registro de treinos, execuções e progressão de carga.
Inspirada nos 12 Trabalhos de Hercules.

<details>
<summary><b>Heracles Tracker — Conheça o app</b></summary>

### Conta
- Crie sua conta com email e senha
- Faça login para acessar seus treinos
- Seus dados são só seus — ninguém mais vê
- Saia da conta com um toque

### Calendário
- Veja todos os seus treinos do mês ou da semana
- Cada treino tem sua cor — fácil de identificar
- Dias com treino feito ficam coloridos
- Toque num dia vazio para registrar um treino novo
- Toque num dia com treino para ver os detalhes

### Treinos
- Monte seus treinos: dê um nome e adicione os exercícios
- Exemplo: "Peito" com Supino, Crucifixo, Cross Over
- Todos os seus treinos aparecem em cards na tela
- Apague um treino quando não precisar mais

### Sua evolução
- Gráfico de linha mostra como sua carga aumentou
- De 50 kg para 55 kg no Leg Press? O gráfico mostra
- "+5 kg desde o início" — resumo em uma frase
- Funciona por exercício, não por treino

### Registrar treino feito
- Escolha o treino, a data e a hora
- Preencha séries, repetições e carga de cada exercício
- Campo de observações livre (ex: "senti o ombro")
- Duplique o treino de ontem e só ajuste o que mudou

### Histórico
- Todas as vezes que você fez um treino, em ordem
- Toque para expandir e ver séries × reps · carga
- Link rápido para os detalhes completos

### Detalhes
- Data, hora, todos os exercícios com séries e cargas
- Botão Duplicar — repita o mesmo treino com um toque
- Botão Excluir — remova um registro errado

</details>

## Stack

| Categoria | Stack |
|---|---|
| **Linguagem** | Java 21 |
| **Framework** | Spring Boot 4.1.0 |
| **Persistência** | Spring Data JPA (Hibernate 7.4) |
| **Segurança** | Spring Security 6.x + JWT (jjwt 0.12.6) |
| **Validação** | Bean Validation (Jakarta) |
| **Banco (prod)** | PostgreSQL (Neon) |
| **Banco (dev)** | H2 (em memória) |
| **Documentação** | Swagger/OpenAPI (springdoc 2.8.8) |
| **Build** | Maven |
| **Container** | Docker |
| **Monitoramento** | Spring Actuator |
| **Utilidades** | Lombok |

## Deploy

| Ambiente | URL |
|---|---|
| [App (Vercel)](https://heracles-tracker.vercel.app) | |
| [API (Render + Docker)](https://heracles-api-qdt8.onrender.com) | |
| [Swagger](https://heracles-api-qdt8.onrender.com/swagger-ui/index.html#/) | |

## Como Rodar

**Pré-requisitos:** Java 21

### Desenvolvimento (H2 em memória)

```bash
# 1. Clone o repositório
git clone https://github.com/GabrielNFR/heracles-api.git
cd heracles-api

# 2. Rode a aplicação (Maven wrapper incluso, não precisa instalar Maven)
./mvnw spring-boot:run

# 3. Acesse a API
#    http://localhost:8080
```

Perfil `default`: H2, `DataLoader` popula o banco com dados de exemplo e cria um usuário de teste (`teste@email.com` / `123456`).

### Banco H2 Console

Acesse `http://localhost:8080/h2-console`:

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:mem:heraclesdb` |
| Username | `sa` |
| Password | (vazio) |

## Swagger / OpenAPI

Documentação interativa disponível em:

```
http://localhost:8080/swagger-ui.html
```

### Schemas documentados

Todos os DTOs possuem `@Schema` com `description` e `example`. Endpoints possuem `@ApiResponses` com códigos 200, 201, 204, 400, 404, 500.

## Tratamento de Erros

Respostas de erro seguem o formato `ErrorResponse`:

```json
{
  "timestamp": "2026-06-30T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Treino não encontrado com ID: 999",
  "path": "/treinos/999",
  "fieldErrors": null
}
```

| Código | Handler | Quando ocorre |
|---|---|---|
| 400 | `@Valid` DTO / `IllegalArgumentException` | Validação de campos, exercício de outro treino |
| 401 | Spring Security | Token JWT ausente ou inválido |
| 404 | `EntityNotFoundException` / `NoResourceFoundException` | ID inexistente, rota inválida |
| 409 | `AuthController` | Email já cadastrado |
| 500 | `Exception` (genérico) | Erro interno (logado no servidor) |

Erros 400 de validação incluem `fieldErrors` com mensagens por campo.

## Autenticação

Endpoints protegidos com Spring Security + JWT. Credenciais válidas retornam um token que deve ser enviado no header `Authorization: Bearer <token>`.

### Endpoints

| Método | Endpoint | Autenticado |
|---|---|---|
| `POST` | `/auth/register` | Não |
| `POST` | `/auth/login` | Não |
| `POST` | `/auth/refresh` | Não (usa refresh token) |
| `POST` | `/auth/logout` | Não (usa refresh token) |
| `GET/POST/DELETE` | `/treinos/**` | Sim |
| `GET/POST/DELETE` | `/execucoes/**` | Sim |

### Exemplos

```bash
# Registrar
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@email.com","username":"user","password":"123456"}'

# Login (retorna access + refresh token)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@email.com","password":"123456"}'

# Usar token
curl http://localhost:8080/treinos \
  -H "Authorization: Bearer <access_token>"

# Renovar access token
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"uuid-do-login"}'

# Logout (revoga refresh token)
curl -X POST http://localhost:8080/auth/logout \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"uuid-do-login"}'
```

Access token expira em 15 minutos; refresh token dura 30 dias. Cada usuário acessa apenas seus próprios treinos e execuções.

## Pipeline CI/CD

```
git push main → GitHub Actions (JUnit + Mockito)
                  ├── passou → Deploy automático no Render via Deploy Hook
                  └── falhou → Bloqueia deploy
```

31 testes unitários cobrindo services, controllers e JWT.

## Próximos Passos

- 📄 **Paginação** — `GET /treinos?page=0&size=20` com links de navegação
