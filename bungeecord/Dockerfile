FROM openjdk:8-jre-alpine

WORKDIR /data

RUN apk add --no-cache curl coreutils

# download latest bungeecord
RUN curl "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar" -o bungeecord.jar

# copy config files
COPY config.yml modules.yml locations.yml ./

# copy compiled plugin
COPY build/libs/bungeecord-all.jar plugins/

ENTRYPOINT ["java", "-jar", "bungeecord.jar"]