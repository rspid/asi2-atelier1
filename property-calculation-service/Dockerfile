# Build stage
FROM maven:3.8.6-amazoncorretto-17 AS build
WORKDIR /app/SP
COPY pom.xml .
COPY src ./src
COPY libs ./libs
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17
RUN mkdir -p /app/SP
WORKDIR /app/SP
COPY --from=build /app/SP/target/property-calculation-service-0.0.1-SNAPSHOT.jar property-calculation-service.jar
CMD ["java", "-jar", "property-calculation-service.jar"]
