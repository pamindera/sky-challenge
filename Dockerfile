# ---- Build stage ----
FROM gradle:8.14-jdk21 AS build

WORKDIR /workspace

COPY gradlew ./
COPY gradle/ ./gradle/
COPY build.gradle.kts settings.gradle.kts gradle.properties* ./

RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew bootJar --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre

WORKDIR /deployments

COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080
USER 1001

ENTRYPOINT ["sh", "-c", "java -jar /deployments/app.jar"]