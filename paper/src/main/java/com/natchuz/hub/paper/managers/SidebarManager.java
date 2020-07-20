package com.natchuz.hub.paper.managers;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.natchuz.hub.paper.Color;

/**
 * Manages custom scoreboard
 */
public class SidebarManager {

    private final Map<Player, Function<Player, String[]>> entries;
    private final String scoreboardTitle;
    private final String serverID;

    /**
     * Creates simple SidebarManager
     *
     * @param scoreboardTitle title of scoreboard
     */
    public SidebarManager(String scoreboardTitle, String serverID) {
        this.scoreboardTitle = Color.YELLOW + Color.BOLD + scoreboardTitle;
        this.entries = new HashMap<>();
        this.serverID = serverID;
    }

    /**
     * Set scoreboard for player
     *
     * @param player player to have scoreboard
     * @param fun    a function that generates scoreboard content. Takes a player as an argument and returns array of Strings.
     */
    public void set(Player player, Function<Player, String[]> fun) {
        entries.put(player, fun);
        refresh(player);
    }

    /**
     * Delete a scoreboard for player
     *
     * @param player the player who remove the current scoreboard
     */
    public void unset(Player player) {
        entries.remove(player);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    /**
     * Refresh scoreboard for player
     *
     * @param player the player who refreshes scoreboard
     */
    public void refresh(Player player) {
        Function<Player, String[]> fun = entries.get(player);
        String[] ret = fun.apply(player);

        ret = (String[]) ArrayUtils.add(ret, 0, Color.GREY + "Server: " + Color.DARK_GREY + serverID);
        ret = (String[]) ArrayUtils.add(ret, 1, "");
        ret = (String[]) ArrayUtils.add(ret, "");
        ret = (String[]) ArrayUtils.add(ret, Color.YELLOW + "natchuzhub.com");

        Scoreboard scoreboard = player.getScoreboard();
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        try {
            scoreboard.getObjective("sidebar").unregister();
        } catch (Exception ignored) {
        }
        Objective objective = scoreboard.registerNewObjective("sidebar", "dummy", scoreboardTitle, RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        String separator = "";

        //since every line has to be unique, we have to replace empty lines with spaces
        for (int i = 0; i < ret.length; i++) {
            String element = ret[ret.length - i - 1];
            if (element.equals("")) {
                separator += " ";
                element = separator;
            }
            objective.getScore(element).setScore(i + 1);
        }
    }
}
