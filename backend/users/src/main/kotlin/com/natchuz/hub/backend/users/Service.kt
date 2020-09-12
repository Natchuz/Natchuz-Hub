package com.natchuz.hub.backend.users

import java.util.*

interface Service {
    /**
     * Get user and create new if doesn't exist
     */
    fun getUser(playerUUID: UUID): String
}


