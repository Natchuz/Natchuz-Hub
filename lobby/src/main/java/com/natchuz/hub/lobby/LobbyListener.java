package com.natchuz.hub.lobby;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import com.natchuz.hub.paper.managers.DialogManager;
import com.natchuz.hub.paper.regions.BlockVectors;
import com.natchuz.hub.protocol.state.JoinFlags;
import com.natchuz.hub.utils.VersionInfo;
import com.natchuz.hub.core.content.Items;
import com.natchuz.hub.core.content.ui.CosmeticDialog;
import com.natchuz.hub.core.content.ui.MenuDialog;
import com.natchuz.hub.core.content.ui.RewardsDialog;
import com.natchuz.hub.core.user.User;

import static com.natchuz.hub.paper.Color.*;

public class LobbyListener implements Listener {

    private final PrivilegedFacade client;
    private final VersionInfo versionInfo;

    private LivingEntity rewardsDealer;
    private LivingEntity kitpvpNPC;

    private BossBar infoBossBar;

    private final MapConfig mapConfig;

    public LobbyListener(PrivilegedFacade client, MapConfig mapConfig) {
        this.mapConfig = mapConfig;
        this.client = client;
        this.versionInfo = new VersionInfo(LobbyListener.class);
    }

    private ItemStack generatePlayerMenuSkull(Player player) {
        return new StackBuilder(Material.PLAYER_HEAD)
                .name(DARK_PURPLE + BOLD + "Menu")
                .meta(SkullMeta.class, (m) -> m.setOwningPlayer(player))
                .doneGUI();
    }

    //region event handlers

    @EventHandler
    public void onEnable(PluginEnableEvent event) {

        // add rewards dealer
        Location rewardsDealerLocation = BlockVectors.centerFlat(mapConfig.getRewards());
        rewardsDealer = rewardsDealerLocation.getWorld().spawn(rewardsDealerLocation, WanderingTrader.class);
        rewardsDealer.setCustomName("Rewards dealer");
        rewardsDealer.setAI(false);

        // kitpvp npc
        Location kitpvpNpcLocation = BlockVectors.centerFlat(mapConfig.getKitpvp());
        kitpvpNPC = kitpvpNpcLocation.getWorld().spawn(kitpvpNpcLocation, Zombie.class);
        kitpvpNPC.setCustomName("Kit PvP");
        kitpvpNPC.setAI(false);

        // set a time for a map
        Bukkit.getScheduler().scheduleSyncRepeatingTask(client.getPlugin(),
                () -> client.getPlugin().getServer().getWorld("world").setTime(5000), 0, 100);

        infoBossBar = Bukkit.createBossBar(BOLD + "Natchuz " + YELLOW + BOLD + "HUB",
                BarColor.BLUE, BarStyle.SOLID);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        client.getPlayer(player.getUniqueId()).thenAcceptAsync(o -> {
            o.ifPresent(info -> {
                if (info.getJoinFlags().contains(JoinFlags.PROXY_JOIN)) {
                    player.sendTitle(WHITE + BOLD + "Natchuz " + RESET + YELLOW + BOLD + "HUB",
                            versionInfo.getDisplay(), 10, 70, 20);
                }
            });
        });

        player.teleport(BlockVectors.centerFlat(mapConfig.getSpawn()));

        client.getSidebarManager().set(event.getPlayer(), (p) -> {
            String[] sideBar = new String[2];
            User user = client.getProfile(p);
            sideBar[0] = "Rank: " + user.getRank().getName();
            sideBar[1] = "Coins: " + YELLOW + user.getCoins();

            return sideBar;
        });

        infoBossBar.addPlayer(player);

        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.addItem(generatePlayerMenuSkull(player), Items.COSMETICS);

        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (ItemUtils.similar(itemInMainHand, Items.COSMETICS)) {
            DialogManager.openDialog(new CosmeticDialog(), player);
            return;
        }
        if (ItemUtils.equalsName(itemInMainHand, generatePlayerMenuSkull(player))) {
            DialogManager.openDialog(new MenuDialog(), player);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (rewardsDealer.getUniqueId().equals(entity.getUniqueId())) {
            event.setCancelled(true);
            DialogManager.openDialog(new RewardsDialog(), player);
        }

        if (kitpvpNPC.getUniqueId().equals(entity.getUniqueId())) {
            event.setCancelled(true);
            client.send(player.getName(), "kp");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // check if player falls under the map
        if (event.getTo().getY() < 90) {
            player.teleport(mapConfig.getSpawn());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventory(InventoryInteractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityBurn(EntityCombustEvent event) {
        event.setCancelled(true);
    }

    //region world events

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

    //endregion

    //endregion

}
