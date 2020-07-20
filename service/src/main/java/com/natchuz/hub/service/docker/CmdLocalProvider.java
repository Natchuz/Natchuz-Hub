package com.natchuz.hub.service.docker;

import lombok.SneakyThrows;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is disgusting
 * <p>
 * Java does not support unix sockets so fuck you
 */
public class CmdLocalProvider implements DockerClient {

    @SneakyThrows
    private String execCmd(String cmd) {
        cmd = "docker " + cmd;
        System.out.println("[DOCKER] " + cmd);
        java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    public String createContainer(String image, String name, Map<String, String> params, Map<String, String> labels) {
        return execCmd("create --rm --name " + name + " " +
                params.entrySet().stream().map(e -> "-e " + e.getKey() + "=" + e.getValue()).collect(Collectors.joining(" ")) +
                " " +
                labels.entrySet().stream().map(e -> "-l " + e.getKey() + "=" + e.getValue()).collect(Collectors.joining(" ")) +
                " " + image);
    }

    @Override
    public void runContainer(String id) {
        execCmd("start " + id);
    }

    @Override
    public void stopContainer(String id, int timeout) {
        execCmd("stop -t " + timeout + " " + id);
    }

    @Override
    public void killContainer(String id) {
        execCmd("kill " + id);
    }

    @Override
    public void removeContainer(String id) {
        execCmd("rm " + id);
    }

    @Override
    public void joinContainerToNetwork(String container, String network) {
        execCmd("network connect " + network + " " + container);
    }
}
