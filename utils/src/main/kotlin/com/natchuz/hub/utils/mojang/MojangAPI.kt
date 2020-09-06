package com.natchuz.hub.utils.mojang

import java.util.*

interface MojangAPI {
    /**
     * Returns username of user with specified UUID
     */
    fun getUsername(uuid: UUID): String?

    /**
     * Returns UUID of user with specified username
     */
    fun getUUID(username: String): UUID?
}