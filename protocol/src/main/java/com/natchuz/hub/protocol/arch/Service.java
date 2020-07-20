package com.natchuz.hub.protocol.arch;

import com.rabbitmq.client.Channel;

import java.io.IOException;

import com.natchuz.hub.protocol.messaging.Client;

public abstract class Service {

    public abstract void declareQueues(Channel channel) throws IOException;

    public abstract Client createClient() throws IOException;
}
