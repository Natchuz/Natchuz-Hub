package com.natchuz.hub.build.gamemode

import org.gradle.api.Project

open class GamemodeExtension(project: Project) {
    /**
     * The name of testing map to load
     */
    open var testMapFile: String = "testMap.zip"

    /**
     * Which project from copy server preset
     */
    open var baseProject: Project = project.project(":core")

    /**
     * Where to store runtime files
     */
    open var workDirectory: String = "${project.buildDir}/runtime"
}