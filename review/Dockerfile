# Dockerfile for Review Service
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/review-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*