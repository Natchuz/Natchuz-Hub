package com.natchuz.hub.protocol.messaging;

import com.rabbitmq.client.Channel;

import java.io.IOException;

public class SimpleClient implements Client {

    private final String queueName;

    public SimpleClient(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void init(Channel channel) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
    }

    @Override
    public String getQueue() {
        return queueName;
    }
}
