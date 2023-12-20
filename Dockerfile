FROM gradle:jdk17 as builder
WORKDIR /home/gradle/project/
COPY build.gradle settings.gradle ./
COPY src/ src/
RUN gradle --no-daemon bootJar

FROM amazoncorretto:17-alpine as runner
WORKDIR /botasana
COPY --from=builder /home/gradle/project/build/libs/resume-chatbot-0.0.1-SNAPSHOT.jar botasana.jar
COPY .env.docker .env
ENTRYPOINT ["java"]
CMD ["-Dspring.config.import=optional:file:.env[.properties]", "-jar", "botasana.jar"]
EXPOSE 8080

# docker run -e SECRET_OPENAI_API_KEY=<openai-api-key> ...
# docker run --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle gradle <gradle-task>
