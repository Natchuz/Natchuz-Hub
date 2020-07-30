package com.natchuz.hub.lobby;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.FoodData;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.data.Has;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.world.ChangeWorldWeatherEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.natchuz.hub.paper.regions.BlockVectors;
import com.natchuz.hub.protocol.state.JoinFlags;
import com.natchuz.hub.protocol.state.StateDatabase;
import com.natchuz.hub.utils.VersionInfo;
import com.natchuz.hub.core.proxy.ProxyBackend;

public class LobbyListener {

    private final VersionInfo versionInfo;

    private UUID rewardsDealer;
    private UUID kitpvpNPC;

    private ServerBossBar infoBossBar;

    private final MapConfig mapConfig;

    public LobbyListener(MapConfig mapConfig) {
        this.mapConfig = mapConfig;
        this.versionInfo = new VersionInfo(LobbyListener.class);
    }

    /*private ItemStack generatePlayerMenuSkull(Player player) {
        return new StackBuilder(Material.PLAYER_HEAD)
                .name(DARK_PURPLE + TextStyles.BOLD + "Menu")
                .meta(SkullMeta.class, (m) -> m.setOwningPlayer(player))
                .doneGUI();
    }*/

    //region event handlers

    @Listener
    public void onEnable(GameStartingServerEvent event) {

        // add rewards dealer
        Location<World> rewardsDealerLocation = BlockVectors.centerFlat(mapConfig.getRewards());
        World extent = rewardsDealerLocation.getExtent();

        DataContainer container = EntityArchetype.builder()
                .set(Keys.DISPLAY_NAME, Text.of("Rewards Delaer"))
                .set(Keys.AI_ENABLED, false)
                .type(EntityTypes.VILLAGER)
                .build().getEntityData();
        Entity rewardEntity = extent.createEntity(container, rewardsDealerLocation.getPosition())
                .orElseThrow(() -> new RuntimeException("I ran of idea how to write those exceptions"));
        extent.spawnEntity(rewardEntity);
        rewardsDealer = rewardEntity.getUniqueId();

        // kitpvp npc
        Location<World> kitpvpNpcLocation = BlockVectors.centerFlat(mapConfig.getKitpvp());
        World extent2 = kitpvpNpcLocation.getExtent();

        container = EntityArchetype.builder()
                .set(Keys.DISPLAY_NAME, Text.of("Kit PvP"))
                .set(Keys.AI_ENABLED, false)
                .type(EntityTypes.ZOMBIE)
                .build().getEntityData();
        Entity kipvpEntity = extent.createEntity(container, kitpvpNpcLocation.getPosition())
                .orElseThrow(() -> new RuntimeException("I ran of idea how to write those exceptions"));
        extent.spawnEntity(kipvpEntity);
        kitpvpNPC = kipvpEntity.getUniqueId();


        // set a time for a map
        Task.builder().interval(3, TimeUnit.SECONDS).execute(() -> Sponge.getServer()
                .getWorlds().stream().findAny().get()
                .getProperties().setWorldTime(5000)).submit(Sponge.getPluginManager().getPlugin("natchuz-hub-lobby")
                .get().getInstance());

        ServerBossBar.builder().name(Text.of(TextStyles.BOLD, "Natchuz ", TextColors.YELLOW, TextStyles.BOLD, "HUB"))
                .color(BossBarColors.YELLOW).build();
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();

        Sponge.getServiceManager().provide(StateDatabase.class)
                .orElseThrow(() -> new RuntimeException("State database was not provided!"))
                .getPlayer(player.getUniqueId()).thenAcceptAsync(o -> o.ifPresent(info -> {
            if (info.getJoinFlags().contains(JoinFlags.PROXY_JOIN)) {
                player.sendTitle(Title.builder().title(Text.of(TextColors.WHITE, TextStyles.BOLD, "Natchuz ",
                        TextStyles.RESET, TextColors.YELLOW, TextStyles.BOLD, "HUB"))
                        .subtitle(Text.of(versionInfo.getDisplay()))
                        .fadeIn(10).fadeOut(70).stay(20).build());
            }
        }));

        player.setTransform(new Transform<>(BlockVectors.centerFlat(mapConfig.getSpawn())));

        /*client.getSidebarManager().set(event.getPlayer(), (p) -> {
            String[] sideBar = new String[2];
            User user = client.getProfile(p);
            sideBar[0] = "Rank: " + user.getRank().getName();
            sideBar[1] = "Coins: " + TextColors.YELLOW + user.getCoins();

            return sideBar;
        });*/

        /*infoBossBar.addPlayer(player);
        CarriedInventory<? extends Carrier> inventory = player.getInventory();
        inventory.clear();
        inventory.set()
        inventory.addItem(generatePlayerMenuSkull(player), Items.COSMETICS);*/

        player.offer(Keys.GAME_MODE, GameModes.ADVENTURE).ifNotSuccessful(() ->
                new RuntimeException("Player didn't accepted gamemode"));
    }

    @Listener
    public void onPlayerInteract(InteractItemEvent event, @First Player player) {
        ItemStackSnapshot itemInMainHand = event.getItemStack();

        /*if (ItemUtils.similar(itemInMainHand, Items.COSMETICS)) {
            DialogManager.openDialog(new CosmeticDialog(), player);
            return;
        }
        if (ItemUtils.equalsName(itemInMainHand, generatePlayerMenuSkull(player))) {
            DialogManager.openDialog(new MenuDialog(), player);
        }*/
    }

    @Listener
    public void onEntityInteract(InteractEntityEvent event, @First Player player) {
        UUID entity = event.getTargetEntity().getUniqueId();

        /*if (rewardsDealer.getUniqueId().equals(entity.getUniqueId())) {
            event.setCancelled(true);
            DialogManager.openDialog(new RewardsDialog(), player);
        }*/

        if (entity.equals(kitpvpNPC)) {
            event.setCancelled(true);
            Sponge.getServiceManager().provide(ProxyBackend.class)
                    .orElseThrow(() -> new RuntimeException("Did not found proxy backend"))
                    .send(player.getName(), "kp");
        }
    }

    @Listener
    public void onMove(MoveEntityEvent event, @First Player player) {

        // check if player falls under the map
        if (event.getToTransform().getPosition().getY() < 90) {
            player.setTransform(new Transform<>(mapConfig.getSpawn()));
            player.playSound(SoundTypes.ENTITY_ENDERMEN_TELEPORT, player.getPosition(), 1);
        }
    }

    /*@Listener(priority = EventPriority.LOW)
    public void onInventory(InventoryInteractEvent event) {
        event.setCancelled(true);
    }*/

    @Listener
    public void onFoodChange(ChangeDataHolderEvent.ValueChange event, @Has(FoodData.class) Entity entity) {
        event.setCancelled(true);
    }

    @Listener
    public void onDamage(DamageEntityEvent event) {
        event.setCancelled(true);
    }

    @Listener
    public void onBlockDestroy(ChangeBlockEvent.Break event) {
        event.setCancelled(true);
    }

    @Listener
    public void onBlockPlace(ChangeBlockEvent.Place event) {
        event.setCancelled(true);
    }

    /*@Listener
    public void onEntityBurn(EntityCombustEvent event) {
        event.setCancelled(true);
    }*/

    //region world events

    @Listener
    public void onWeather(ChangeWorldWeatherEvent event) {
        event.setCancelled(true);
    }

    @Listener
    public void onSpawn(SpawnEntityEvent event) {
        event.getContext().get(EventContextKeys.SPAWN_TYPE).ifPresent(e -> {
            event.setCancelled(!e.equals(SpawnTypes.CUSTOM)
                    && !e.equals(SpawnTypes.PLUGIN)
                    && !e.equals(SpawnTypes.FALLING_BLOCK));
        });
    }

    //endregion

    //endregion

}
