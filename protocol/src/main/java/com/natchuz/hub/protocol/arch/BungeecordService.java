package com.natchuz.hub.protocol.arch;

import com.rabbitmq.client.Channel;

import java.io.IOException;

import com.natchuz.hub.protocol.messaging.Client;
import com.natchuz.hub.protocol.messaging.MessageEndpoint;
import com.natchuz.hub.protocol.messaging.SimpleClient;

public class BungeecordService extends Service {

    private final MessageEndpoint properties;

    public BungeecordService() {
        this.properties = new MessageEndpoint("", "BUNGEECORD", null);
    }

    public MessageEndpoint messageEndpoint() {
        return properties;
    }

    @Override
    public void declareQueues(Channel channel) throws IOException {
        channel.queueDeclare("BUNGEECORD", false, false, false, null);
    }

    @Override
    public Client createClient() throws IOException {
        return new SimpleClient("BUNGEECORD");
    }
}
