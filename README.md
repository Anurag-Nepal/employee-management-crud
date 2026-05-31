# Employee Management System API

## Project Overview
This is an enterprise-grade Java application demonstrating a robust, secure, and production-ready Employee Management System. The primary objective of this project is to manage employee records securely, where all CRUD operations communicate with the database strictly through **MySQL Stored Procedures**. It acts as a central system where an administrator can authenticate and manage an organization's workforce.

## Tech Stack
* **Java 17 / 21**
* **Spring Boot 3.x** (Web, Data JPA, Security, Cache)
* **MySQL 8** (Database & Stored Procedures)
* **Redis** (Caching layer to optimize performance)
* **Spring Security & JWT** (Stateless authentication)
* **Docker & Docker Compose** (Containerization & Infrastructure)
* **Swagger/OpenAPI** (Interactive API Documentation)
* **Lombok & MapStruct** (Boilerplate reduction and object mapping)

---

## How to Run

Running the entire infrastructure and application is fully automated via Docker Compose.

1. Ensure Docker and Docker Compose are installed.
2. From the root of the project directory, run:

```bash
docker-compose up --build
```

This command will:
- Spin up the **MySQL** container and automatically execute the initialization SQL scripts to create schemas, tables, and stored procedures.
- Spin up the **Redis** container.
- Build the Spring Boot application and start the **employee-app** container.

Once everything is up, you can access the interactive API documentation and test endpoints directly through **Swagger UI**:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

## Application Flow & Usage

1. **Data Seeding**: Registration is strictly disabled for public users to ensure security. Instead, upon application startup, a `DataSeeder` (`CommandLineRunner`) silently checks if the default admin exists. If not, it automatically seeds an admin user with the following credentials:
   - **Email**: `admin@admin.com`
   - **Password**: `admin123`
2. **Login**: The admin logs in using the `/api/v1/auth/login` endpoint and receives a JWT token.
3. **Authorization**: In Swagger UI, click the **"Authorize"** button at the top and paste the token. This acts as central auth and applies the Bearer token to all subsequent requests.
4. **Employee Management**: The admin can now create, read, update, and delete employees. Note that sensitive attributes like `password` and `salary` are protected and omitted from the update DTOs.

---

## Project Structure & Architecture

The application strictly follows Clean Architecture principles:

### Database & Stored Procedures (`db/init/`)
Direct SQL queries are explicitly avoided in the Java logic. The database is initialized via Docker using scripts that create tables, indexes, and **Stored Procedures**. 
In the Java codebase, the `EmployeeRepository` extends `JpaRepository` and uses the `@Procedure` annotation to invoke these pre-compiled SQL procedures (e.g., `sp_create_employee`, `sp_update_employee`). This enforces ACID compliance and logic directly at the database tier.

### Services (`service/`)
The service layer contains business logic. It handles data processing, caching logic (`@Cacheable`, `@CacheEvict` via Redis), and prepares data for the repository. 

### Transactions (`@Transactional`)
The `@Transactional` annotation is used across the service layer to ensure data integrity during operations. 
*Note on Read-Only Transactions:* While standard `GET` queries often utilize `@Transactional(readOnly = true)` for performance, we deliberately use the standard `@Transactional` without the read-only flag for all stored procedure invocations. This is because the MySQL JDBC driver conservatively treats `CALL` statements as potential data-modifying operations and will throw exceptions if a stored procedure is executed on a strictly read-only connection.

### Uniform Responses & Exception Handling
- **`ApiResponse<T>`**: The API guarantees a uniform, predictable JSON payload across **every** endpoint (both success and error responses), structured with `status`, `timestamp`, and `data` fields. 
- **`GlobalExceptionHandler`**: Intercepts exceptions (e.g., Validation Errors, Resource Not Found, Database exceptions) and seamlessly maps them into the standardized `ApiResponse` structure, preventing stack traces from leaking to the client.

---

## Future Improvements

Due to limited time, the scope of this project was kept to core functionality. If given more time, the following production enhancements would be implemented:

1. **OAuth2 / Keycloak Integration**: Moving away from standard JWT to a robust, centralized Identity and Access Management (IAM) provider like Keycloak for enterprise-grade security.
2. **Role-Based Access Control (RBAC)**: Implementing granular authorization to distinguish between `ADMIN`, `HR`, and standard `EMPLOYEE` roles.
3. **OTP Verification & Emailing**: Adding Two-Factor Authentication (2FA) and email verification steps via an SMTP server during new employee onboarding.
4. **Rate Limiting**: Implementing API Gateway level or application-level rate limiting (e.g., using Bucket4j) to protect against brute-force attacks and DDOS.
5. **Auditing & Logging**: Adding Hibernate Envers or database triggers to maintain historical audit logs of salary and department changes.
