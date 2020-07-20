package com.natchuz.hub.protocol.messaging;

import com.rabbitmq.client.Channel;

import java.io.IOException;

public interface Client {
    void init(Channel channel) throws IOException;

    String getQueue();
}
