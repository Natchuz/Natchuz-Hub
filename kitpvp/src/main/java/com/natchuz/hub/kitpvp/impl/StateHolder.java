package com.natchuz.hub.kitpvp.impl;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * State handler holds {@link PlayerState player states}
 */
public class StateHolder {

    private final HashMap<Player, PlayerState> states;

    public StateHolder() {
        this.states = new HashMap<>();
    }

    /**
     * Creates new state
     */
    public void initNewState(Player player, PlayerState state) {
        Validate.notNull(player, "Player cannot be null");
        Validate.notNull(state, "State cannot be null");
        states.put(player, state);
    }

    /**
     * Returns state for player, null if not found
     */
    public PlayerState getState(Player player) {
        return states.get(player);
    }

    /**
     * Removes player from holder
     *
     * @apiNote dangerous method, only use when you are sure, player no longer exist
     */
    public void removeState(Player player) {
        states.remove(player);
    }
}
