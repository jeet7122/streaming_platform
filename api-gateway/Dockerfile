# --------------- Build Stage -------------------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY api-gateway ./api-gateway

RUN mvn -pl api-gateway -am clean package -DskipTests

# ------------- Run  Time --------------
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /app/api-gateway/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]