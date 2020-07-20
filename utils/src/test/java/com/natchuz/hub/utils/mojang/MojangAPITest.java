package com.natchuz.hub.utils.mojang;

import kong.unirest.Config;
import kong.unirest.UnirestInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MojangAPITest {

    private UnirestInstance instance;

    @BeforeEach
    public void setup() {
        instance = spy(new UnirestInstance(new Config()));
    }

    @Test
    public void testDefaultEndpoint() {
        ElectroidMojangAPI api = new ElectroidMojangAPI(instance);
        api.getUser("Natchuz");
        verify(instance).get("https://api.ashcon.app/mojang/v2/user/Natchuz");
    }
}