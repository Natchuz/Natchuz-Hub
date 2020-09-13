package com.natchuz.hub.lobby

import com.natchuz.hub.backend.state.PlayerFlags
import com.natchuz.hub.core.api.proxy.ProxyBackend
import com.natchuz.hub.core.api.user.UserService
import com.natchuz.hub.sponge.kotlin.EntityUUID
import com.natchuz.hub.sponge.kotlin.require
import com.natchuz.hub.sponge.regions.BlockVectors
import com.natchuz.hub.utils.VersionInfo
import org.spongepowered.api.Sponge
import org.spongepowered.api.boss.BossBarColors
import org.spongepowered.api.boss.BossBarOverlays
import org.spongepowered.api.boss.ServerBossBar
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.manipulator.mutable.entity.FoodData
import org.spongepowered.api.effect.sound.SoundTypes
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.Transform
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.gamemode.GameModes
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.block.ChangeBlockEvent.Place
import org.spongepowered.api.event.cause.EventContextKeys
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes
import org.spongepowered.api.event.data.ChangeDataHolderEvent.ValueChange
import org.spongepowered.api.event.entity.DamageEntityEvent
import org.spongepowered.api.event.entity.InteractEntityEvent
import org.spongepowered.api.event.entity.MoveEntityEvent
import org.spongepowered.api.event.entity.SpawnEntityEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.filter.data.Has
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.event.world.ChangeWorldWeatherEvent
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors
import org.spongepowered.api.text.format.TextStyles
import org.spongepowered.api.text.title.Title
import java.util.concurrent.TimeUnit

class LobbyListener private constructor(private val mapConfig: MapConfig) {

    private val versionInfo: VersionInfo = VersionInfo(this::class)

    /** UUID of the NPC that have rewards for players */
    private lateinit var rewardsDealer: EntityUUID

    /** UUID of the NPC that teleports to KitPvP server */
    private lateinit var kitpvpNPC: EntityUUID

    /** Boss bar to be displayed to every player in lobby */
    private lateinit var infoBossBar: ServerBossBar

    companion object Factory {

        fun initLobby(mapConfig: MapConfig) : LobbyListener = LobbyListener(mapConfig).apply {
            //FIXME: these entities just cannot be created, because yes

            /*

            /* Spawn rewards npc */
            with(BlockVectors.centerFlat(mapConfig.rewards)) {
                val container = EntityArchetype.builder().run {
                    set(Keys.DISPLAY_NAME, Text.of("Rewards Dealer"))
                    set(Keys.AI_ENABLED, false)
                    type(EntityTypes.VILLAGER)
                    build()
                }.entityData

                val npc = extent.createEntity(container, position)
                        .orElseThrow { AssertionError("Could not create entity!") }

                if (!extent.spawnEntity(npc))
                    throw AssertionError("Could not spawn entity!")

                rewardsDealer = npc.uniqueId
            }

            /* Spawn kit pvp npc */
            with(BlockVectors.centerFlat(mapConfig.kitpvp)) {
                val container = EntityArchetype.builder().run {
                    set(Keys.DISPLAY_NAME, Text.of("Kit PvP"))
                    set(Keys.AI_ENABLED, false)
                    type(EntityTypes.ZOMBIE)
                    build()
                }.entityData

                val npc = extent.createEntity(container, position)
                        .orElseThrow { AssertionError("Could not create entity!") }

                if (!extent.spawnEntity(npc))
                    throw AssertionError("Could not spawn entity!")

                kitpvpNPC = npc.uniqueId
            }
             */

            /* Set map daytime */
            Task.builder().interval(3, TimeUnit.SECONDS).execute { ->
                Sponge.getServer().worlds.forEach { // To be replaced with Map Service in core
                    it.properties.worldTime = 5000
                }
            }.submit(Sponge.getPluginManager().getHubPluginInstance())

            /* Info boss bar that will be displayed to every player */
            infoBossBar = ServerBossBar.builder()
                    .name(Text.of(TextStyles.BOLD, "Natchuz ", TextColors.YELLOW, TextStyles.BOLD, "HUB"))
                    .color(BossBarColors.YELLOW)
                    .overlay(BossBarOverlays.PROGRESS)
                    .build()
        }
    }

    @Listener
    fun au(event: ClientConnectionEvent.Auth) {
        println("auth")
    }

    @Listener
    fun l(event: ClientConnectionEvent.Login) {
        println("login")
    }

    @Listener
    fun dis(event: ClientConnectionEvent.Disconnect) {
        println("dis ${event.cause}, ${event.context}, ${event.message}")
    }

    @Listener
    fun onPlayerJoin(event: ClientConnectionEvent.Join) {

        val player = event.targetEntity

        if (PlayerFlags.PROXY_JOIN in Sponge.getServiceManager().require<UserService>().getState(player.uniqueId).flags) {
            player.sendTitle(Title.builder()
                    .title(Text.of(TextColors.WHITE, TextStyles.BOLD, "Natchuz ",
                            TextStyles.RESET, TextColors.YELLOW, TextStyles.BOLD, "HUB"))
                    .subtitle(Text.of(versionInfo.display))
                    .fadeIn(10)
                    .fadeOut(70)
                    .stay(20)
                    .build())
        }

        player.sendMessage(Text.of("""
            |So there is an issue where entities just don't spawn. I've been stuck for a week there, 
            |and decided to move on and do more interesting things than fixing bugs
        """.trimMargin("|")))

        player.transform = Transform(BlockVectors.centerFlat(mapConfig.spawn))
        player.offer(Keys.GAME_MODE, GameModes.ADVENTURE)
                .ifNotSuccessful { RuntimeException("Player didn't accepted gamemode") }
    }

    @Listener
    fun onEntityInteract(event: InteractEntityEvent, @First player: Player) = when (event.targetEntity.uniqueId) {
        kitpvpNPC -> {
            event.isCancelled = true
            Sponge.getServiceManager().require<ProxyBackend>().send(player.name, "kp")
        }
        else -> Unit
    }

    @Listener
    fun onMove(event: MoveEntityEvent, @First player: Player) {
        if (event.toTransform.position.y < mapConfig.height) { // check if player falls under the map
            player.transform = Transform(mapConfig.spawn)
            player.playSound(SoundTypes.ENTITY_ENDERMEN_TELEPORT, player.position, 1.0)
        }
    }

    @Listener
    fun onSpawn(event: SpawnEntityEvent) {
        event.context.get(EventContextKeys.SPAWN_TYPE).ifPresent {
            event.isCancelled = when (it) {
                SpawnTypes.CUSTOM, SpawnTypes.PLUGIN, SpawnTypes.FALLING_BLOCK -> true
                else -> false
            }
        }
    }

    @Listener
    fun onFoodChange(event: ValueChange, @Has(FoodData::class) @First entity: Entity) {
        event.isCancelled = true
    }

    @Listener
    fun onDamage(event: DamageEntityEvent) {
        event.isCancelled = true
    }

    @Listener
    fun onBlockDestroy(event: ChangeBlockEvent.Break) {
        event.isCancelled = true
    }

    @Listener
    fun onBlockPlace(event: Place) {
        event.isCancelled = true
    }

    @Listener
    fun onWeather(event: ChangeWorldWeatherEvent) {
        event.isCancelled = true
    }

}