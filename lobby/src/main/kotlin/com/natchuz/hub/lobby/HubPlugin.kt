package com.natchuz.hub.lobby

import com.google.gson.GsonBuilder
import com.google.inject.Inject
import com.natchuz.hub.core.CorePlugin
import com.natchuz.hub.core.api.MainFacade
import com.natchuz.hub.sponge.kotlin.getPluginInstance
import com.natchuz.hub.sponge.kotlin.typeToken
import com.natchuz.hub.sponge.serialization.LocationDeserializer
import org.slf4j.Logger
import org.spongepowered.api.Game
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Dependency
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.plugin.PluginManager
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

@Suppress("ThrowableNotThrown")
@Plugin(id = HubPlugin.ID, dependencies = [Dependency(id = CorePlugin.ID)])
class HubPlugin {

    @Inject private lateinit var game: Game
    @Inject private lateinit var logger: Logger

    @Listener
    fun onReady(event: GameStartedServerEvent) {
        logger.info("Loading lobby plugin")

        val core = Sponge.getPluginManager().getPluginInstance<MainFacade>(CorePlugin.ID)
                .orElseThrow { AssertionError("Not found core plugin") }
        val mapWorld = game.server.getWorld(core.mapWorld)
                .orElseThrow { AssertionError("Not found expected world") }
        val gson = GsonBuilder().registerTypeAdapter(typeToken<Location<World>>(), LocationDeserializer(mapWorld))
                .create()
        val mapConfig = gson.fromJson(core.mapConfiguration, MapConfig::class.java)

        game.eventManager.registerListeners(this, LobbyListener(mapConfig))
        logger.info("Successfully loaded lobby plugin")
    }

    companion object {
        const val ID = "natchuz-hub-lobby"
        val PluginNotFound = AssertionError("Plugin was not found by Sponge")
    }
}

/**
 * Shortcut for accessing Hub plugin container
 */
fun PluginManager.getHubPlugin(): PluginContainer =
        getPlugin(HubPlugin.ID).orElseThrow { HubPlugin.PluginNotFound }

/**
 * Shortcut for accessing Hub plugin instance
 */
fun PluginManager.getHubPluginInstance() = getHubPlugin().instance
        .orElseThrow { AssertionError("Could not recive Hub plugin instance") } as HubPlugin
