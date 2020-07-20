package com.natchuz.hub.kitpvp.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @see WorldController
 */
public class WorldControllerTest {

    private WorldController handler;

    @BeforeEach
    public void setup() {
        handler = new WorldController();
    }

    //region Spawn Event

    /**
     * Whenever spawn event is not canceled
     */
    @Test
    public void testSpawnEventValid() {
        CreatureSpawnEvent event = new CreatureSpawnEvent(mock(LivingEntity.class),
                CreatureSpawnEvent.SpawnReason.CUSTOM);
        handler.onSpawn(event);
        assertFalse(event.isCancelled());
    }

    /**
     * Whenever spawn event is not canceled
     */
    @Test
    public void testSpawnEventInvalid() {
        CreatureSpawnEvent event = new CreatureSpawnEvent(mock(LivingEntity.class),
                CreatureSpawnEvent.SpawnReason.EXPLOSION);
        handler.onSpawn(event);
        assertTrue(event.isCancelled());

        event = new CreatureSpawnEvent(mock(LivingEntity.class),
                CreatureSpawnEvent.SpawnReason.DEFAULT);
        handler.onSpawn(event);
        assertTrue(event.isCancelled());

        event = new CreatureSpawnEvent(mock(LivingEntity.class),
                CreatureSpawnEvent.SpawnReason.BUILD_WITHER);
        handler.onSpawn(event);
        assertTrue(event.isCancelled());
    }

    //endregion
}
