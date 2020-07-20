package com.natchuz.hub.protocol.arch;

import com.rabbitmq.client.Channel;

import java.io.IOException;

import com.natchuz.hub.protocol.messaging.Client;
import com.natchuz.hub.protocol.messaging.ExchangedClient;
import com.natchuz.hub.protocol.messaging.MessageEndpoint;

public class PaperService extends Service {

    public MessageEndpoint getMessageEndpoint(String topic) {
        return new MessageEndpoint("PAPER", topic, null);
    }

    @Override
    public void declareQueues(Channel channel) throws IOException {
        channel.exchangeDeclare("PAPER", "topic");
    }

    @Override
    public Client createClient() throws IOException {
        return new ExchangedClient("PAPER");
    }
}
