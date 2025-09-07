# BankLite API

## 🚀 About The Project

**BankLite API** is a backend study project that simulates the core functionalities of a banking API. The goal was to build a robust, secure, and well-architected system, covering the full development lifecycle from setting up a local environment with Docker to deploying on a cloud platform.

This project was developed to demonstrate proficiency in the Java/Spring stack and modern software engineering practices, such as integration testing, database migrations, and token-based security.

## ✨ Live Demo

The API is deployed and running on the cloud via Render. You can interact with the documentation and test the endpoints publicly.

- **Interactive Documentation (Swagger UI):** https://bank-lite-api.onrender.com/swagger-ui.html
- **Health Check Endpoint:** https://bank-lite-api.onrender.com/actuator/health

*(Note: Render's free tier may spin down the service after a period of inactivity. The first request might take 30-60 seconds to "wake up" the application).*

## 📋 Features Implemented

### 👤 Clients
- Full CRUD for client management (`name`, `email`, `CPF`).
- Input validation, including email format and mathematical validation for Brazilian CPF.
- Conflict handling (`409 Conflict`) for duplicate CPFs or emails.

### 💳 Accounts
- Account creation, always linked to an existing client.
- Ability to check account balance and status.
- Functionality to block and unblock accounts.

### 💸 Transactions
- **Deposit:** Adds funds to an account's balance.
- **Withdrawal:** Removes funds from an account's balance, with insufficient funds validation.
- **Transfer:** Moves funds between two accounts, with security and balance validations.
- **Statement:** Fetches a list of all transactions for a specific account.

### 🔐 Security
- **Authentication:** Dual-token login system (**Access Token** + **Refresh Token**) using JWT.
- **Authorization:** Role-based route protection.
   - `ROLE_ADMIN`: Full access to the API.
   - `ROLE_USER`: Restricted access to read-only operations.
- Public authentication endpoints (`/login`, `/refresh`).

## 🛠️ Tech Stack

- **Backend:** Java 17, Spring Boot 3
- **Persistence:** Spring Data JPA, Hibernate
- **Database:** PostgreSQL
- **Migrations:** Flyway
- **Security:** Spring Security, JSON Web Token (JWT)
- **Documentation:** Springdoc OpenAPI (Swagger UI)
- **Testing:**
   - **Unit:** JUnit 5, Mockito
   - **Integration:** Testcontainers
- **Local Infrastructure:** Docker, Docker Compose
- **Deployment:** Render

## ⚙️ Running Locally

To run this project in your local environment, you need to have **Docker Desktop** installed.

### 1. Clone the repository:

```bash
git clone https://github.com/JoaoPedroAraujoSouza/bank-lite-api.git
cd bank-lite-api
```

### 2. Start the services with Docker Compose:

This command will start a container with the PostgreSQL database and Adminer (a graphical interface for the DB).

```bash
docker-compose up -d
```

### 3. Run the Spring Boot Application:

Open the project in your favorite IDE (e.g., IntelliJ IDEA) and run the main class `BankliteApiApplication.java`.

The application will start on port `8088`.

- **Local API:** `http://localhost:8088`
- **Local Swagger UI:** `http://localhost:8088/swagger-ui.html`
- **Adminer (to view the database):** `http://localhost:8081`
   - **System:** `PostgreSQL`
   - **Server:** `db`
   - **Username:** `postgres`
   - **Password:** `postgres`
   - **Database:** `banklite_db`

## 📚 API Documentation

The complete documentation for all endpoints, including data models and error responses, is available via Swagger UI.

- **Production:** https://bank-lite-api.onrender.com/swagger-ui.html
- **Local:** `http://localhost:8088/swagger-ui.html`