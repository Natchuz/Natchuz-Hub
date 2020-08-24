package com.natchuz.hub.backend.state

import redis.clients.jedis.Jedis
import java.time.Instant
import java.util.*
import kotlin.time.TimeMark

/**
 * Services handles entire logic of requests
 */
interface Service {
    /**
     * Login player
     */
    fun loginPlayer(player: UUID): PlayerLoginStatus

    /**
     * Logout player
     */
    fun logoutPlayer(player: UUID) : Boolean

    /**
     * Get player flags
     */
    fun getPlayerFlags(player: UUID) : List<PlayerFlags>
}

enum class PlayerFlags {
    PROXY_JOIN
}

/** Default Service that uses Redis as database */
class DefaultService private constructor(private val jedis: Jedis) : Service {

    override fun loginPlayer(player: UUID): PlayerLoginStatus {
        jedis["player.$player.login"] = Instant.now().toString()
        setPlayerFlags(player, PlayerFlags.PROXY_JOIN)
        return PlayerLoginStatus.Ok("lb")
    }

    override fun logoutPlayer(player: UUID) : Boolean {
        val multi = jedis.multi()
        val result = multi.del("player.$player.flags")
        multi.del("player.$player.flags")
        multi.exec()
        return result.get() == 1.toLong()
    }

    override fun getPlayerFlags(player: UUID) : List<PlayerFlags> {
        return jedis["player.$player.flags"]?.split(" | ")?.map(PlayerFlags::valueOf) ?: emptyList()
    }

    private fun setPlayerFlags(player: UUID, vararg flags: PlayerFlags) {
        jedis["player.$player.flags"] = flags.joinToString(separator = " | ")
    }

    companion object Factory {
        /**
         * Construct new service
         */
        fun construct(jedis: Jedis): DefaultService {
            return DefaultService(jedis)
        }
    }
}

fun DefaultService.Factory.construct(redisAddress: String) : DefaultService {
    return construct(Jedis(redisAddress))
}