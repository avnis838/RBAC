# 📌 RBAC Project (Spring Boot + MySQL/PostgreSQL + Kafka + Docker)

A **Role-Based Access Control (RBAC)** microservice implemented using **Java 21** and **Spring Boot**, with support for:

| Setup Option | Database | Kafka |
|---|----------|-------|
| 1 | MySQL (Docker) | Docker |

---

## 🧠 Features

- Spring Boot (Java 21)
- RBAC: Users, Roles
- Kafka event streaming
- MySQL or PostgreSQL support
- Dockerized Kafka & Zookeeper
- Clean enterprise-grade project structure

---

## 📁 Project Structure

```text
RBAC/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── .gitignore
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/RBACUserManagement/
│   │   │       ├── config/
│   │   │       │   ├── DataInitializer
│   │   │       │   ├── KafkaConfig
│   │   │       │   └── SecurityConfig
│   │   │       ├── controller/
│   │   │       │   ├── AdminController
│   │   │       │   ├── AuthController
│   │   │       │   └── RoleController
│   │   │       ├── dto/
│   │   │       │   ├── AuthResponse
│   │   │       │   ├── LoginResponse
│   │   │       │   ├── RegisterRequest
│   │   │       │   ├── RoleDto
│   │   │       │   ├── UserProfileDto
│   │   │       │   └── UserStatsDTO
│   │   │       ├── exception/
│   │   │       │   └── GlobalExceptionHandler
│   │   │       ├── mapper/
│   │   │       │   └── UserMapper
│   │   │       ├── model/
│   │   │       │   ├── Role
│   │   │       │   └── User
│   │   │       ├── repository/
│   │   │       │   ├── RoleRepository
│   │   │       │   └── UserRepository
│   │   │       ├── security/
│   │   │       │   ├── CustomUserDetailsService
│   │   │       │   ├── JwtAuthenticationFilter
│   │   │       │   └── Jwtservice
│   │   │       ├── service/
│   │   │       │   ├── AuthService
│   │   │       │   ├── KafkaEventProducer
│   │   │       │   ├── RoleService
│   │   │       │   └── UserService
│   │   │       └── RbacUserManagementApplication
│   │   └── resources/
│   │       ├── application-local.yml
│   │       └── application-docker.yml
│   └── test/...
├── docker-compose.yml
├── Dockerfile
└── README.md
```


---

# ⚙️ Configuration Guide

---


# 🐬 MySQL (Docker) + Kafka (Docker)

### application-docker.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rbacdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: avnish
    password: Avnish@2020
    driver-class-name: com.mysql.cj.jdbc.Driver

kafka:
  bootstrap-servers: localhost:9092
```

### pom.xml
Enable MySQL driver:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.4.0</version>
</dependency>
<!--
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
-->
```


### Run Application
Clone and run Below commands
```bash
docker-compose up
```
or 
```bash
docker-compose up --build
```

### Default Login Credentials

```text
admin@example.com
adminpassword

Role - Admin
```


# 🐳 Docker Setup

🎉 Done!
Your RBAC system with MySQL + Kafka + Docker is now ready to run.