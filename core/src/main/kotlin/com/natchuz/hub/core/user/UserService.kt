package com.natchuz.hub.core.user

import java.util.*

interface UserService {
    fun getUser(uuid: UUID): User
}

