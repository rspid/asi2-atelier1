FROM openjdk:17
RUN mkdir -p /app/SP
WORKDIR /app/SP
COPY ./target/property-calculation-service-0.0.1-SNAPSHOT.jar property-calculation-service.jar
CMD ["java", "-jar", "property-calculation-service.jar"]