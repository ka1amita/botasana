FROM gradle:jdk17 AS cache
WORKDIR /home/gradle/project/
ENV GRADLE_USER_HOME /home/gradle/.gradle
COPY build.gradle settings.gradle ./
RUN gradle --no-daemon --info --build-cache --configuration-cache --stacktrace dependencies

FROM cache AS build
COPY config/ config/
COPY src/ src/
COPY .env.test .env
RUN gradle --no-daemon --info --build-cache --configuration-cache --stacktrace build

FROM amazoncorretto:17-alpine AS pub
WORKDIR /botasana
COPY --from=build /home/gradle/project/build/libs/botasana-0.0.1-SNAPSHOT.jar botasana.jar
COPY .env.example .env
ENTRYPOINT ["java", "-jar", "botasana.jar"]
EXPOSE 8080
