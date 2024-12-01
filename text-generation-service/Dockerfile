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
COPY --from=build /app/SP/target/text-generation-service-0.0.1-SNAPSHOT.jar text-generation-service.jar
CMD ["java", "-jar", "text-generation-service.jar"]
