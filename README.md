# Shoply

Marketplace onde compradores encontram e adquirem produtos de vendedores. Os requisitos completos e as decisões de escopo estão documentados em [REQUISITOS.md](./REQUISITOS.md).

## Estrutura do projeto

```
Shoply/
├── backend/    # API em Spring Boot (Java)
└── frontend/   # Aplicação em React + TypeScript
```

## Backend

- **Stack**: Java 26, Spring Boot 4.1.1 (Web, Security, Data JPA, OAuth2 Resource Server, Validation)
- **Banco de dados**: H2 (em memória, uso em desenvolvimento)

### Executando

```bash
cd backend
./mvnw spring-boot:run
```

A API sobe por padrão em `http://localhost:8080`.

### Endpoints disponíveis

| Método | Rota | Descrição |
| --- | --- | --- |
| POST | `/api/usuarios` | Cadastra um novo usuário |
| GET | `/api/usuarios/{id}` | Busca um usuário por ID |
| POST | `/api/autenticacao/login` | Autentica um usuário e retorna um access token |

## Frontend

- **Stack**: React, TypeScript, Vite, React Router
- **Identidade visual**: preto e vermelho como cores principais, com cuidado de contraste e acessibilidade (vermelho nunca é o único indicador de erro/estado)

### Executando

```bash
cd frontend
npm install
npm run dev
```

A aplicação sobe por padrão em `http://localhost:5173`.

### Páginas implementadas

- **Home** (`/`) — vitrine inicial com produtos em destaque
- **Cadastro** (`/cadastro`) — criação de conta de comprador
- **Login** (`/login`) — autenticação, integrada com `/api/autenticacao/login`

## Status do projeto

Projeto em desenvolvimento inicial. Consulte [REQUISITOS.md](./REQUISITOS.md) para o escopo confirmado, pendências e decisões em aberto.
