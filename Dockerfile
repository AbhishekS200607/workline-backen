FROM openjdk:17-jdk-slim

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

EXPOSE 10000

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "target/workline-backend-0.0.1-SNAPSHOT.jar"]