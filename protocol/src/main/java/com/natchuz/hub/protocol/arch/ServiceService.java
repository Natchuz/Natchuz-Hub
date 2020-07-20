package com.natchuz.hub.protocol.arch;

import com.rabbitmq.client.Channel;

import java.io.IOException;

import com.natchuz.hub.protocol.messaging.Client;
import com.natchuz.hub.protocol.messaging.MessageEndpoint;
import com.natchuz.hub.protocol.messaging.SimpleClient;

public class ServiceService extends Service {

    private final MessageEndpoint messageEndpoint;

    public ServiceService() {
        messageEndpoint = new MessageEndpoint("", "SERVICE", null);
    }

    @Override
    public void declareQueues(Channel channel) throws IOException {
        channel.queueDeclare("SERVICE", false, false, false, null);
    }

    @Override
    public Client createClient() throws IOException {
        return new SimpleClient("SERVICE");
    }
}
