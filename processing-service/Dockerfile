FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY event-lib ./event-lib
COPY processing-service ./processing-service

RUN mvn -pl processing-service -am clean package -DskipTests

FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -y ffmpeg && rm -rf /var/lib/apt.lists/*

WORKDIR /app

COPY --from=builder /app/processing-service/target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]



