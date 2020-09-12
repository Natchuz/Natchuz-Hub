FROM openjdk:8-jre-alpine

WORKDIR /data

RUN apk add --no-cache curl coreutils

# download spigot and other plugins
RUN mkdir mods && \
    curl -L "https://repo.spongepowered.org/maven/org/spongepowered/spongevanilla/1.12.2-7.2.3/spongevanilla-1.12.2-7.2.3.jar" -o sponge.jar && \
    curl -L "https://github.com/CrushedPixel/PacketGate/releases/download/0.1.1/PacketGate-0.1.1.jar" -o mods/packetgate.jar

# add config files
COPY common network ./

# add compiled core plugin
COPY ["build/libs/core-all.jar", "build/libs/sponge-all.jar", "mods/"]

ENTRYPOINT ["java", "-Dserver.context=network", "-jar", "sponge.jar"]