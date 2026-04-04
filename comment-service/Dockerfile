FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY comment-service ./comment-service

RUN mvn -pl comment-service -am clean package -DskipTests


FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /app/comment-service/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]