# Todo App Backend

This is the RESTful API backend for the Todo application, built with Spring Boot.

## Technologies
- **Java 8**
- **Spring Boot 2.7.18**
- **Spring Data JPA**
- **MySQL Database**
- **Maven**

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- Maven
- MySQL Server

## Configuration

The application configuration is located in `src/main/resources/application.yml`.

### Database
Ensure you have a MySQL database named `todo_db` created:
```sql
CREATE DATABASE todo_db;
```

Update the `username` and `password` in `application.yml` to match your local MySQL installation:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todo_db?useSSL=false&serverTimezone=UTC
    username: root
    password: root # Change this to your password
```

## Running the Application

You can run the application using the Maven wrapper or your local Maven installation.

### Using Maven Wrapper
```bash
./mvnw spring-boot:run
```

### Using Local Maven
```bash
mvn spring-boot:run
```

The server will start on port **8080**.

## API Endpoints

| Method | Endpoint      | Description           | Payload (JSON) |
|--------|---------------|-----------------------|----------------|
| GET    | `/api/todos`  | Get all todos         | N/A            |
| POST   | `/api/todos`  | Create a new todo     | `{"title": "...", "completed": false}` |
| PUT    | `/api/todos/{id}` | Update a todo     | `{"id": 1, "title": "...", "completed": true}` |
| DELETE | `/api/todos/{id}` | Delete a todo     | N/A            |

## Project Structure
- `src/main/java/com/example/learn/controller` - REST Controllers
- `src/main/java/com/example/learn/entity` - JPA Entities
- `src/main/java/com/example/learn/repository` - Data Access Layer
