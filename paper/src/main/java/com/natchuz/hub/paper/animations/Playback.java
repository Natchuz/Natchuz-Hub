package com.natchuz.hub.paper.animations;

import org.bukkit.entity.Player;

import java.util.List;

public interface Playback<V> {

    void play();

    void pause();

    void restart();

    void setLoop(boolean loop);

    void addPlayer(Player player);

    void removePlayer(Player player);

    List<Player> getTargets();
}
