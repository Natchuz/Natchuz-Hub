package com.natchuz.hub.kitpvp.impl;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

import com.natchuz.hub.paper.managers.DialogManager;
import com.natchuz.hub.kitpvp.ConcreteKitGamemode;
import com.natchuz.hub.kitpvp.impl.ui.SpawnContextDialog;
import com.natchuz.hub.kitpvp.map.ArenaSpawn;
import com.natchuz.hub.kitpvp.model.content.EmptyKit;

import static com.natchuz.hub.paper.Color.GOLD;

/**
 * Lobby state when player is in lobby
 */
public class LobbyState extends PlayerState {

    private final Lobby lobby; // to avoid tight coupling

    /**
     * Basic lobby state
     */
    private LobbyState(ConcreteKitGamemode gamemode, Player player, Lobby lobby) {
        super(gamemode, player);
        this.lobby = lobby;
    }

    public String[] scoreboardHandler(Player p) {
        String[] ret = new String[1];
        ret[0] = "Coins: " + GOLD + getProfile(p).getCoins();
        return ret;
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();

        if (entity.getUniqueId().equals(lobby.getHubNpc().getUniqueId())) {
            sendToLobby(player);
        }
    }

    @Override
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getTo().getY() < lobby.getVoidHeight()) {
            player.teleport(lobby.getSpawn());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
        }
    }

    @Override
    public void onEntityDamageEvent(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        lobby.getIdFromEntity(entity).ifPresent(i -> {
            Arena arena = getMainArena();

            ArenaSpawn spawn = arena.getArenaSpawns().get(ThreadLocalRandom.current()
                    .nextInt(0, arena.getArenaSpawns().size()));

            ArenaState.beginArenaState(getGamemode(), getPlayer(), getMainArena(), spawn,
                    getProfile(getPlayer()).getKit(i).flatMap(k -> getKitConfig().getKit(k))
                            .orElse(EmptyKit.INSTANCE));
        });
    }

    @Override
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        lobby.getIdFromEntity(event.getRightClicked()).ifPresent(i ->
                DialogManager.openDialog(new SpawnContextDialog(getGamemode(), getMainArena(), i), getPlayer()));
    }

    /**
     * Enters player into lobby state
     */
    public static void beginNewState(ConcreteKitGamemode plugin, Player player, Lobby lobby) {
        LobbyState state = new LobbyState(plugin, player, lobby);

        player.teleport(lobby.getSpawn());
        player.setGameMode(GameMode.ADVENTURE);

        // clear all effects
        player.setFoodLevel(20);
        player.setHealth(20);
        player.getInventory().clear();

        for (PotionEffect e : player.getActivePotionEffects()) {
            player.removePotionEffect(e.getType());
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                50000, 0, true, false, false));

        plugin.getSidebarManager().set(player, state::scoreboardHandler);

        plugin.initNewState(player, state);
    }
}
