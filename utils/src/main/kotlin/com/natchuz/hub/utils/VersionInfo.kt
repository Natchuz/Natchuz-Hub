package com.natchuz.hub.utils

import java.util.*
import kotlin.reflect.KClass

/**
 * Loads information about jar
 *
 * @link https://github.com/nemerosa/versioning#versioning-info
 */
class VersionInfo constructor(clazz: Class<*>, location: String) {

    val build: String
    val branch: String
    val base: String
    val branchID: String
    val branchType: String
    val commit: String
    val display: String
    val full: String
    val scm: String
    val tag: String
    val lastTag: String
    val isDirty: Boolean

    constructor(clazz: Class<*>) : this(clazz, "/version.properties")

    constructor(kClass: KClass<*>) : this(kClass.java)

    init {
        Properties().apply {
            load(clazz.getResourceAsStream(location))
            build = getProperty("VERSION_BUILD")
            branch = getProperty("VERSION_BRANCH")
            base = getProperty("VERSION_BASE")
            branchID = getProperty("VERSION_BRANCHID")
            branchType = getProperty("VERSION_BRANCHTYPE")
            commit = getProperty("VERSION_COMMIT")
            display = getProperty("VERSION_DISPLAY")
            full = getProperty("VERSION_FULL")
            scm = getProperty("VERSION_SCM")
            tag = getProperty("VERSION_TAG")
            lastTag = getProperty("VERSION_LAST_TAG")
            isDirty = getProperty("VERSION_DIRTY") == "true"
        }
    }
}