@file:JvmName("UUIDConverter")
package com.natchuz.hub.utils

import java.util.*

/**
 * Convert given UUID to condensed string (without dashes)
 */
fun UUID.toCondensed() : String {
    return toString().replace("-", "")
}

/**
 * Creates UUID from condensed string
 */
fun String.fromCondensed(): UUID {
    return UUID.fromString(replace("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex(), "$1-$2-$3-$4-$5"))
}