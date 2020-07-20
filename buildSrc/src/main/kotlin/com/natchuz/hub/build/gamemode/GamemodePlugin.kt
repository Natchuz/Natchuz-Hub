package com.natchuz.hub.build.gamemode

import Deps
import Vars
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.JavaExec
import java.io.File

open class GamemodePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("gamemode", GamemodeExtension::class.java, project)

        project.tasks.register("prepareEnvironment", Copy::class.java) { task ->
            task.from(extension.baseProject.configurations.getByName(Vars.LOCAL_SERVER_CONFIGURATION))
            task.from(extension.testMapFile) { scope ->
                scope.into("plugins/NatchuzHub")
            }
            task.from(project.configurations.getByName("shadow").artifacts.files) { scope ->
                scope.into("plugins")
            }
            task.into(extension.workDirectory)
            task.dependsOn("build")
            task.group = Companion.TASKS_GROUP
        }

        project.tasks.register("cleanRuntime", Delete::class.java) { task ->
            task.delete(project.files(extension.workDirectory))
            task.group = Companion.TASKS_GROUP
        }

        project.tasks.register("run", JavaExec::class.java) { task ->
            task.dependsOn("prepareEnvironment")
            task.workingDir = File(extension.workDirectory)
            task.classpath = project.files("${extension.workDirectory}/paper.jar")
            task.systemProperties["server.context"] = "standalone"
            task.environment["SERVERID"] = "testing-1"
            task.environment["SERVERTYPE"] = "testing"
            task.group = Companion.TASKS_GROUP
        }

        project.dependencies.add("implementation", extension.baseProject)
        project.dependencies.add("annotationProcessor", Deps.PLUGIN_ANNOTATIONS)
        project.dependencies.add("compileOnly", Deps.PLUGIN_ANNOTATIONS)
    }

    companion object {
        const val TASKS_GROUP = "gamemode"
    }
}