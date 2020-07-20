FROM openjdk:8-jre-alpine

WORKDIR /app

RUN apk add --no-cache docker

COPY build/libs/service-all.jar .

ENTRYPOINT ["java", "-jar", "./service-all.jar"]