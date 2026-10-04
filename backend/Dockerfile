FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -B -ntp package

FROM eclipse-temurin:21-jre
WORKDIR /app
ENV PORT=10000
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=65.0"
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar ./app.jar
USER 10001
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "app.jar"]
