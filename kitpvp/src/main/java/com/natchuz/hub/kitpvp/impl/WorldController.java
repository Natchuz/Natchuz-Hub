package com.natchuz.hub.kitpvp.impl;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.Plugin;

import com.natchuz.hub.kitpvp.map.MapConfig;

public class WorldController implements Listener {

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        event.setCancelled(!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM));
    }

    @EventHandler
    public void onRaid(RaidTriggerEvent event) {
        event.setCancelled(true);
    }

    public static WorldController initWorldController(MapConfig config, Plugin plugin, World world) {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->
                world.setTime(config.getWorldTime()), 0, 10);

        return new WorldController();
    }
}
