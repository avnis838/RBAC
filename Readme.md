# 📌 RBAC Project (Spring Boot + MySQL/PostgreSQL + Kafka + Docker)

A **Role-Based Access Control (RBAC)** microservice implemented using **Java 21** and **Spring Boot**, with support for:

| Setup Option | Database | Kafka |
|--------------|----------|-------|
| 1 | PostgreSQL (Local) | Docker |
| 2 | MySQL (Docker) | Docker |

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
└── README.md
```


---

# ⚙️ Configuration Guide

---

# 🐘 1. PostgreSQL (Local) + Kafka (Docker)

### application-local.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/rbacdb
    username: your_pg_user
    password: your_pg_password
    driver-class-name: org.postgresql.Driver

kafka:
  bootstrap-servers: localhost:9092
 
```
### pom.xml
Enable PostgreSQL driver:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>

<!--
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
-->
```
---


# 🐬 2. MySQL (Docker) + Kafka (Docker)

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
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<!--
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
-->
```

# 🐳 Docker Setup

### MySQL Container
```bash
docker run -d --name mysql-rbac \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=rbacdb \
  -e MYSQL_USER=avnish \
  -e MYSQL_PASSWORD=Avnish@2020 \
  -p 3306:3306 mysql:8
```

### Zookeeper
```bash
docker run -d --name=zookeeper \
  -p 2181:2181 \
  -e ZOOKEEPER_CLIENT_PORT=2181 \
  confluentinc/cp-zookeeper:latest
```


### Kafka
```bash
docker run -d --name=kafka \
  -p 9092:9092 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  --link zookeeper:zookeeper \
  confluentinc/cp-kafka:5.5.3
```

### Start Existing Containers
```bash
docker start mysql-rbac
docker start zookeeper
docker start kafka
```

### ▶ Run Spring Boot Application
```bash
mvn spring-boot:run
```


OR build first:

```bash
mvn clean package
java -jar target/rbac-*.jar
```
### 🛢 Access MySQL (Inside Docker)
Enter Container
```bash
docker exec -it mysql-rbac mysql -u avnish -p
```
```css
Avnish@2020
```
Select Database
```sql
USE rbacdb;
```

```sql
List Tables

SHOW TABLES;
```

### 🧪 Optional: Create Kafka Topic
```bash
kafka-topics --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 1 \
  --topic your_topic
```

### 🧾 Best Practices
Use Spring profiles (application-docker.yml, application-local.yml)

Use Flyway/Liquibase for schema management

Add initial roles,user using Datainitializer file

Implemented JWT-based security for protecting endpoints

🎉 Done!
Your RBAC system with PostgreSQL / MySQL + Kafka + Docker is now ready to run.