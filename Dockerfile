FROM gradle:jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:8-jre-slim

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

COPY ./entrypoint.sh /
RUN chmod 755 /entrypoint.sh

ENV SPRING_MAIN_BANNER-MODE=off

ENTRYPOINT ["/entrypoint.sh"]
