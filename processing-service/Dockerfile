FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -y ffmpeg && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/processing-service-1.0.0.jar app.jar
EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]



