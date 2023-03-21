FROM gradle:8.0.0-jdk17 AS builder

WORKDIR /app

# Only copy dependency-related files
COPY build.gradle.kts settings.gradle.kts /app/

# download & cache dependencies
RUN --mount=type=cache,target=/home/gradle/.gradle/caches gradle build --no-daemon --stacktrace -x bootJar -x test || true

# Copy actual source code
COPY ./src /app/src/
# build
RUN --mount=type=cache,target=/home/gradle/.gradle/caches gradle clean build --no-daemon -x test

FROM amazoncorretto:17.0.6-al2 AS final

ARG PROFILE=local

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "/app/app.jar"]
