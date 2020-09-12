FROM openjdk:8-jre-alpine

WORKDIR /data

COPY "build/libs/users-all.jar" app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "-port=80"]