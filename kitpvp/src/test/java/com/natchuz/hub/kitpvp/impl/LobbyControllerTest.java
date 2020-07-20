package com.natchuz.hub.kitpvp.impl;

import com.google.common.collect.Sets;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @see LobbyController
 */
public class LobbyControllerTest {

    @Mock
    private Lobby mockLobby;
    @Mock
    private LivingEntity hubNPC;
    @Mock
    private ArmorStand testStand;

    private LobbyController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        controller = new LobbyController(mockLobby);

        when(hubNPC.getUniqueId()).thenReturn(UUID.randomUUID());
        when(testStand.getUniqueId()).thenReturn(UUID.randomUUID());
        when(mockLobby.getArmorStands()).thenReturn(Sets.newHashSet(testStand));
        when(mockLobby.getHubNpc()).thenReturn(hubNPC);
    }

    //region EntityDamageEvent

    /**
     * Test if armor stands cannot be damaged
     */
    @Test
    public void testEntityDamageArmorStands() {
        EntityDamageEvent entityDamageEvent = new EntityDamageEvent(testStand,
                EntityDamageEvent.DamageCause.CUSTOM, 10);
        controller.onEntityDamage(entityDamageEvent);
        assertTrue(entityDamageEvent.isCancelled());
    }

    /**
     * Test if return npc cannot be damaged
     */
    @Test
    public void testEntityDamageHubNpc() {
        EntityDamageEvent entityDamageEvent = new EntityDamageEvent(hubNPC,
                EntityDamageEvent.DamageCause.CONTACT, 10);
        controller.onEntityDamage(entityDamageEvent);
        assertTrue(entityDamageEvent.isCancelled());
    }

    /**
     * Test if other npc-s can be damaged
     */
    @Test
    public void testEntityDamageOtherEntity() {
        Entity otherEntity = mock(Entity.class);
        when(otherEntity.getUniqueId()).thenReturn(UUID.randomUUID());
        EntityDamageEvent entityDamageEvent = new EntityDamageEvent(otherEntity,
                EntityDamageEvent.DamageCause.CONTACT, 10);
        controller.onEntityDamage(entityDamageEvent);
        assertFalse(entityDamageEvent.isCancelled());
    }

    //endregion
    //region EntityCombustEvent

    /**
     * Test if return npc does not combust
     */
    @Test
    public void testEntityCombustHubNpc() {
        EntityCombustEvent entityCombustEvent = new EntityCombustEvent(hubNPC, 10);
        controller.onEntityBurn(entityCombustEvent);
        assertTrue(entityCombustEvent.isCancelled());
    }

    /**
     * Test if other entities combust
     */
    @Test
    public void testEntityCombustOtherEntity() {
        Entity otherEntity = mock(Entity.class);
        when(otherEntity.getUniqueId()).thenReturn(UUID.randomUUID());

        EntityCombustEvent entityCombustEvent = new EntityCombustEvent(otherEntity, 10);
        controller.onEntityBurn(entityCombustEvent);
        assertFalse(entityCombustEvent.isCancelled());
    }

    //endregion

}
