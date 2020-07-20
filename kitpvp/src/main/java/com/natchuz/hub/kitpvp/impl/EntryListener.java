package com.natchuz.hub.kitpvp.impl;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.natchuz.hub.kitpvp.ConcreteKitGamemode;

/**
 * Entry listener that does not interact with player states
 */
@AllArgsConstructor
public class EntryListener implements Listener {

    private final ConcreteKitGamemode plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective health = board.registerNewObjective("health", "health",
                ChatColor.DARK_RED + "❤");
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        event.getPlayer().setScoreboard(board);

        LobbyState.beginNewState(plugin, event.getPlayer(), plugin.getMainLobby());
    }
}
