package com.natchuz.hub.sponge.kotlin

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Create generic type for GSON serializers
 */
inline fun <reified T> typeToken(): Type = object : TypeToken<T>() {}.type