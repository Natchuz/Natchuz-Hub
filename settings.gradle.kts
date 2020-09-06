pluginManagement {
    repositories {
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
        mavenCentral()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }
}

rootProject.name = "natchuz-hub"

include("utils")
include("protocol")
include("proxy")
include("sponge")
include("core")

/* Gamemodes */
include("lobby")
// include("kitpvp") currently not in use

/* Backend */
include("backend:state")