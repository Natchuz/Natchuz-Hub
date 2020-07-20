package com.natchuz.hub.protocol.state;

import lombok.Value;

@Value
public class Server {
    ServerID id;
    int players;
}
