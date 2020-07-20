package com.natchuz.hub.protocol.messaging;

import com.rabbitmq.client.AMQP;

public class MessageEndpoint {

    private final String exchange;
    private final String routingKey;
    private final AMQP.BasicProperties properties;

    public MessageEndpoint(String exchange, String routingKey, AMQP.BasicProperties properties) {
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.properties = properties;
    }

    public String getExchange() {
        return exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public AMQP.BasicProperties getProperties() {
        return properties;
    }
}
