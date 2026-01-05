<h1 align="center"> Expense Tracker API

![java](https://img.shields.io/static/v1?label=java&message=21.0.8&color=2d3748&logo=openjdk&style=flat-square)
![spring boot](https://img.shields.io/static/v1?label=spring%20boot&message=3.5.4&color=2d3748&logo=springboot&style=flat-square)
![mongodb](https://img.shields.io/badge/mongodb-8.0.17-4b32c3?style=flat-square&logo=mongodb&color=2d3748)
![docker](https://img.shields.io/static/v1?label=docker&message=28.5.0&color=2d3748&logo=docker&style=flat-square)
![swagger](https://img.shields.io/static/v1?label=swagger&message=2.8.5&color=2d3748&logo=swagger&style=flat-square)
</h1>

## Table of Contents

- [About](#about)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
    - [Configuring](#configuring)
        - [.env](#env)
- [Usage](#usage)
    - [Starting](#starting)
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

## Requirements:

**For Docker (Recommended):**

- Docker & Docker Compose

**For Local Development:**

- Java 21+
- Maven 3.9+
- MongoDB

## Getting Started

### Configuring

#### **.env**

Rename  `.env.example` to `.env` and modify variables according to your needs.

| Variable       | For Docker                               | For Local Development                    | Description                       |
|----------------|------------------------------------------|------------------------------------------|-----------------------------------|
| PORT           | Optional (Default: "8080")               | Optional (Default: "8080")               | Server port                       |
| MONGO_USER     | Optional (Default: "admin")              | **Required**                             | MongoDB username                  |
| MONGO_PASSWORD | Optional (Default: "dbpassword")         | **Required**                             | MongoDB password                  |
| MONGO_DATABASE | Optional (Default: "expensetrackerdb")   | Optional (Default: "expensetrackerdb")   | MongoDB database                  |
| JWT_SECRET     | **Required** (Default: "supersecretkey") | **Required** (Default: "supersecretkey") | Secret key for signing JWT tokens |

## Usage

### **Starting**

The fastest way to run the application is using Docker Compose:

```bash
# Run docker compose command to start all services
$ docker compose up -d --build
```

Once started, access the Swagger documentation at `http://localhost:8080/docs` (or the port you configured).

### **Routes**

| Route                   | HTTP Method | Params                                                                                                                                                                                                                                                                                                                                                     | Description                                 | Auth Method |
|-------------------------|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------|-------------|
| `/docs`                 | GET         | -                                                                                                                                                                                                                                                                                                                                                          | Swagger documentation                       | None        |
| `/api/v1/auth/register` | POST        | Body with `name`, `email` and `password`                                                                                                                                                                                                                                                                                                                   | Register a new user                         | None        |
| `/api/v1/auth/login`    | POST        | Body with `email` and `password`                                                                                                                                                                                                                                                                                                                           | Login an existing user                      | None        |
| `/api/v1/users/:id`     | PUT         | `:id` + Body with information to be update.                                                                                                                                                                                                                                                                                                                | Update user profile                         | Bearer      |
| `/api/v1/users/:id`     | DELETE      | `:id`                                                                                                                                                                                                                                                                                                                                                      | Delete user profile                         | Bearer      |
| `/api/v1/expenses`      | GET         | **Query Parameters:**<br>• `page` - Page number (default: 0)<br>• `size` - Page size (default: 10)<br>• `orderBy` - Sort field (default: "name")<br>• `direction` - Sort direction: ASC/DESC (default: "ASC")<br>• `startDate` - Filter expenses from this date (format: yyyy-MM-dd)<br>• `endDate` - Filter expenses until this date (format: yyyy-MM-dd) | Retrieve paginated expenses with sorting    | Bearer      |
| `/api/v1/expenses`      | POST        | Body with `name`, `description`, `amount`, `category` and `date`                                                                                                                                                                                                                                                                                           | Create a new expense                        | Bearer      
| `/api/v1/expenses/:id`  | GET         | `:id`                                                                                                                                                                                                                                                                                                                                                      | Retrieve existing expense by its unique id. | Bearer      |
| `/api/v1/expenses/:id`  | PATCH       | `:id` + Body with information to be update.                                                                                                                                                                                                                                                                                                                | Update an existing expense information      | Bearer      |
| `/api/v1/expenses/:id`  | DELETE      | `:id`                                                                                                                                                                                                                                                                                                                                                      | Delete an existing expense                  | Bearer      |

#### Requests

- `POST /api/v1/auth/register`

Request body:

```json
{
  "name": "John Doe",
  "email": "johndoe@gmail.com",
  "password": "#P4ssword_"
}
```

- `POST /api/v1/auth/login`

Request body:

```json
{
  "email": "johndoe@gmail.com",
  "password": "#P4ssword_"
}
```

- `PUT /api/v1/users/:id`

Request body:

```json
{
  "email": "johndoeupdated@gmail.com"
}
```

- `POST /api/v1/expenses`

Request body:

```json
{
  "name": "Prime Video",
  "description": "Amazon Prime subscription",
  "amount": 19.9,
  "category": "subscriptions",
  "date": "2025-12-12"
}
```

- `PATCH /api/v1/expenses/:id`

Request body:

```json
{
  "amount": 24.9
}
```

[⬆ Back to the top](#-expense-tracker-api)