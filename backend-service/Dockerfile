# Build stage
FROM maven:3.8.6-amazoncorretto-17 AS build
WORKDIR /app/SP
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17
RUN mkdir -p /app/SP
WORKDIR /app/SP
COPY --from=build /app/SP/target/asi2-backendmarket-monolithic-student-0.0.1-SNAPSHOT.jar backend.jar
EXPOSE 8084
CMD ["java", "-jar", "backend.jar"]