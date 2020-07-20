package com.natchuz.hub.kitpvp.impl;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * @see StateHolder
 */
public class StateHolderTest {

    private StateHolder holder;

    @BeforeEach
    public void setup() {
        holder = spy(new StateHolder());
    }

    /**
     * Test valid addition and removal
     */
    @Test
    public void testValidState() {
        Player target = mock(Player.class, "valid-player");
        PlayerState state = mock(PlayerState.class);
        holder.initNewState(target, state);

        assertEquals(holder.getState(target), state);
    }

    /**
     * Test invalid arguments
     */
    @Test
    public void testInitNulls() {
        assertThrows(IllegalArgumentException.class, () -> holder.initNewState(null, null));
    }

    /**
     * Test for returning player that does not exist
     */
    @Test
    public void testInvalidPlayer() {
        Player target = mock(Player.class, "valid-player");
        PlayerState state = mock(PlayerState.class);
        holder.initNewState(target, state);

        Player invalidPlayer = mock(Player.class, "invalid-player");

        assertNull(holder.getState(invalidPlayer));
    }

}
