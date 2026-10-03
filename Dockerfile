FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-Xmx256m", "-Xss512k", "-XX:MaxMetaspaceSize=128m", "-XX:ReservedCodeCacheSize=64m", "-XX:+UseSerialGC", "-XX:TieredStopAtLevel=1", "-jar", "app.jar"]
