FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .

RUN ./gradlew bootJar

CMD ["java", "-jar", "build/libs/accountOrchestrator-0.0.1-SNAPSHOT.jar"]