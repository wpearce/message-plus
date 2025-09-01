# -------- build stage --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Gradle wrapper & metadata first to leverage layer caching
COPY gradlew ./
COPY gradle ./gradle
RUN chmod +x gradlew

# Copy build files (Kotlin or Groovy DSL) and optional props
COPY settings.gradle* build.gradle* gradle.properties* ./

# Now copy sources
COPY src ./src

# Build a runnable Boot jar (skip tests for faster CI)
RUN ./gradlew --no-daemon clean bootJar -x test

# -------- runtime stage --------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the Boot jar (there should be exactly one *.jar in build/libs)
COPY --from=build /app/build/libs/*.jar app.jar

# Good container defaults for the JVM
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
