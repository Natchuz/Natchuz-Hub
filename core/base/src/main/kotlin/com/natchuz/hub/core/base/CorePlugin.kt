package com.natchuz.hub.core.base

import com.google.inject.Inject
import com.natchuz.hub.core.api.MainFacade
import com.natchuz.hub.core.base.context.ServerContext
import com.natchuz.hub.core.base.map.AnvilZipMapLoader
import com.natchuz.hub.core.api.map.LoadedMap
import com.natchuz.hub.core.base.map.MapLoader
import com.natchuz.hub.core.api.map.MapManifest
import com.natchuz.hub.core.api.proxy.ProxyBackend
import com.natchuz.hub.core.api.user.UserService
import org.slf4j.Logger
import org.spongepowered.api.Game
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.game.state.GamePostInitializationEvent
import org.spongepowered.api.event.game.state.GameStartingServerEvent
import org.spongepowered.api.plugin.Dependency
import org.spongepowered.api.plugin.Plugin
import java.util.*

/**
 * This is entry class of Core plugin for Sponge
 */
@Plugin(
        id = CorePlugin.ID,
        name = "Core Plugin",
        version = "1.0",
        dependencies = [Dependency(id = "natchuz-hub-sponge-utils")]
)
class CorePlugin @Inject constructor(
        private val game: Game,
        private val logger: Logger,
) : MainFacade {

    private val context: ServerContext

    private lateinit var serverID: String

    private lateinit var userService: UserService
    private lateinit var proxyBackend: ProxyBackend

    private lateinit var loadedMap: LoadedMap

    init {
        /* Load context.
        * We are required to to this by reflection, to avoid circular dependency in gradle.
        * Every context class should have one constructor with Game parameter,
        * so @JvmOverloads annotation with default parameters is the best for Kotlin.
        * We also need to shadow context implementation via Shadow Plugin in gradle */
        context = Class.forName(when (val contextName = System.getProperty("server.context")) {
            "network" -> "com.natchuz.hub.core.network.NetworkContext"
            "standalone" -> "com.natchuz.hub.core.standalone.StandaloneContext"
            else -> throw IllegalStateException("Unknown context : $contextName")
        }).getConstructor(Game::class.java).newInstance(game) as ServerContext
        logger.info("Using context: " + context::class.qualifiedName)
    }

    @Listener
    fun onEnable(event: GameInitializationEvent) {
        /* Obtain server ID */
        serverID = System.getenv("SERVERID")

        /* Construct basic services */
        proxyBackend = context.createProxyBackend()
        userService = context.createUserService()

        /* Load all context-depended listeners */
        context.createModules()
                .forEach { module -> game.eventManager.registerListeners(this, module) }
    }

    @Listener
    fun onPost(event: GamePostInitializationEvent) {
        /* Register all services */
        with(game.serviceManager) {
            setProvider(this@CorePlugin, ProxyBackend::class.java, proxyBackend)
            setProvider(this@CorePlugin, UserService::class.java, userService)
        }
    }

    @Listener
    fun onStarting(event: GameStartingServerEvent) {
        /* Load test map */
        val repository = context.createMapRepository()
        val loader: MapLoader = AnvilZipMapLoader(repository)
        loadedMap = loader.load("testMap")
    }

    //region legacy facade implementation

    override fun getMapManifest(): MapManifest {
        return loadedMap.manifest
    }

    override fun getMapConfiguration(): String {
        return loadedMap.mapConfiguration
    }

    override fun getMapWorld(): UUID {
        return loadedMap.world
    }

    override fun getServerId(): String {
        return serverID
    }

    //endregion

    companion object {
        const val ID = "natchuz-hub-core"
    }
}