<h1 align="center"> Expense Tracker API

![java](https://img.shields.io/static/v1?label=java&message=21.0.8&color=2d3748&logo=openjdk&style=flat-square)
![spring boot](https://img.shields.io/static/v1?label=spring%20boot&message=4.0.6&color=2d3748&logo=springboot&style=flat-square)
![postgresql](https://img.shields.io/static/v1?label=postgres&message=18.3&labelColor=2d3748&color=grey&logo=postgresql&logoColor=white&style=flat)
![docker](https://img.shields.io/static/v1?label=docker&message=29.4.3&color=2d3748&logo=docker&style=flat-square)
![swagger](https://img.shields.io/static/v1?label=swagger&message=3.0.3&color=2d3748&logo=swagger&style=flat-square)
</h1>

## Table of Contents

- [About](#about)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
    - [Running with Docker (Recommended)](#running-with-docker-recommended)
    - [Running Locally (Without Docker)](#running-locally-without-docker)
    - [Environment Variables Reference](#environment-variables-reference)
- [Usage](#usage)
    - [Routes](#routes)
        - [Requests](#requests)


## About

This is my solution for [Expense Tracker API](https://roadmap.sh/projects/expense-tracker-api) challenge, provided
by [Developer Roadmaps](https://roadmap.sh/). It is a RESTful API designed to track user expenses, allowing users to
create, list, update, and delete expenses. Users can register and log in, and each user has their own isolated set of
expenses.

**Key features:**

- JWT-based authentication and authorization
- Input validation for authentication and expense operations
- Full CRUD operations for expenses
- Pagination and flexible sorting for expense listing
- Date range filtering for expenses
- Swagger UI documentation
- Soft delete support for user profiles

## Requirements

**For Docker (Recommended):**

- Docker & Docker Compose

**For Local Development:**

- Java 21+
- Maven 3.9+
- PostgreSQL 18+

## Getting Started

### Running with Docker (Recommended)

This is the simplest setup. Docker Compose will automatically build and start all required services, using default values in the [Compose file](./docker-compose.yml).

```bash
docker compose up -d --build
```

Then access the application at `http://localhost:8080/` (or the port you configured).

**Optionally**, you can override any environment variables with your own settings.
```bash
cp .env.example .env
# Edit .env with your local values
```

### Running Locally (Without Docker)

1. Create a PostgreSQL database
2. Set the required environment variables using **either** method below:
    - **Option 1: Using `.env` file**. Copy the example file and edit with your values:
      ```bash
      cp .env.example .env
      # Edit .env with your local settings
      ```

    - **Option 2: Exporting via shell**
      ```bash
      export PG_USERNAME=<your-postgresql-user-here>
      export PG_PASSWORD=<your-postgresql-password-here>
      export POSTGRES_DB=<database-name>
      export JWT_SECRET=<jwt-secret-here>
      ```

3. (Optional) Set server port:
```bash
export PORT=8081
```
4. Start the application:
```bash
mvn spring-boot:run
```
5. Access at `http://localhost:8080/` (or the port you configured).

### Environment Variables Reference

| Variable      | For Docker                       | For Local Development                                     | Description                                                                 |
|---------------|----------------------------------|-----------------------------------------------------------|-----------------------------------------------------------------------------|
| PORT          | Optional (Default: `8080`)       | Optional (Default: `8080`)                                | Server port                                                                 |
| PG_USERNAME   | Optional (Default: `admin`)      | **Required**                                              | PostgreSQL username                                                         |
| PG_PASSWORD   | Optional (Default: `dbpassword`) | **Required**                                              | PostgreSQL password                                                         |
| POSTGRES_DB   | Optional (Default: `expenses_db`)| **Required** (must match the manually created database)   | PostgreSQL database name                                                    |
| JWT_SECRET    | Optional (Default: see compose)  | **Required**                                              | Secret key for signing JWT tokens. **Use a strong random value in production.** |

> [!WARNING]
> Never use the default `JWT_SECRET` value in production. Generate a secure secret with:
> ```bash
> openssl rand -hex 32
> ```

## Usage

Once the application is running, you can interact via Swagger UI at `/docs` or directly through HTTP requests.

### Routes

| Route                      | HTTP Method | Params                                                                                                                                                                                                                                                                                                                                                     | Description                                 | Auth   |
|----------------------------|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------|--------|
| `/docs`                    | GET         | -                                                                                                                                                                                                                                                                                                                                                          | Swagger documentation                       | None   |
| `/api/v1/auth/register`    | POST        | Body with `name`, `email` and `password`                                                                                                                                                                                                                                                                                                                   | Register a new user                         | None   |
| `/api/v1/auth/login`       | POST        | Body with `email` and `password`                                                                                                                                                                                                                                                                                                                           | Login an existing user                      | None   |
| `/api/v1/users/{id}`       | PUT         | `{id}` + Body with fields to update                                                                                                                                                                                                                                                                                                                        | Update user profile                         | Bearer |
| `/api/v1/users/{id}`       | DELETE      | `{id}`                                                                                                                                                                                                                                                                                                                                                     | Delete user profile                         | Bearer |
| `/api/v1/expenses`         | GET         | **Query Parameters:**<br>• `page` - Page number (default: `0`)<br>• `size` - Page size (default: `10`)<br>• `orderBy` - Sort field (default: `name`)<br>• `direction` - Sort direction: `ASC`/`DESC` (default: `ASC`)<br>• `startDate` - Filter from date (format: `yyyy-MM-dd`)<br>• `endDate` - Filter until date (format: `yyyy-MM-dd`) | Retrieve paginated and sorted expenses      | Bearer |
| `/api/v1/expenses`         | POST        | Body with `name`, `description`, `amount`, `category` and `date`                                                                                                                                                                                                                                                                                           | Create a new expense                        | Bearer |
| `/api/v1/expenses/{id}`    | GET         | `{id}`                                                                                                                                                                                                                                                                                                                                                     | Retrieve an expense by its ID               | Bearer |
| `/api/v1/expenses/{id}`    | PATCH       | `{id}` + Body with fields to update                                                                                                                                                                                                                                                                                                                        | Update an existing expense                  | Bearer |
| `/api/v1/expenses/{id}`    | DELETE      | `{id}`                                                                                                                                                                                                                                                                                                                                                     | Delete an existing expense                  | Bearer |

#### Requests

- `POST /api/v1/auth/register`

```json
{
  "name": "John Doe",
  "email": "johndoe@gmail.com",
  "password": "#P4ssword_"
}
```

- `POST /api/v1/auth/login`

```json
{
  "email": "johndoe@gmail.com",
  "password": "#P4ssword_"
}
```

- `PUT /api/v1/users/{id}`

```json
{
  "email": "johndoeupdated@gmail.com"
}
```

- `POST /api/v1/expenses`

```json
{
  "name": "Prime Video",
  "description": "Amazon Prime subscription",
  "amount": 19.9,
  "category": "subscriptions",
  "date": "2025-12-12"
}
```

- `PATCH /api/v1/expenses/{id}`

```json
{
  "amount": 24.9
}
```

[⬆ Back to the top](#-expense-tracker-api)