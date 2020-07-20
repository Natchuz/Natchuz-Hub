package com.natchuz.hub.protocol.messaging;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;

import java.io.IOException;

public class ExchangedClient implements Client {

    private Channel channel;
    private String queueName;
    private final String exchangeName;

    public ExchangedClient(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    @Override
    public void init(Channel channel) throws IOException {
        this.channel = channel;
        channel.exchangeDeclare(exchangeName, "topic");
        this.queueName = channel.queueDeclare().getQueue();
    }

    @Override
    public String getQueue() {
        return queueName;
    }

    @SneakyThrows
    public void subscribe(String... topics) {
        for (String topic : topics) {
            channel.queueBind(queueName, exchangeName, topic);
        }
    }

    @SneakyThrows
    public void unsubscribe(String... topics) {
        for (String topic : topics) {
            channel.queueUnbind(queueName, exchangeName, topic);
        }
    }
}
