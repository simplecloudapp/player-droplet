FROM gradle:jdk21 AS build

WORKDIR /home/gradle/

COPY --chown=gradle:gradle . .

RUN gradle shadowJar --no-daemon

FROM openjdk:21-jdk-slim

ENV GRPC_PORT=5826
ENV MONGO_CONNECTION_STRING=mongodb://localhost:27017
ENV MONGO_DATABASE=admin
ENV REDIS_HOST=localhost
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=redis
ENV DATABASE_URL=jdbc:postgresql://localhost:5432/postgres

EXPOSE 5826

WORKDIR /app

COPY --from=build /home/gradle/player-server/build/libs/player-server.jar /app/server.jar
ENTRYPOINT ["java", "-jar", "server.jar"]