package com.natchuz.hub.service;

import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;

import java.util.HashMap;

import com.natchuz.hub.protocol.arch.Services;
import com.natchuz.hub.protocol.messaging.Protocol;
import com.natchuz.hub.service.docker.CmdLocalProvider;
import com.natchuz.hub.service.docker.DockerClient;

public class Service {

    private DockerClient dockerClient;
    private Protocol protocol;
    private Jedis redis;

    @SneakyThrows
    public void init() {
        dockerClient = new CmdLocalProvider();
        redis = new Jedis("redis");
        protocol = new Protocol(Services.SERVICE.createClient());

        protocol.handle("scan", this::scan);
    }

    public void createServer(String image) {
        long id = redis.incr("servers." + image + ".inc");
        String container = dockerClient.createContainer(
                image,
                "natchuzhub_" + image + "_" + id,
                new HashMap() {{
                    put("SERVERID", Long.toString(id));
                }}, new HashMap());
        dockerClient.runContainer(container);
    }

    public void scan(String[] args) {
        long freeServers = redis.zcount("servers." + args[0], 0, 30);
        if (freeServers == 0) {
            createServer(args[0]);
        }
    }
}
