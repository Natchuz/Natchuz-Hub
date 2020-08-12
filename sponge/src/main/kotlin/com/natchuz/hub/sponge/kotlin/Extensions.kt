package com.natchuz.hub.sponge.kotlin

import org.spongepowered.api.plugin.PluginManager
import org.spongepowered.api.service.ServiceManager
import java.util.*

/**
 * Kotlin syntax for accessing service
 */
inline fun <reified T> ServiceManager.provide(): Optional<T> = provide(T::class.java)

/**
 * Kotlin shortcut for providing service or throwing an error if not found
 */
inline fun <reified T> ServiceManager.require(errorMessage: String = "Could not provide ${T::class.java.name}",
                                              crossinline callback: T.() -> Unit) =
        callback.invoke(provide(T::class.java).orElseThrow { AssertionError(errorMessage) })

/**
 * Kotlin shortcut for providing service or throwing an error if not found
 */
inline fun <reified T> ServiceManager.require(errorMessage: String = "Could not provide ${T::class.java.name}"): T =
        provide(T::class.java).orElseThrow { AssertionError(errorMessage) }

/**
 * Get plugin instance directly
 */
inline fun <reified T> PluginManager.getPluginInstance(pluginID: String): Optional<T> {
    val res = getPlugin(pluginID)
    return if (!res.isPresent) {
        Optional.empty()
    } else {
        @Suppress("UNCHECKED_CAST")
        res.get().instance as Optional<T>
    }
}