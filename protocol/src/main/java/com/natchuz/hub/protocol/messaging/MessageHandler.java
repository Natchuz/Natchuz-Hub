package com.natchuz.hub.protocol.messaging;

public interface MessageHandler {
    void handle(String[] args) throws Exception;
}
