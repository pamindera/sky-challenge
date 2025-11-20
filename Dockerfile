# ---- Build stage ----
FROM gradle:8.14-jdk21 AS build

WORKDIR /workspace

COPY gradlew ./
COPY gradle/ ./gradle/
COPY build.gradle.kts settings.gradle.kts gradle.properties* ./

RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew quarkusBuild --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre

WORKDIR /deployments

COPY --from=build /workspace/build/quarkus-app/lib/ ./lib/
COPY --from=build /workspace/build/quarkus-app/*.jar ./
COPY --from=build /workspace/build/quarkus-app/app/ ./app/
COPY --from=build /workspace/build/quarkus-app/quarkus/ ./quarkus/

EXPOSE 8080
USER 1001

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]