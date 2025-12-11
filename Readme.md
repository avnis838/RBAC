First way -
For database, In application.yml,
need to set the datasource -
spring:
datasource:
url: jdbc:mysql://localhost:3306/rbacdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
username: avnish
password: Avnish@2020
driver-class-name: com.mysql.cj.jdbc.Driver

In pom.xml,
Uncomment postgres connector, and comment mysql connector

If Kafka is there make sure running kafka should have the url as
localhost:9092 or change below in application.yml
kafka:
bootstrap-servers: localhost:9092


Database - postgres local
Kafka - docker image
Clone the Project, Java 21 is used



Second way - 
Note - pom.xml should have mysql driver as uncommented
application.yml have the updated datasource informations.
as above done

Database - mysql docker image
Kafka - docker image
clone the project 

Run docker mysql
Command -
docker run -d --name mysql-rbac -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=rbacdb -e MYSQL_USER=avnish -e MYSQL_PASSWORD=Avnish@2020 -p 3306:3306 mysql:8

Run kafka image
Command - to start kafka using zookeeper
docker run -d --name=zookeeper -p 2181:2181 -e ZOOKEEPER_CLIENT_PORT=2181 confluentinc/cp-zookeeper:latest

docker run -d --name=kafka -p 9092:9092 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 --link zookeeper:zookeeper confluentinc/cp-kafka:5.5.3

if already have do start the image with command: docker start <container-name>

Now run mvn spring-boot:run

Now we can check Database image:
Step 1 :
docker exec -it mysql-rbac mysql -u avnish -p

Step 2 :
Enter password - Avnish@2020

Step 3 :
Now we are in Database, use database - rbacdb as used in the project
use rbacdb;

Step 4 :
now do operations 
show tables;
