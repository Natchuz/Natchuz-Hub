package com.natchuz.hub.protocol.arch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Services {

    public static final BungeecordService BUNGEECORD = new BungeecordService();
    public static final ServiceService SERVICE = new ServiceService();
    public static final PaperService PAPER = new PaperService();

    public static final List<Service> ALL_SERVICES = new ArrayList<>();

    // use reflection to load all services into list
    static {
        Class<Services> clazz = Services.class;
        Field[] arr = clazz.getFields();
        for (Field f : arr) {
            if (f.getType().equals(Service.class)) {
                try {
                    ALL_SERVICES.add((Service) f.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
