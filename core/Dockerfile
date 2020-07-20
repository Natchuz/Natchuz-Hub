FROM openjdk:8-jre-alpine

WORKDIR /data

RUN apk add --no-cache curl coreutils

# download paper and other plugins
RUN curl "https://papermc.io/api/v1/paper/1.14.4/latest/download" -o paper.jar && \
    mkdir plugins && \
    curl "https://media.forgecdn.net/files/2760/373/worldedit-bukkit-7.0.1.jar" -o plugins/WorldEdit.jar && \
    curl -L "https://github.com/dmulloy2/ProtocolLib/releases/download/4.5.1/ProtocolLib.jar" -o plugins/ProtocolLib.jar

# add config files
COPY common network ./

# add compiled network plugin
COPY build/libs/core-all.jar plugins/core.jar

ENTRYPOINT ["java", "-Dserver.context=network", "-jar", "paper.jar"]