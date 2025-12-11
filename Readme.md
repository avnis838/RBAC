# RBAC Project Setup Guide (MySQL / PostgreSQL + Kafka + Docker)

This guide explains how to run the RBAC project using either:

- PostgreSQL (Local) + Kafka (Docker)
- MySQL (Docker) + Kafka (Docker)

Java Version → **Java 21**

---

# 🚀 First Way – PostgreSQL (Local) + Kafka (Docker)

## 1. application.yml (PostgreSQL Configuration)

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

## 2. pom.xml Adjustments
- ✔ Uncomment **PostgreSQL connector**
- ❌ Comment **MySQL connector**

---

# 🚀 Second Way – MySQL (Docker) + Kafka (Docker)

## 1. pom.xml
- ✔ MySQL driver **uncommented**
- ❌ PostgreSQL driver **commented**

## 2. application.yml (MySQL Configuration)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rbacdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: avnish
    password: Avnish@2020
    driver-class-name: com.mysql.cj.jdbc.Driver
```

---

# 🐳 Docker Commands

## MySQL Container

```bash
docker run -d --name mysql-rbac \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=rbacdb \
  -e MYSQL_USER=avnish \
  -e MYSQL_PASSWORD=Avnish@2020 \
  -p 3306:3306 mysql:8
```

---

## Zookeeper

```bash
docker run -d --name=zookeeper \
  -p 2181:2181 \
  -e ZOOKEEPER_CLIENT_PORT=2181 \
  confluentinc/cp-zookeeper:latest
```

---

## Kafka

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

---

## Start existing containers

```bash
docker start mysql-rbac
docker start zookeeper
docker start kafka
```

---

# ▶ Run Spring Boot Application

```bash
mvn spring-boot:run
```

---

# 🛢 Access MySQL in Docker

### Step 1 — Enter MySQL

```bash
docker exec -it mysql-rbac mysql -u avnish -p
```

### Step 2 — Enter password

```
Avnish@2020
```

### Step 3 — Select database

```sql
USE rbacdb;
```

### Step 4 — Show tables

```sql
SHOW TABLES;
```

---

# 🎉 Done!

You can now work with this project using PostgreSQL or MySQL + Kafka + Docker.
