FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17 AS base

COPY --from=build /app/target/final.thesis-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar", \
     "--spring.data.mongodb.uri=${DATABASE_URI}", \
     "--email.password=${EMAIL_PASSWORD}", \
     "--email.username=${EMAIL_USERNAME}", \
     "--jwt.secret.key=${JWT_SECRET_KEY}"]
