# user-service/Dockerfile

FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
COPY event-lib ./event-lib
COPY user-service ./user-service

RUN mvn -pl user-service -am clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/user-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]