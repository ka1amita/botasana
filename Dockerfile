# syntax=docker/dockerfile:1

FROM gradle:jdk17 AS cache
WORKDIR /home/gradle/project/
ENV GRADLE_USER_HOME /home/gradle/.gradle
COPY build.gradle settings.gradle ./

FROM cache AS build
COPY config/ config/
COPY src/ src/
COPY .env.test .env
RUN gradle --no-daemon --info --stacktrace build

FROM amazoncorretto:17-alpine AS pub
WORKDIR /botasana
COPY --from=build /home/gradle/project/build/libs/botasana-0.0.1-SNAPSHOT.jar botasana.jar
COPY .env.example .env
ENTRYPOINT ["java", "-jar", "botasana.jar"]
EXPOSE 8080

FROM amazoncorretto:17-alpine AS compose
WORKDIR /botasana
COPY --from=build /home/gradle/project/build/libs/botasana-0.0.1-SNAPSHOT.jar botasana.jar
COPY .env.compose .env
ENTRYPOINT ["java", "-jar", "botasana.jar", "--spring.profiles.active=compose"]
EXPOSE 8080
