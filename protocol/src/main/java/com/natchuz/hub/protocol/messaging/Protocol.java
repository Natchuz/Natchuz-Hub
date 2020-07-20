package com.natchuz.hub.protocol.messaging;

import com.google.common.primitives.Shorts;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import lombok.SneakyThrows;
import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;
import java.net.ProtocolException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.natchuz.hub.protocol.arch.Service;
import com.natchuz.hub.protocol.arch.Services;

public class Protocol {

    private final ConnectionFactory factory;
    private Connection connection;

    private final Channel receiverChannel;
    private final Channel publishChannel;

    private final Map<String, MessageHandler> handlers;
    private final Client client;

    private boolean listening = false;
    private String consumer = null;

    @SneakyThrows
    public Protocol(Client client) throws IOException, TimeoutException {
        this.client = client;
        handlers = new HashMap<>();

        factory = new ConnectionFactory();
        factory.setHost("rabbit");

        do {
            try {
                connection = factory.newConnection();
            } catch (Exception e) {
                Thread.sleep(2000);
            }
        } while (connection == null);

        publishChannel = connection.createChannel();

        for (Service s : Services.ALL_SERVICES) {
            s.declareQueues(publishChannel);
        }

        receiverChannel = connection.createChannel();

        client.init(receiverChannel);
    }

    @SneakyThrows
    public void startReceiving() {
        if (!listening) {
            consumer = receiverChannel.basicConsume(client.getQueue(), true, this::delivered, this::canceled);
            System.out.println("[Protocol] Started listening on queue " + client.getQueue());
            listening = true;
        } else {
            throw new ProtocolException("Attempt to start consumer, but it's already running!");
        }
    }

    @SneakyThrows
    public void stopReceiving() {
        if (listening) {
            receiverChannel.basicCancel(consumer);
            System.out.println("[Protocol] stopped listening!");
        } else {
            throw new ProtocolException("[Protocol] Attempt to stop consumer, but not started!");
        }
    }

    @SneakyThrows
    public void send(MessageEndpoint properties, String id, String... args) {
        byte[] message = new byte[]{};

        byte[] bid = id.getBytes(StandardCharsets.UTF_8);

        message = ArrayUtils.addAll(message, Shorts.toByteArray((short) bid.length));
        message = ArrayUtils.addAll(message, bid);

        message = ArrayUtils.addAll(message, Shorts.toByteArray((short) args.length));

        for (String arg : args) {
            byte[] barg = arg.getBytes(StandardCharsets.UTF_8);
            message = ArrayUtils.addAll(message, Shorts.toByteArray((short) barg.length));
            message = ArrayUtils.addAll(message, barg);
        }

        publishChannel.basicPublish(
                properties.getExchange(),
                properties.getRoutingKey(),
                properties.getProperties(),
                message);

        System.out.println("[Protocol] Sent message to exchange \"" +
                properties.getExchange() + "\" with route: \"" + properties.getRoutingKey() + "\": "
                + id + " " + Arrays.toString(args) + " (bin): " + Arrays.toString(message));
    }

    public Client getClient() {
        return client;
    }

    public void disconnect() throws IOException, TimeoutException {
        publishChannel.close();
        receiverChannel.close();
        connection.close();
    }

    public void handle(String id, MessageHandler handler) {
        handlers.put(id, handler);
    }

    private void delivered(String consumerTag, Delivery message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBody());
        buffer.rewind();

        try {
            byte[] idBytes = new byte[buffer.getShort()];
            buffer.get(idBytes);

            String id = new String(idBytes, StandardCharsets.UTF_8);

            String[] args = new String[buffer.getShort()];
            for (int i = 0; i < args.length; i++) {
                byte[] arg = new byte[buffer.getShort()];
                buffer.get(arg);
                args[i] = new String(arg, StandardCharsets.UTF_8);
            }

            System.out.println("[Protocol] Received message: "
                    + id + " " + Arrays.toString(args)
                    + " (bin): "
                    + Arrays.toString(message.getBody()));

            try {
                handlers.get(id).handle(args);
            } catch (Exception e) {
                System.out.println("[Protocol] Exception " + e.getMessage());
            }
        } catch (BufferOverflowException e) {
            System.out.println("[Protocol] Received damaged message, ignoring: " + Arrays.toString(message.getBody()));
        }
    }

    private void canceled(String consumerTag) throws IOException {
    }
}
