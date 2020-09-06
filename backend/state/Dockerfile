FROM openjdk:8-jre-alpine

WORKDIR /data

COPY "build/libs/state-all.jar" app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "-port=80"]