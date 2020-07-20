package com.natchuz.hub.protocol.state;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ServerID {

    @Getter
    private List<String> namespaces;
    @Getter
    private String id;

    public String getFullNamespace() {
        return String.join("/", namespaces);
    }

    public String getFull() {
        return String.join("/", namespaces) + "/" + id;
    }

    public static ServerID fromString(String name) {
        ServerID ret = new ServerID();
        String[] namespaces = name.split("/");
        ret.namespaces = Arrays.asList(Arrays.copyOfRange(namespaces, 0, namespaces.length - 1));
        ret.id = namespaces[namespaces.length - 1];
        return ret;
    }
}
