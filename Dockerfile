FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apk add --no-cache maven
RUN mvn clean package -DskipTests

EXPOSE 10000

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "target/workline-backend-0.0.1-SNAPSHOT.jar"]