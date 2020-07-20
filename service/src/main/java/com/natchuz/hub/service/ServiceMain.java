package com.natchuz.hub.service;

public class ServiceMain {
    private static Service service = null;

    public static void main(String[] args) {
        service = new Service();
        service.init();
    }
}
