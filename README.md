# Smart Library Management System

A Java-based desktop application for managing library operations with a focus on transactions, testability, and real-world usability.

## 🚀 Overview

The **Smart Library Management System** is designed to provide librarians (ADMIN) and students (BORROWER) with a robust platform for book management, loan processing, and fine calculation. The system leverages a clean layered architecture and ensures data consistency through PostgreSQL transactions.

## ✨ Key Features

- **Book Management**: Full CRUD operations with inventory tracking.
- **Student Management**: Registration and borrowing history.
- **Borrow & Return System**: Rule-based borrowing with automatic inventory updates.
- **Due Dates & Fines**: Automatic fine calculation and loan blocking.
- **Role-Based Security**: Different interfaces for Admins and Students.
- **Audit Logs**: Detailed tracking of system activities.
- **Bonus Features**: Email simulation, Dark Mode, and CSV Import.

## 🛠 Tech Stack

- **Language**: Java 25 (LTS)
- **UI Framework**: Java Swing
- **Build Tool**: Maven 3.9+
- **Database**: PostgreSQL 16+ (Dockerized)
- **Containerization**: Docker & Docker Compose
- **Testing**: JUnit 5 for TDD

## 🏗 Architecture

The project follows a clean layered architecture:
- **Presentation Layer**: Java Swing UI
- **Service Layer**: Business logic and transaction management
- **Repository Layer**: Data access using the DAO pattern
- **Database Layer**: PostgreSQL with ACID compliance

## 🚀 Quick Start

### Prerequisites
- JDK 25
- Maven 3.9+
- Docker Desktop
- Git

### Setup & Running
1. Clone the repository
2. Start the database:
   ```bash
   docker-compose up -d postgres
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="com.library.Main"
   ```

## 🧪 Testing
Run unit tests with:
```bash
mvn test
```

## 📄 License
This project is for academic purposes.
