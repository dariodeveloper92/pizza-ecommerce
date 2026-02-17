# pizza-ecommerce

🍕 Pizza E-Commerce - Microservices Architecture
Welcome to Pizza E-Commerce, a robust and scalable backend system built on a microservices architecture. This project demonstrates a production-grade approach to handling a digital pizza shop, managing product catalogs, user authentication, and shopping cart persistence in an isolated and resilient environment.

🏗️ System Architecture
The system is designed with independent, decoupled microservices that communicate through a centralized API Gateway:

API Gateway (Port 8080): The single entry point. It handles request routing and centralized JWT (JSON Web Token) validation.

User Service (Port 8082): Manages user registration, login, and issues secure authentication tokens.

Product Service (Port 8081): Manages the pizza catalog, including categories, ingredients, and pricing.

Cart Service (Port 8083): Handles real-time shopping cart management for authenticated users, utilizing identity propagation from the Gateway.

🛠️ Technology Stack
Backend
Java 21 & Spring Boot 3.4+

Spring Cloud Gateway: For edge routing and perimeter security.

Spring Security: Implementation of a Stateless security model.

JSON Web Token (JWT): For secure, cross-service authentication.

Hibernate / Spring Data JPA: For ORM and data persistence.

MySQL: Relational databases (dedicated instance per microservice).

Apache Kafka: Integrated for asynchronous communication and notification systems.

Frontend
Angular: (Upcoming) A modern, reactive user interface.

🔐 Security & Identity Flow
The project implements an advanced Gateway Aggregation & Propagation pattern:

Authentication: The client sends credentials to the User Service. Upon success, it receives an HMAC-SHA256 signed JWT.

Authorization: Every request to protected services (Cart, Product) must include the Authorization: Bearer <token> header.

Header Propagation: The Gateway validates the token signature. If valid, it extracts the user's email and "injects" it into a custom X-User-Email header before forwarding the request to internal microservices.

Identity Awareness: The Cart Service reads the X-User-Email header to identify the owner of the cart, ensuring high security without the need to re-verify the JWT signature at every internal hop.

📂 Database Design
To ensure the Database-per-Service pattern, each service owns its schema:

pizza_user_db: User profiles, roles, and credentials.

pizza_product_db: Full product catalog (Pizzas, Ingredients, Categories).

pizza_cart_db: Persistence of cart items associated with unique user emails.

🚀 Getting Started
Prerequisites
JDK 21+

MySQL 8.0+

Maven 3.9+

Apache Kafka (running for messaging features)

Setup
Clone the repository:

Bash

git clone https://github.com/your-username/pizza-ecommerce.git
Configure database credentials in the application.properties of each microservice.

Build the project:

Bash

mvn clean install
Start the services in the following order: User, Product, Cart, and finally the Gateway.

🛤️ Roadmap
[x] Microservices Architecture & API Gateway Integration

[x] Centralized JWT Authentication

[x] Identity Propagation for Cart Management

[ ] Order Service Implementation

[ ] Kafka-based email confirmation system

[ ] Angular 17+ Frontend Development