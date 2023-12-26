# syntax=docker/dockerfile:1
FROM gradle:jdk17 AS build
RUN git clone https://github.com/ka1amita/botasana
WORKDIR /home/gradle/botasana
RUN gradle --no-daemon bootJar

FROM amazoncorretto:17-alpine AS runtime
WORKDIR /app
COPY --from=build /home/gradle/botasana/build/libs/botasana-0.0.1-SNAPSHOT.jar botasana.jar
COPY --from=build /home/gradle/botasana/.env.example .env
ENTRYPOINT ["java", "-jar", "botasana.jar"]
EXPOSE 8080
