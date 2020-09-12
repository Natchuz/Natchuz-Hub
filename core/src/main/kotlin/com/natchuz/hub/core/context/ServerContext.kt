package com.natchuz.hub.core.context

import com.natchuz.hub.core.map.MapRepository
import com.natchuz.hub.core.modules.Module
import com.natchuz.hub.core.proxy.ProxyBackend
import com.natchuz.hub.core.service.PlayerService
import com.natchuz.hub.core.user.UserService

/**
 * ServerContext is an abstract factory for a different backends, starting from simple Bungeecord client to map loading etc.
 * This will be used in future to create testing context that will fake all backends, to make gamemode testing easier.
 */
interface ServerContext {
    /**
     * Creates [map repository][MapRepository] for this context
     */
    fun createMapRepository(): MapRepository

    /**
     * Creates a [proxy backaend][ProxyBackend]
     */
    fun createProxyBackend(): ProxyBackend

    /**
     * To be documented
     */
    fun createPlayerService(): PlayerService

    /**
     * Create user service
     */
    fun createUserService(): UserService

    /**
     * Creates a list of [modules][Module] required to be registered in this network context
     */
    fun createModules(): List<Module>
}