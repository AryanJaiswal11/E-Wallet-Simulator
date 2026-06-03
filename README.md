# E-Wallet Simulator

## Overview

E-Wallet Simulator is a Spring Boot-based microservices application that simulates a digital wallet ecosystem. The system supports user registration, automatic wallet creation, wallet management, and secure fund transfers between users. The application follows an event-driven architecture using Apache Kafka to enable loose coupling and asynchronous communication between services.

The project demonstrates real-world backend development concepts including Microservices Architecture, Spring Security, REST APIs, Event-Driven Communication, Database Persistence, and Inter-Service Authentication.

---

## Features

* User Registration
* Automatic Wallet Creation
* Wallet Balance Management
* Fund Transfer Between Users
* Secure Authentication using Spring Security
* Event-Driven Processing with Apache Kafka
* Inter-Service Communication using REST APIs
* Independent Microservices
* Database Persistence using Hibernate/JPA
* Reduced Boilerplate Code using Lombok

---

## Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA (Hibernate)
* Lombok

### Database

* MySQL

### Messaging

* Apache Kafka

### Build Tool

* Maven

### API Testing

* Postman

---

## Architecture

The application consists of four independent modules:

### 1. User Service

Responsible for:

* User Registration
* User Management
* Publishing User Creation Events

### 2. Wallet Service

Responsible for:

* Wallet Creation
* Wallet Balance Management
* Processing Transactions
* Publishing Wallet Update Events

### 3. Transaction Service

Responsible for:

* Transaction Initiation
* Transaction Validation
* Publishing Transaction Events

### 4. User-Util Service

Shared authentication module used by:

* User Service
* Transaction Service

Provides:

* UserDetailsService Implementation
* Authentication Configuration
* Common User Authentication Logic

---

## High-Level Architecture

```text
                     +----------------+
                     | User Service   |
                     +----------------+
                              |
                              | User Created Event
                              v
                     +----------------+
                     | Apache Kafka   |
                     +----------------+
                              |
                              v
                     +----------------+
                     | Wallet Service |
                     +----------------+
                              |
                              |
                              v
                       Wallet Created


+--------------------+
| Transaction Service|
+--------------------+
          |
          | Transaction Initiated
          v
+--------------------+
| Apache Kafka       |
+--------------------+
          |
          v
+--------------------+
| Wallet Service     |
+--------------------+
          |
          | Update Wallet Balances
          |
          v
+--------------------+
| Wallet Updated     |
+--------------------+
```

---

## Kafka Event Flow

### User Creation Flow

1. User registers through User Service.
2. User information is saved to the database.
3. User Service publishes a Kafka event.
4. Wallet Service consumes the event.
5. Wallet Service automatically creates a wallet for the new user.

### Transaction Flow

1. User initiates a transaction.
2. Transaction Service validates the request.
3. Transaction details are published to Kafka.
4. Wallet Service consumes the transaction event.
5. Sender and receiver wallet balances are updated.
6. Wallet Service publishes a wallet update event.

This architecture keeps services loosely coupled and highly scalable.

---

## Project Structure

Each microservice follows a layered architecture.

### Controller Layer

Handles incoming HTTP requests and returns responses.

Responsibilities:

* Request Mapping
* Input Validation
* Delegating Business Logic

---

### DTO Layer

Used for transferring data between layers.

Responsibilities:

* Request Payloads
* Response Payloads
* Prevent Direct Exposure of Entities

Examples:

* UserDTO
* TransactionDTO
* WalletDTO

---

### Service Layer

Contains business logic.

Responsibilities:

* User Registration
* Wallet Creation
* Transaction Processing
* Kafka Publishing and Consumption

---

### Repository Layer

Handles database operations using Spring Data JPA.

Responsibilities:

* CRUD Operations
* Query Execution
* Entity Persistence

---

### Model (Entity) Layer

Represents database tables.

Responsibilities:

* Entity Mapping
* Relationship Definitions
* Persistence Representation

---

### Config Layer

Contains application configurations.

Responsibilities:

* Security Configuration
* Kafka Configuration
* Bean Configuration

---

### Enum Layer

Stores application constants.

Examples:

* Transaction Status
* Wallet Status
* User Roles

---

## Security

Authentication is implemented using:

* Spring Security
* HTTP Basic Authentication

Protected APIs require valid credentials.

User credentials are securely stored using BCrypt password encoding.

---

## APIs

### 1. Create User

```http
POST /users
```

Creates a new user.

Example Request:

```json
{
  "name": "Aman",
  "email": "aman@gmail.com",
  "username": "Aman",
  "password": "2211"
}
```

---

### 2. View Wallet

```http
GET /wallet/{walletID}
```

Returns wallet information for the specified wallet.

Example:

```http
GET /wallet/1
```

---

### 3. Initiate Transaction

```http
POST /transaction
```

Initiates a wallet-to-wallet transfer.

Example Request:

```json
{
  "receiverId": 2,
  "amount": 500
}
```

Authentication Required.

---

### 4. Internal API

```http
GET /users/internal/{username}
```

Used internally by services for authentication and user lookup.

Not intended for direct client usage.

---

## Running the Project

### Start Dependencies

1. MySQL
2. Zookeeper
3. Kafka Broker

### Start Services

Run services in the following order:

```text
1. User-Util Service
2. User Service
3. Wallet Service
4. Transaction Service
```

---

## Future Enhancements

* JWT Authentication
* API Gateway
* Service Discovery
* Docker Containerization
* Distributed Tracing
* Notification Service
* Transaction History
* Admin Dashboard
* Kubernetes Deployment

---

## Learning Outcomes

This project demonstrates:

* Microservices Architecture
* Event-Driven Systems
* Apache Kafka Integration
* Spring Security
* REST API Development
* Hibernate/JPA
* MySQL Integration
* Inter-Service Communication
* Layered Backend Design
* Scalable Application Development
