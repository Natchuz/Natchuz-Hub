package com.natchuz.hub.core

import com.natchuz.hub.core.api.map.LocalMapRepository
import com.natchuz.hub.core.api.map.MapRepository
import com.natchuz.hub.core.api.proxy.ProxyBackend
import com.natchuz.hub.core.api.user.UserService
import com.natchuz.hub.core.network.Module
import com.natchuz.hub.core.network.ServerContext
import org.spongepowered.api.Game
import java.io.File

/**
 * Standalone context is used for testing
 */
class StandaloneContext(
        val game: Game
) : ServerContext {

    override fun createMapRepository(): MapRepository {
        val repo = File(game.gameDirectory.toFile().absoluteFile.toString() + "/maps")
        repo.mkdirs()
        return LocalMapRepository(repo)
    }

    override fun createProxyBackend(): ProxyBackend = StandaloneProxyBackend()

    override fun createUserService(): UserService = StandaloneUserService()

    override fun createModules(): List<Module> = emptyList()
}