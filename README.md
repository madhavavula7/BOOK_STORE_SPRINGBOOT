Backend: System Architecture & API Specification
Location: /backend/README.md

Bookstore Core Service (Distributed System)
This repository houses the mission-critical backend engine, architected using the Spring Boot framework. It follows a Layered Architecture (Controller-Service-Repository) to ensure high maintainability and scalable business logic.

Enterprise Stack

Java 17 (LTS): Utilizing modern syntax and Stream APIs for data processing.

Spring Security & JWT: Stateless authentication with high-entropy token signing.

PostgreSQL: Relational data persistence with ACID compliance.

Hibernate/JPA: Optimized ORM mapping for complex entity relationships.

Core Functional Modules

Financial Middleware: Calculates dynamic GST (18%) and enforces transactional integrity during checkout.

Inventory Watchdog: A synchronized stock validation service that prevents race conditions during high-concurrency purchases.

RBAC Gateway: Strict Role-Based Access Control to separate ADMIN logistics from CUSTOMER procurement.

Local Development

Configure application.properties with your PostgreSQL credentials.

Run ./mvnw spring-boot:run.

The API will be accessible at http://localhost:8080.
