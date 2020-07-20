package com.natchuz.hub.paper.animations;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;

public class BasicPlayback<V> implements Playback<V> {

    private boolean isPlaying;
    private int tick;
    private BukkitTask task;
    private boolean loop;

    private final List<Player> targets;
    private Plugin plugin;

    private FrameHandler<V> frameHandler;
    private Animation<V> animation;

    public static <V> BasicPlayback<V> createPlayback(Animation<V> anim, FrameHandler<V> frameHandler,
                                                      boolean loop, Plugin plugin) {
        BasicPlayback<V> playback = new BasicPlayback<>();

        playback.loop = loop;
        playback.frameHandler = frameHandler;
        playback.plugin = plugin;
        playback.animation = anim;

        return playback;
    }

    private BasicPlayback() {
        targets = new LinkedList<>();
    }

    private void update() {
        if (isPlaying && (task == null || task.isCancelled())) {
            task = Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                tick = 0;
                while (tick < animation.getLength()) {
                    if (isPlaying) {
                        for (Player p : targets) {
                            frameHandler.handle(animation.getFrame(tick), tick, p);
                        }
                        tick++;
                        if (tick >= animation.getLength() && loop) {
                            tick = 0;
                        }
                    }
                    try {
                        Thread.sleep(animation.getSpacing());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isPlaying = false;
            });
        }
    }

    @Override
    public void play() {
        isPlaying = true;
        update();
    }

    @Override
    public void pause() {
        isPlaying = false;
    }

    @Override
    public void restart() {
        tick = 0;
    }

    @Override
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public void addPlayer(Player player) {
        targets.add(player);
    }

    @Override
    public void removePlayer(Player player) {
        targets.remove(player);
    }

    @Override
    public List<Player> getTargets() {
        return targets;
    }
}
