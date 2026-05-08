FROM maven:3.9.10-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

RUN apk update && apk upgrade --no-cache && rm -rf /var/cache/apk/*

WORKDIR /app

RUN addgroup -S expensetrackerapi && adduser -S expensetrackerapi -G expensetrackerapi

COPY --from=build /app/target/*.jar app.jar

RUN chown -R expensetrackerapi:expensetrackerapi /app

USER expensetrackerapi

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]