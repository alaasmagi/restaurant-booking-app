FROM eclipse-temurin:25-jdk AS build

WORKDIR /workspace

COPY gradlew build.gradle settings.gradle ./
COPY gradle gradle
COPY src src

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
