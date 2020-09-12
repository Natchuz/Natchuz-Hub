package com.natchuz.hub.core.context

import com.natchuz.hub.core.map.LocalMapRepository
import com.natchuz.hub.core.map.MapRepository
import com.natchuz.hub.core.modules.Module
import com.natchuz.hub.core.proxy.ProxyBackend
import com.natchuz.hub.core.proxy.StandaloneProxyBackend
import com.natchuz.hub.core.user.StandaloneUserService
import com.natchuz.hub.core.user.UserService
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