# pizza-ecommerce

🍕 Pizza E-Commerce - Microservices Architecture
Welcome to Pizza E-Commerce, a robust and scalable backend system built on a microservices architecture. This project demonstrates a production-grade approach to handling a digital pizza shop, managing product catalogs, user authentication, and shopping cart persistence in an isolated and resilient environment.

--------------------------------------
🏗️ System Architecture
The system is designed with independent, decoupled microservices that communicate through a centralized API Gateway:

- API Gateway (Port 8080): The single entry point. It handles request routing and centralized JWT (JSON Web Token) validation.

- User Service (Port 8082): Manages user registration, login, and issues secure authentication tokens.

- Product Service (Port 8081): Manages the pizza catalog, including categories, ingredients, and pricing.

- Cart Service (Port 8083): Handles real-time shopping cart management for authenticated users, utilizing identity propagation from the Gateway.

- Notification Service (Port 8084): The auditing and communication hub. It tracks system events and maintains a persistent audit log of all notifications.

--------------------------------------
🛠️ Technology Stack

Backend: 
- Java 21 & Spring Boot 3.4+

Infrastructure:

- Docker & Docker Compose: Containerization and orchestration of all supporting services.

- Apache Kafka: Distributed event streaming platform for asynchronous messaging.

- MySQL: Relational persistence utilizing the Database-per-Service pattern.

- Security: Spring Security with a Stateless JWT model and Header Propagation.

- Build Tool: Maven.

Frontend
- Angular: (Upcoming) A modern, reactive user interface.

--------------------------------------
🔄 Event-Driven Design (Kafka)

The project utilizes Apache Kafka to maintain eventual consistency across the distributed system without direct service coupling:

1. User Registration: The User Service emits a user-registered event; the Notification Service consumes it to trigger welcome workflows and audit logs.

2. Product Management: When a product is removed, the Product Service publishes a product-deleted event.
- The Cart Service consumes this to automatically sanitize active carts.
- The Notification Service consumes this to alert administrators of catalog changes.

--------------------------------------
📂 Infrastructure & Dockerization

The entire infrastructure is orchestrated via Docker, ensuring a "plug-and-play" environment for developers. Each service owns its dedicated schema to prevent tight coupling.

Data Persistence:

The system utilizes Docker Volumes to map internal MySQL data to the local directory ./mysql_data. This ensures that your databases (users, products, notifications) are preserved even if containers are destroyed or updated.
Service	Database Instance	External Port

User	pizza_user_db	3309
Product	pizza_product_db	3307
Cart	pizza_cart_db	3308
Notification	pizza_notification_db	3310
Messaging	Kafka Broker	9092

--------------------------------------
🚀 Getting Started

Prerequisites:
- Docker & Docker Desktop
- JDK 21+
- Maven 3.9+

Setup & Run:
1. Clone the repository:
- git clone https://github.com/your-username/pizza-ecommerce.git
- 
2. Spin up the Infrastructure (DBs & Kafka):
  From the project root, run:
- docker-compose up -d

  Note: This will automatically create a ./mysql_data folder to persist your data.

3. Build & Launch:

Run (in the root (or in each service folder)):
- mvn clean install

Start services in order: User, Product, Cart, Notification, and finally the Gateway.

4. Stopping the System:

Use (to pause the infrastructure (keeps data and containers)):
- docker-compose stop

Use (to remove containers (keeps data safe in volumes)):
- docker-compose down

--------------------------------------

🔐 Identity & Security Flow
1. Authentication: Client logs in via User Service and receives a signed JWT.

2. Validation: The Gateway validates the JWT signature at the perimeter.

3. Propagation: The Gateway extracts user metadata and injects it into a custom X-User-Email header.

4. Downstream Logic: Internal services trust the Gateway-validated header, ensuring high performance by avoiding redundant token parsing.

--------------------------------------
📂 Database Design

To ensure the Database-per-Service pattern, each service owns its dedicated schema, ensuring high isolation and fault tolerance:

- pizza_user_db (Port 3309): User profiles, roles, and encrypted credentials.

- pizza_product_db (Port 3307): Full product catalog (Pizzas, Ingredients, Categories).

- pizza_cart_db (Port 3308): Persistence of cart items associated with unique user emails.

- pizza_notification_db (Port 3310): Historical audit logs of all system notifications and events.

--------------------------------------

🛤️ Roadmap:

[x] Microservices Architecture & API Gateway Integration

[x] Centralized JWT Authentication & Identity Propagation

[x] Asynchronous Messaging with Apache Kafka

[x] Full Infrastructure Dockerization

[x] Persistent Notification Audit Log

[ ] Order Service & Payment Integration

[ ] Angular 17+ Reactive Frontend Development

--------------------------------------