# Hostel CRM

Backend for a hostel management system: guests, apartments, categories, users, and roles.
REST API built with Kotlin, Spring Boot, PostgreSQL, Liquibase, and JWT.

No UI.

## Requirements

- JDK 25
- PostgreSQL 16 (for a local run without Docker)
- Docker Desktop — if you start the app with `docker compose`

## Configuration

| Variable | Purpose | Default |
|---|---|---|
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/crm` |
| `DB_USER` | Database user | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |
| `JWT_SECRET` | JWT HS256 signing key, at least 32 characters | `local-dev-secret-change-me-32b-minimum!!` |

Liquibase creates tables on application startup.

## Run with Docker

```bash
docker compose up --build
```

App: `http://localhost:8080`.  
The first build takes several minutes (Gradle downloads dependencies inside the image).

Stop: `docker compose down`.  
Wipe the database volume: `docker compose down -v`.

If port `5432` is already taken by a local Postgres, stop it or change the mapping in `docker-compose.yml` to `"5433:5432"`.

## Run locally

Create a `crm` database in PostgreSQL, then:

```bash
./gradlew bootRun
```

Or set your own values:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/crm
export DB_USER=postgres
export DB_PASSWORD=postgres
export JWT_SECRET=local-dev-secret-change-me-32b-minimum!!
./gradlew bootRun
```

## Tests

```bash
./gradlew test
```

Tests use in-memory H2 and do not need PostgreSQL.

## First user

On a fresh database you cannot create an admin via API: `POST /api/users` requires the `ADMINISTRATOR` role. Run this SQL once.

With Docker:

```bash
docker compose exec -T db psql -U postgres -d crm <<'SQL'
INSERT INTO role (name) VALUES ('ADMINISTRATOR');
INSERT INTO role (name) VALUES ('ACCOMMODATION_MANAGER');
INSERT INTO app_user (username, password_hash, role_id)
VALUES (
  'admin',
  '$2a$10$RZ5l9Tn1b8lWVIuHB55cX.f49BcNkqLCyOPaizHhVQysVhsg4TI.q',
  (SELECT id FROM role WHERE name = 'ADMINISTRATOR')
);
SQL
```

Locally, run the same SQL with `psql -U postgres -d crm`.

Login: **admin** / **admin123** (stored as BCrypt, not plain text).

## Authentication

`POST /api/auth/login` needs no token. Every other request:

```
Authorization: Bearer <token>
```

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}'
```

Response: `{ "token": "eyJ..." }`. Then:

```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/guests
```

The token expires in 1 hour. The `role` claim has no `ROLE_` prefix (`ADMINISTRATOR` or `ACCOMMODATION_MANAGER`).

## Roles

| | ADMINISTRATOR | ACCOMMODATION_MANAGER |
|---|---|---|
| Guests (all operations) | yes | yes |
| Guests in an apartment | yes | yes |
| List categories | yes | yes |
| Room count | yes | yes |
| Create / delete categories | yes | no |
| Apartments (create, delete, list, assign category) | yes | no |
| Users and roles | yes | no |

## API

Base URL: `http://localhost:8080`. Request bodies are JSON, dates are `YYYY-MM-DD`.

Status codes: **201** created, **204** deleted (empty body), **400** validation error, **401** missing/invalid token or login, **403** forbidden, **404** not found, **409** uniqueness conflict.

Error body:

```json
{ "message": "User 9 not found" }
```

### Auth

**POST** `/api/auth/login` — no token

Request:

```json
{ "username": "admin", "password": "admin123" }
```

Response `200`:

```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

### Categories

Allowed `name` values: `APARTMENT`, `BUSINESS`, `DELUXE`, `DUPLEX`, `SUPERIOR`, `STANDARD`.

| Method | Path | Roles | Description |
|---|---|---|---|
| GET | `/api/categories` | admin, manager | list |
| POST | `/api/categories` | admin | create |
| DELETE | `/api/categories/{id}` | admin | delete |

**POST** request:

```json
{ "name": "DELUXE", "description": "Deluxe room" }
```

`description` may be `null`.

**POST** response `201`:

```json
{ "id": 1, "name": "DELUXE", "description": "Deluxe room" }
```

### Apartments

| Method | Path | Roles | Description |
|---|---|---|---|
| GET | `/api/apartments` | admin | list |
| POST | `/api/apartments` | admin | create |
| DELETE | `/api/apartments/{id}` | admin | delete |
| PUT | `/api/apartments/{apartmentId}/category/{categoryId}` | admin | assign category |
| GET | `/api/apartments/{id}/room-count` | admin, manager | room count |

**POST** request:

```json
{ "apartmentNumber": "101", "roomCount": 2, "cleaningDate": "2026-09-21" }
```

`cleaningDate` may be omitted or `null`. `roomCount` must be > 0.

**POST** response `201`:

```json
{
  "id": 1,
  "apartmentNumber": "101",
  "roomCount": 2,
  "cleaningDate": "2026-09-21",
  "category": null
}
```

After assigning a category, `category` is the same object as in `GET /api/categories`.

**GET** `/api/apartments/{id}/room-count` returns a number, not an object: `2`.

### Guests

| Method | Path | Roles | Description |
|---|---|---|---|
| GET | `/api/guests` | admin, manager | list |
| POST | `/api/guests` | admin, manager | create |
| PUT | `/api/guests/{id}` | admin, manager | update |
| DELETE | `/api/guests/{id}` | admin, manager | delete |
| PUT | `/api/guests/{guestId}/apartment/{apartmentId}` | admin, manager | assign apartment |
| GET | `/api/guests/apartment/{apartmentId}` | admin, manager | guests in an apartment |

**POST** / **PUT** request:

```json
{
  "fullName": "Ivan Ivanov",
  "passport": "1234 567890",
  "photo": "/photos/ivan.jpg",
  "birthDate": "1990-05-12",
  "checkInDate": "2026-09-21",
  "checkOutDate": null
}
```

Required: `fullName`, `passport`, `birthDate`. Passport must be unique.

**Response:**

```json
{
  "id": 1,
  "fullName": "Ivan Ivanov",
  "passport": "1234 567890",
  "photo": "/photos/ivan.jpg",
  "birthDate": "1990-05-12",
  "checkInDate": "2026-09-21",
  "checkOutDate": null,
  "apartment": null
}
```

After assigning an apartment:

```json
"apartment": { "id": 1, "apartmentNumber": "101" }
```

### Roles

Allowed `name` values: `ADMINISTRATOR`, `ACCOMMODATION_MANAGER`.

| Method | Path | Roles | Description |
|---|---|---|---|
| GET | `/api/roles` | admin | list |
| POST | `/api/roles` | admin | create |

**POST** request: `{ "name": "ADMINISTRATOR" }`  
**POST** response `201`: `{ "id": 1, "name": "ADMINISTRATOR" }`

If roles were already inserted with the SQL above, a second POST returns **409**.

### Users

| Method | Path | Roles | Description |
|---|---|---|---|
| GET | `/api/users` | admin | list |
| POST | `/api/users` | admin | create |
| DELETE | `/api/users/{id}` | admin | delete |
| PUT | `/api/users/{userId}/role/{roleId}` | admin | assign role |

**POST** request:

```json
{ "username": "manager", "password": "password1" }
```

Password must be 8–255 characters. It is not returned in the response.

**POST** response `201`:

```json
{ "id": 2, "username": "manager", "role": null }
```

After assigning a role, `role` looks like `{ "id": 2, "name": "ACCOMMODATION_MANAGER" }`.
