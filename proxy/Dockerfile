FROM openjdk:8-jre-alpine

RUN apk add --no-cache curl coreutils

# some tools to help debugging
WORKDIR /tools
RUN curl -L "https://github.com/itzg/mc-monitor/releases/download/0.6.0/mc-monitor_0.6.0_linux_amd64.tar.gz" | tar -xz

# actually bungeecord
WORKDIR /app
RUN curl "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar" -o bungeecord.jar

# copy config files
COPY config.yml modules.yml locations.yml ./

# copy compiled plugin
COPY build/libs/proxy-all.jar plugins/proxy-plugin.jar

ENTRYPOINT ["java", "-jar", "bungeecord.jar"]