# Real Estate Backend

Backend REST API for a real estate platform developed with **Java** and **Spring Boot**.

The project focuses on authentication, authorization, role-based access control and business logic for managing properties and users.

## Tech Stack
- Java 17
- Spring Boot
- Spring Security
- JWT (Access & Refresh Token)
- JPA / Hibernate
- MySQL
- Maven
- Postman

## Main Features
- User authentication and authorization using JWT
- Role-based access control (USER, MANAGER, ADMIN)
- Property management
- Visit booking and approval workflow
- Offers management (create, accept, reject)
- Favorites management
- Ownership and business rules enforced at service level
- Global exception handling

## Roles
- **USER**: browse properties, request visits, make offers, manage favorites
- **MANAGER**: manage own properties, approve visits, handle offers
- **ADMIN**: full system access and user management

## Architecture
- Controller layer (REST API)
- Service layer (business logic)
- Repository layer (JPA/Hibernate)
- DTOs for request/response mapping

## Security
- Stateless authentication with JWT
- Access and refresh tokens
- Endpoint protection with Spring Security and `@PreAuthorize`

## API Testing
All endpoints can be tested using **Postman** with Bearer Token authentication.

## Notes
This project was developed as a learning and portfolio project to practice backend development and Spring Security best practices.
