package com.natchuz.hub.kitpvp.impl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * @see StateHolderController
 */
public class StateHolderControllerTest {

    private PlayerState mockState;
    private StateHolder holder;
    private StateHolderController controller;
    private Player reason;

    @BeforeEach
    public void setup() {
        mockState = mock(PlayerState.class);
        reason = mock(Player.class);

        holder = spy(new StateHolder());
        controller = new StateHolderController(holder);

        holder.initNewState(reason, mockState);
    }


    /**
     * Whenever state is removed when player leaves server
     */
    @Test
    public void testPlayerQuitEvent() {
        PlayerQuitEvent event = new PlayerQuitEvent(reason, "");
        controller.onPlayerLeftEvent(event);

        verify(holder).removeState(eq(reason));
    }

    /**
     * Checks if player interact entity event is called properly
     */
    @Test
    public void testPlayerInteractEntityEvent() {
        PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(reason, null);

        controller.onEntityInteract(event);

        verify(holder).getState(eq(reason));
        verify(mockState).onPlayerInteractEntity(eq(event));
    }

    /**
     * Checks if player interact event is called properly
     */
    @Test
    public void testPlayerInteractEvent() {
        PlayerInteractEvent event = new PlayerInteractEvent(reason, null, null, null, null, null);

        controller.onInteract(event);

        verify(holder).getState(eq(reason));
        verify(mockState).onPlayerInteract(eq(event));
    }

    /**
     * Checks if player move event is called properly
     */
    @Test
    public void testPlayerMoveEvent() {
        PlayerMoveEvent event = new PlayerMoveEvent(reason, null, null);

        controller.onPlayerMove(event);

        verify(holder).getState(eq(reason));
        verify(mockState).onMove(eq(event));
    }

    /**
     * Checks if entity damage event is called properly
     */
    @Test
    public void testEntityDamageEvent() {
        EntityDamageEvent event = mock(EntityDamageEvent.class);
        when(event.getEntity()).thenReturn(reason);

        controller.onDamage(event);

        verify(holder).getState(eq(reason));
        verify(mockState).onEntityDamageEvent(eq(event));
    }

    /**
     * Checks if entity damage event isn't called when entity is not a player
     */
    @Test
    public void testEntityDamageEventNonPlayer() {
        EntityDamageEvent event = mock(EntityDamageEvent.class);
        when(event.getEntity()).thenReturn(mock(Entity.class));

        controller.onDamage(event);

        verify(holder, never()).getState(eq(reason));
        verify(mockState, never()).onEntityDamageEvent(eq(event));
    }

    /**
     * Checks if entity damage by entity event is called properly
     */
    @Test
    public void testEntityDamageByEntityEvent() {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(reason, mock(Entity.class),
                null, 20);
        controller.onEntityDamageEvent(event);

        verify(holder).getState(eq(reason));
        verify(mockState).onEntityDamageByEntityEvent(eq(event));
    }

    /**
     * Checks if entity damage by entity event isn't called when entity is not a player
     */
    @Test
    public void testEntityDamageByEntityEventNonPlayer() {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(mock(Entity.class), mock(Entity.class),
                null, 20);
        controller.onEntityDamageEvent(event);

        verify(holder, never()).getState(eq(reason));
        verify(mockState, never()).onEntityDamageByEntityEvent(eq(event));
    }

    @Test
    public void testPlayerInteractAtEntity() {
        PlayerInteractAtEntityEvent event = new PlayerInteractAtEntityEvent(reason, mock(LivingEntity.class), mock(Vector.class));

        controller.onPlayerInteractAtEntityEvent(event);

        verify(holder).getState(eq(reason));
        verify(mockState).onPlayerInteractAtEntityEvent(eq(event));
    }
}
